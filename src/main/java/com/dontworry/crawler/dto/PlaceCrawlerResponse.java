package com.dontworry.crawler.dto;

import lombok.Builder;
import java.time.LocalDate;
import java.util.List;

@Builder
public record PlaceCrawlerResponse(
        Long placeId,
        LocalDate crawlDate,
        Integer blogCafeReviewCount,

        Integer imageReviewCount,
        Integer newsPostCount,

        // 방문자 리뷰 본문
        List<VisitorReviewItem> visitorReviews,

        // 테마/메뉴 분석
        List<ReviewAnalysisItem> themes,
        List<ReviewAnalysisItem> menus,

        String description,
        String menuList
) {}