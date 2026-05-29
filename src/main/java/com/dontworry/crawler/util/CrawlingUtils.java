package com.dontworry.crawler.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

public class CrawlingUtils {

    public static Double calculateTotalScore(Integer rank) {
        if (rank == 1) {
            return 100.0;
        }

        Double rand = ThreadLocalRandom.current().nextDouble(0, 0.91);
        Double randFrac = Math.round(rand * 10.0) / 10.0;

        Double base = 100.0 - (rank - 1);
        Double totalScore = base + randFrac;

        if (totalScore > 100.0) {
            totalScore = 100.0;
        }

        return Math.round(totalScore * 10.0) / 10.0;
    }

    public static String makePlaceUrl(String keyword, Long placeId) {
        if (keyword == null || placeId == null) return null;

        String encoded = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        return "https://map.naver.com/p/search/" + encoded + "/place/" + placeId;
    }
}
