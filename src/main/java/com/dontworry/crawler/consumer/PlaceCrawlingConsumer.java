package com.dontworry.crawler.consumer;

import com.dontworry.crawler.config.RabbitMqConfig;
import com.dontworry.crawler.dto.PlaceCrawlerRequest;
import com.dontworry.crawler.service.CrawlingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlaceCrawlingConsumer {

    private final CrawlingService crawlingService;

    @Transactional
    @RabbitListener(queues = RabbitMqConfig.PLACE_QUEUE)
    public void consume(PlaceCrawlerRequest request) {
        crawlingService.processCrawling(request.placeId(), request.crawlDate(), request.cidList(), request.callbackUrl());
    }
}
