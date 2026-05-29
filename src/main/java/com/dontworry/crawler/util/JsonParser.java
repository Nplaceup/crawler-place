package com.dontworry.crawler.util;

import com.dontworry.crawler.dto.ReviewAnalysisItem;
import com.dontworry.crawler.dto.VisitorReviewItem;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    public static int parseInt(String raw) {
        if (raw == null) return 0;

        String cleaned = raw.replaceAll("[^0-9]", "");

        if (cleaned.isEmpty()) return 0;

        try {
            return Integer.parseInt(cleaned);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static Long parseLong(String raw) {
        if (raw == null) return 0L;

        String cleaned = raw.replaceAll("[^0-9]", "");

        if (cleaned.isEmpty()) return 0L;

        try {
            return Long.parseLong(cleaned);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    public static String parsePhone(JsonNode item) {
        String virtual = item.path("virtualPhone").asText(null);
        if (virtual != null && !virtual.isEmpty()) {
            return virtual;
        }

        String phone = item.path("phone").asText(null);
        if (phone != null && !phone.isEmpty()) {
            return phone;
        }

        return null;
    }

    public static Integer extractBlogCafeReviewCount(JsonNode detailResponse) {
        Integer blogCount = 0;

        for (JsonNode node : detailResponse) {
            JsonNode dataNode = node.path("data");
            if (dataNode.isMissingNode()) {
                continue;
            }

            JsonNode fsasNode = dataNode.path("fsasReviews");
            if (!fsasNode.isMissingNode()) {
                String totalText = fsasNode.path("total").asText();
                blogCount = JsonParser.parseInt(totalText);
                break;
            }
        }

        return blogCount;
    }

    public static Integer extractImageReviewCount(JsonNode detailResponse) {
        Integer imageReviewCount = 0;

        for (JsonNode node : detailResponse) {
            JsonNode reviewNode = node.path("data")
                    .path("visitorReviewStats");
            if (reviewNode.isMissingNode()) {
                continue;
            }

            JsonNode reviewStatsNode = reviewNode.path("review");
            if (!reviewStatsNode.isMissingNode()) {
                String totalText = reviewStatsNode.path("imageReviewCount").asText();
                imageReviewCount = JsonParser.parseInt(totalText);
                break;
            }
        }

        return imageReviewCount;
    }

    public static Integer extractNewsPostCount(JsonNode detailResponse) {
        Integer newsPostCount = 0;

        for (JsonNode node : detailResponse) {
            JsonNode feedNode = node.path("data").path("feeds");
            if (feedNode.isMissingNode()) {
                continue;
            }

            JsonNode feedsNode = feedNode.path("feeds");
            if (!feedsNode.isMissingNode()) {
                newsPostCount = feedsNode.size();
                break;
            }
        }

        return newsPostCount;
    }

    public static List<ReviewAnalysisItem> extractThemes(JsonNode detailResponse) {
        return extractAnalysisList(detailResponse, "themes", "label");
    }

    public static List<ReviewAnalysisItem> extractMenus(JsonNode detailResponse) {
        return extractAnalysisList(detailResponse, "menus", "label");
    }

    private static List<ReviewAnalysisItem> extractAnalysisList(
            JsonNode detailResponse, String key, String labelKey) {
        for (JsonNode node : detailResponse) {
            JsonNode items = node.path("data")
                    .path("visitorReviewStats")
                    .path("analysis")
                    .path(key);
            if (!items.isMissingNode() && items.isArray()) {
                List<ReviewAnalysisItem> result = new ArrayList<>();
                for (JsonNode item : items) {
                    String label   = item.path(labelKey).asText(null);
                    Integer count  = item.path("count").asInt(0);
                    if (label != null) {
                        result.add(ReviewAnalysisItem.builder()
                                .label(label).count(count).build());
                    }
                }
                return result;
            }
        }
        return List.of();
    }

    public static List<VisitorReviewItem> parseVisitorReviewItems(List<JsonNode> items) {
        List<VisitorReviewItem> result = new ArrayList<>();
        for (JsonNode item : items) {
            String id      = item.path("id").asText(null);
            String body    = item.path("body").asText(null);
            String visited = item.path("visited").asText(null);
            if (body != null) {
                result.add(VisitorReviewItem.builder()
                        .naverReviewId(id)
                        .body(body)
                        .visited(visited)
                        .build());
            }
        }
        return result;
    }

}
