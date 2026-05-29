package com.dontworry.crawler.service;

import com.dontworry.crawler.dto.PlaceCrawlerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallbackService {

    private final RestTemplate localRestTemplate;

    public void sendCallback(PlaceCrawlerResponse response, String callbackURL) {
        try {
            log.info("[CALLBACK-START] placeId={}, url={}", response.placeId(), callbackURL);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<PlaceCrawlerResponse> entity = new HttpEntity<>(response, headers);

            localRestTemplate.postForEntity(callbackURL, entity, Void.class);

            log.info("[CALLBACK-SUCCESS] placeId={}", response.placeId());
        } catch (Exception e) {
            log.error("[CALLBACK-FAIL] placeId={}, error={}", response.placeId(), e.getMessage(), e);
        }
    }

}
