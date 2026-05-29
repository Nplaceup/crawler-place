package com.dontworry.crawler.html;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HtmlHeaderProvider {

    public HttpHeaders htmlHeaders() {
        HttpHeaders headers = new HttpHeaders();
        Map<String, String> headerContents = new HashMap<>();
        headerContents.put("referer", "https://map.naver.com/");
        headerContents.put("accept", "*/*");
        headerContents.put("accept-language", "ko");
        headerContents.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                + "(KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36");

        headerContents.forEach(headers::add);
        return headers;
    }
}
