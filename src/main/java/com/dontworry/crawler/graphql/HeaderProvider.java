package com.dontworry.crawler.graphql;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HeaderProvider {

    public HttpHeaders detailHeaders(String placeId) {
        HttpHeaders headers = new HttpHeaders();
        Map<String, String> headerContents = new HashMap<>();
        headerContents.put("content-type", "application/json");
        headerContents.put("origin", "https://pcmap.place.naver.com");
        headerContents.put("referer", "https://pcmap.place.naver.com/" + placeId);
        headerContents.put("accept", "*/*");
        headerContents.put("accept-language", "ko");
        headerContents.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                + "(KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36");

        headerContents.forEach(headers::add);
        return headers;
    }
}
