package com.dontworry.crawler.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PlaceCrawlerRequest(
        Long placeId,
        LocalDate crawlDate,
        String cidList,
        String callbackUrl
) {
}
