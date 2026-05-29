package com.dontworry.crawler.graphql;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverPlaceApiClient {

    private static final int MAX_429_RETRIES = 3;
    private static final int DISPLAY = 20;
    private static final int MAX_REVIEW_COUNT = 20;

    private final HeaderProvider headerProvider;
    private final PayloadFactory payloadFactory;
    private final RestTemplate restTemplate;

    private static final String BASE_URL = "https://pcmap-api.place.naver.com/graphql";

    public JsonNode crawlingPlaceDetail(Long placeId) {
        int retry = 0;

        while (true) {
            try {
                HttpHeaders headers = headerProvider.detailHeaders(placeId.toString());
                List<Map<String, Object>> detailPayload = payloadFactory.detailPayload(placeId.toString());
                HttpEntity<List<Map<String, Object>>> request = new HttpEntity<>(detailPayload, headers);

                ResponseEntity<JsonNode> response = restTemplate.exchange(
                        BASE_URL,
                        HttpMethod.POST,
                        request,
                        JsonNode.class
                );

                randomBackOff(0);
                log.info("[DETAIL] 조회 성공 placeId={}", placeId);
                return response.getBody();
            } catch (HttpStatusCodeException e) {
                if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                    retry++;
                    if (retry > MAX_429_RETRIES) {
                        log.error("[DETAIL] 429 재시도 초과 placeId={}", placeId);
                        return null;
                    }
                    long backoffMillis = calculate429BackoffMillis(retry);
                    log.warn("[DETAIL] 429 재시도 {}/{}회 placeId={}", retry, MAX_429_RETRIES, placeId);
                    sleep(backoffMillis);
                    continue;
                }
                log.error("[DETAIL] 조회 실패 placeId={}", placeId, e);
                return null;
            } catch (Exception e) {
                log.error("[DETAIL] 조회 실패 placeId={}", placeId, e);
                return null;
            }
        }
    }

    public List<JsonNode> crawlingAllVisitorReviews(Long placeId, String cidList) {
        List<JsonNode> allItems = new ArrayList<>();
        Set<String> seenIds = new HashSet<>();
        String after = null;
        int page = 1;
        int neededPages = (int) Math.ceil((double) MAX_REVIEW_COUNT / DISPLAY);

        while (true) {
            if (allItems.size() >= MAX_REVIEW_COUNT) {
                break;
            }

            if (page > neededPages) {
                break;
            }

            int retry = 0;
            boolean success = false;

            while (retry <= MAX_429_RETRIES) {
                try {
                    HttpHeaders headers = headerProvider.detailHeaders(placeId.toString());
                    Map<String, Object> payload =
                            payloadFactory.visitorReviewsPayload(placeId.toString(), DISPLAY, parseCidListFromString(cidList), after);

                    HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
                    ResponseEntity<JsonNode> response = restTemplate.exchange(
                            BASE_URL, HttpMethod.POST, request, JsonNode.class
                    );

                    JsonNode visitorReviews = response.getBody()
                            .path("data").path("visitorReviews");

                    JsonNode items = visitorReviews.path("items");
                    if (!items.isArray() || items.isEmpty()) {
                        log.info("[VISITOR-REVIEW] 수집 완료 placeId={}, 총 {}건", placeId, allItems.size());
                        return allItems;
                    }

                    for (JsonNode item : items) {
                        if (allItems.size() >= MAX_REVIEW_COUNT) break;
                        String id = item.path("id").asText(null);
                        if (id != null && seenIds.add(id)) {
                            allItems.add(item);
                        }
                    }

                    String nextAfter = items.get(items.size() - 1).path("cursor").asText(null);

                    if (nextAfter == null || nextAfter.equals(after)) {
                        log.info("[VISITOR-REVIEW] 수집 완료 placeId={}, 총 {}건", placeId, allItems.size());
                        return allItems;
                    }

                    after = nextAfter;
                    page++;
                    randomBackOff(1);
                    success = true;
                    break;

                } catch (HttpStatusCodeException e) {
                    if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                        retry++;
                        long backoff = calculate429BackoffMillis(retry);
                        log.warn("[VISITOR-REVIEW] 429 재시도 {}/{}회 placeId={}", retry, MAX_429_RETRIES, placeId);
                        sleep(backoff);
                    } else {
                        log.error("[VISITOR-REVIEW] 수집 실패 placeId={}", placeId, e);
                        return allItems;
                    }
                } catch (Exception e) {
                    log.error("[VISITOR-REVIEW] 수집 실패 placeId={}", placeId, e);
                    return allItems;
                }
            }

            if (!success) {
                log.error("[VISITOR-REVIEW] 429 재시도 초과 placeId={}", placeId);
                break;
            }
        }

        log.info("[VISITOR-REVIEW] 수집 완료 placeId={}, 총 {}건", placeId, allItems.size());
        return allItems;
    }

    private long calculate429BackoffMillis(int retry) {
        return retry * 3_000L;
    }

    private void randomBackOff(int attempt) {
        double randomSeconds = 0.5 + Math.random() * 0.5 * attempt;
        long millis = (long) (randomSeconds * 1000);

        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error("InterruptedException", e);
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("InterruptedException", e);
        }
    }

    public List<String> parseCidListFromString(String cidStr) {
        if (cidStr == null || cidStr.isBlank()) {
            return new ArrayList<>();
        }
        return Arrays.asList(cidStr.split("/"));
    }
}