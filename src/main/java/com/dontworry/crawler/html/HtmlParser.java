package com.dontworry.crawler.html;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class HtmlParser {

    public String getDescription(String html) {
        Document document = Jsoup.parse(html);

        String description = document.selectFirst("div.AX_W3._6sPQ") != null
                ? document.selectFirst("div.AX_W3._6sPQ").text()
                : "";

        return description;
    }

    public String getMenus(String html) {
        Document document = Jsoup.parse(html);

        log.info("lPzHi 개수: {}", document.select("span.lPzHi").size());
        log.info("E2jtL 개수: {}", document.select("li.E2jtL").size());

        return document.select("span.lPzHi").stream()
                .map(Element::text)
                .filter(text -> !text.isBlank())
                .collect(Collectors.joining(","));
    }
}
