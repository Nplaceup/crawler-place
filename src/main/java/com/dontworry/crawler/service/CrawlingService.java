package com.dontworry.crawler.service;

import com.dontworry.crawler.dto.PlaceCrawlerResponse;
import com.dontworry.crawler.graphql.NaverPlaceApiClient;
import com.dontworry.crawler.html.NaverPlaceHtmlClient;
import com.dontworry.crawler.util.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingService {

    private final NaverPlaceApiClient naverPlaceApiClient;
    private final NaverPlaceHtmlClient naverPlaceHtmlClient;
    private final CallbackService callbackService;

    public void processCrawling(Long placeId, LocalDate crawlDate, String cidList, String callbackURL) {
        log.info("[DETAIL-CRAWLING-TRIGGERED] placeId={}", placeId);

        JsonNode detailResponse = naverPlaceApiClient.crawlingPlaceDetail(placeId);
        List<JsonNode> allReviewItems = naverPlaceApiClient.crawlingAllVisitorReviews(placeId, cidList);
        Map<String, String> htmlResponse = naverPlaceHtmlClient.crawlingHtml(placeId);

        if (detailResponse != null) {
            log.info("[DETAIL-CRAWLING placeId={}] 크롤링 성공", placeId);
            PlaceCrawlerResponse response = PlaceCrawlerResponse.builder()
                    .placeId(placeId)
                    .crawlDate(crawlDate)
                    .blogCafeReviewCount(JsonParser.extractBlogCafeReviewCount(detailResponse))
                    .imageReviewCount(JsonParser.extractImageReviewCount(detailResponse))
                    .newsPostCount(JsonParser.extractNewsPostCount(detailResponse))
                    .visitorReviews(JsonParser.parseVisitorReviewItems(allReviewItems))
                    .themes(JsonParser.extractThemes(detailResponse))
                    .menus(JsonParser.extractMenus(detailResponse))
                    .description(htmlResponse.get("description"))
                    .menuList(htmlResponse.get("menus"))
                    .build();
            callbackService.sendCallback(response, callbackURL);
        } else {
            log.error("[DETAIL-CRAWLING placeId={}] 크롤링 실패", placeId);
            callbackService.sendCallback(
                    PlaceCrawlerResponse.builder()
                            .placeId(placeId)
                            .crawlDate(crawlDate)
                            .build(),
                    callbackURL
            );
        }
    }


    public PlaceCrawlerResponse crawlDetail(Long placeId, String cidList) {
        log.info("[DETAIL-CRAWLING-TRIGGERED] placeId={}", placeId);
        JsonNode detailResponse = naverPlaceApiClient.crawlingPlaceDetail(placeId);
        List<JsonNode> allReviewItems = naverPlaceApiClient.crawlingAllVisitorReviews(placeId, cidList);
        Map<String, String> htmlResponse = naverPlaceHtmlClient.crawlingHtml(placeId);

        if (detailResponse != null) {
            return PlaceCrawlerResponse.builder()
                    .placeId(placeId)
                    .crawlDate(LocalDate.now())
                    .blogCafeReviewCount(JsonParser.extractBlogCafeReviewCount(detailResponse))
                    .imageReviewCount(JsonParser.extractImageReviewCount(detailResponse))
                    .newsPostCount(JsonParser.extractNewsPostCount(detailResponse))
                    .visitorReviews(JsonParser.parseVisitorReviewItems(allReviewItems))
                    .themes(JsonParser.extractThemes(detailResponse))
                    .menus(JsonParser.extractMenus(detailResponse))
                    .description(htmlResponse.get("description"))
                    .menuList(htmlResponse.get("menus"))
                    .build();
        }
        return null;
    }

}
