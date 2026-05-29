package com.dontworry.crawler.controller;

import com.dontworry.crawler.dto.PlaceCrawlRequest;
import com.dontworry.crawler.dto.PlaceCrawlerRequest;
import com.dontworry.crawler.dto.PlaceCrawlerResponse;
import com.dontworry.crawler.service.CrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/place")
@RequiredArgsConstructor
public class PlaceDetailController {

    private final CrawlingService crawlingService;

    @PostMapping("/crawl")
    public ResponseEntity<PlaceCrawlerResponse> crawl(
            @RequestBody PlaceCrawlRequest request
    ) {
        PlaceCrawlerResponse response = crawlingService.crawlDetail(request.placeId(), request.cidList());
        return ResponseEntity.ok(response);
    }
}