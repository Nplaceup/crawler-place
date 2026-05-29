package com.dontworry.crawler;

import com.dontworry.crawler.graphql.NaverPlaceApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class DetailCrawlerTest {

    @Autowired
    private NaverPlaceApiClient naverPlaceApiClient;

    @Test
    void crawlDetailAndPrint() {

        Long placeId = 2036618312L;

        JsonNode response = naverPlaceApiClient.crawlingPlaceDetail(placeId);

        System.out.println("===== 크롤링 결과 =====");
        System.out.println(response.toString());
        System.out.println("=======================");
    }
}