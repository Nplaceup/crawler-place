package com.dontworry.crawler.html;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverPlaceHtmlClient {

    private final HtmlHeaderProvider htmlHeaderProvider;
    private final HtmlParser htmlParser;
    private final RestTemplate restTemplate;

    private static final String PLACE_HOME_URL = "https://pcmap.place.naver.com/place/%s/information";
    private static final String MENU_URL = "https://pcmap.place.naver.com/place/%s/menu/list";

    public Map<String, String> crawlingHtml(Long placeId) {
        Map<String, String> result = new HashMap<>();

        result.put("description", fetchDescription(placeId));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        result.put("menus", fetchMenus(placeId));

        return result;
    }

    private String fetchDescription(Long placeId) {
        String targetUrl = String.format(PLACE_HOME_URL, placeId);
        log.info("정보 페이지 호출 시작 -> {}", placeId);

        HttpHeaders headers = htmlHeaderProvider.htmlHeaders();
        ResponseEntity<byte[]> response = restTemplate.exchange(
                targetUrl, HttpMethod.GET, new HttpEntity<>(headers), byte[].class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            log.error("HTML fetch 실패, status: {}", response.getStatusCode());
            return "";
        }

        String html = new String(response.getBody(), StandardCharsets.UTF_8);
        return htmlParser.getDescription(html);
    }

    private String fetchMenus(Long placeId) {
        String menuUrl = String.format(MENU_URL, placeId);
        log.info("메뉴 페이지 호출 시작 -> {}", placeId);

        HttpHeaders headers = htmlHeaderProvider.htmlHeaders();
        ResponseEntity<byte[]> response = restTemplate.exchange(
                menuUrl, HttpMethod.GET, new HttpEntity<>(headers), byte[].class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            return "";
        }

        String html = new String(response.getBody(), StandardCharsets.UTF_8);
        return htmlParser.getMenus(html);
    }
}
