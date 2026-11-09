package com.route4me.sdk.examples.driverreview;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.driverreview.DriverReview;
import com.route4me.sdk.services.driverreview.DriverReviewManager;
import com.route4me.sdk.services.driverreview.DriverReviewsResponse;

/** Lists driver reviews with {@link DriverReviewManager#getDriverReviews}. */
public class GetDriverReviews {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        DriverReviewManager manager = new DriverReviewManager(apiKey);
        try {
            DriverReviewsResponse response = manager.getDriverReviews(null, null, null, null, 1, 10);
            if (response.getData() != null) {
                for (DriverReview review : response.getData()) {
                    System.out.println("Review: " + review.getReview() + " Rating: " + review.getRating());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
