package com.dontworry.crawler.graphql;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PayloadFactory {

    // cursor 기반 페이지네이션용 (crawlingAllVisitorReviews에서 사용)
    public Map<String, Object> visitorReviewsPayload(String placeId, int size, List<String> cidList, String after) {
        Map<String, Object> input = new HashMap<>();
        input.put("businessId", placeId);
        input.put("businessType", "place");
        input.put("size", size);
        input.put("item", "0");
        input.put("includeContent", true);
        input.put("isPhotoUsed", false);
        input.put("includeReceiptPhotos", true);
        input.put("cidList", cidList);

        if (after != null) {
            input.put("after", after);  // cursor → after
        }

        return Map.of(
                "operationName", "getVisitorReviews",
                "variables", Map.of("input", input),
                "query", QueryProvider.getVisitorReviews
        );
    }

    public Map<String, Object> visitorReviewStatsPayload(String placeId) {
        return Map.of(
                "operationName", "getVisitorReviewStats",
                "variables", Map.of(
                        "id", placeId,
                        "itemId", "0",
                        "businessType", "place"
                ),
                "query", QueryProvider.getVisitorReviewStats
        );
    }

    public Map<String, Object> feedsPayload(String placeId) {
        return Map.of(
                "operationName", "getFeeds",
                "variables", Map.of(
                        "businessId", placeId,
                        "feedOffset", 0,
                        "type", "all"
                ),
                "query", QueryProvider.getFeeds
        );
    }

    public Map<String, Object> fsasPayload(String placeId) {
        Map<String, Object> input = Map.of(
                "businessId", placeId,
                "businessType", "place",
                "display", 10
        );

        return Map.of(
            "operationName", "getFsasReviews",
            "variables", Map.of("input", input),
            "query", QueryProvider.getFsasReviews
        );
    }

    // detailPayload는 stats/feeds/fsas 전용 (visitorReviews 제외)
    public List<Map<String, Object>> detailPayload(String placeId) {
        return List.of(
                visitorReviewStatsPayload(placeId),
                feedsPayload(placeId),
                fsasPayload(placeId)
        );
    }

}
