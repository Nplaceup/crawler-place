package com.dontworry.crawler.dto;

import lombok.Builder;

@Builder
public record ReviewAnalysisItem(
        String label,
        Integer count
) {}
