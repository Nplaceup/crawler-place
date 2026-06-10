# place-crawler

네이버 플레이스 **상세 페이지(리뷰)** 를 크롤링합니다.

`ranking-crawler`에서 `blogReviewCount`가 0인 업체를 감지하면 자동으로 트리거됩니다.

---

## 결과값

**`PlaceCallbackResponse`**

| 필드 | 설명 |
|---|---|
| `placeId` | 네이버 플레이스 ID |
| `crawlDate` | 크롤링 날짜 |
| `blogCafeReviewCount` | 블로그/카페 리뷰 수 (`fsasReviews.total`) |

### `visitorReviews[]` — 방문자 리뷰 목록

| 필드 | 설명 |
|---|---|
| `body` | 리뷰 본문 |
| `visited` | 방문 날짜 (예: `"2024-11"`) |

### `themes[]` — 방문자 테마 언급

| 필드 | 설명 |
|---|---|
| `label` | 테마명 (예: `"분위기 좋은"`) |
| `count` | 언급 수 |

### `menus[]` — 방문자 메뉴 언급

| 필드 | 설명 |
|---|---|
| `label` | 메뉴명 (예: `"아메리카노"`) |
| `count` | 언급 수 |

---

## 저장 대상

| 데이터 | 설명 |
|---|---|
| `Rankings` 업데이트 | `blogCafeReviewCount`로 블로그 리뷰 수 보완 |
| `PlaceReviews` 저장 | 방문자 리뷰 본문 목록 |
| `ReviewAnalysis` upsert | 테마 / 메뉴 언급 분석 데이터 |
| `PlaceReviewAnalysis` upsert | 업체별 리뷰 분석 집계 |
