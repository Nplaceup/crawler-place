package com.dontworry.crawler.graphql;

import org.springframework.stereotype.Component;

@Component
public class QueryProvider {

    public static String getVisitorReviews = """
            query getVisitorReviews($input: VisitorReviewsInput) {
              visitorReviews(input: $input) {
                total
                items { id cursor body originType status visitCount viewCount visited created }
              }
            }
            """;

    public static String getVisitorReviewStats = """
            query getVisitorReviewStats($id: String, $itemId: String, $businessType: String = "place") {
              visitorReviewStats(input: {businessId: $id, itemId: $itemId, businessType: $businessType}) {
                review { avgRating imageReviewCount authorCount }
                analysis {
                  themes { code label count }
                  menus { label count }
                  votedKeyword {
                    totalCount
                    reviewCount
                    userCount
                    details { category code displayName count }
                  }
                }
                visitorReviewsTotal
                ratingReviewsTotal
              }
            }
            """;

    public static String getFeeds = """
            query getFeeds($businessId: String!, $blogId: String, $blogCategoryNo: String, $type: String, $feedOffset: Int, $blogOffset: Int) {
              feeds(
                businessId: $businessId
                blogId: $blogId
                blogCategoryNo: $blogCategoryNo
                type: $type
                feedOffset: $feedOffset
                blogOffset: $blogOffset
              ) {
                feeds { id __typename }
                hasMore
                __typename
              }
            }
            """;

    public static String getFsasReviews = """
            query getFsasReviews($input: FsasReviewsInput) {
              fsasReviews(input: $input) {
                total
                items { title date }
              }
            }
            """;
}
