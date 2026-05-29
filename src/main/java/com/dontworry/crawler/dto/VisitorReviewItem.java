package com.dontworry.crawler.dto;

import lombok.Builder;

@Builder
public record VisitorReviewItem(
        String naverReviewId,
        String body,
        String visited
){}