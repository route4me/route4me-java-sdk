package com.route4me.sdk.examples.driverreview;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.driverreview.DriverReview;
import com.route4me.sdk.services.driverreview.DriverReviewManager;
import com.route4me.sdk.services.driverreview.DriverReviewResponse;

/** Adds a driver review with {@link DriverReviewManager#createDriverReview}. */
public class CreateDriverReview {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        DriverReviewManager manager = new DriverReviewManager(apiKey);
        try {
            DriverReview review = new DriverReview();
            review.setTrackingNumber("TRACKING_NUMBER_HERE");
            review.setReview("Excellent delivery service");
            review.setRating(4.0);
            DriverReviewResponse response = manager.createDriverReview(review);
            System.out.println("Created review: " + response.getData().getRatingId());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
