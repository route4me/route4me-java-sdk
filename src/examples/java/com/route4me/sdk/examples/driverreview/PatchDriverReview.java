package com.route4me.sdk.examples.driverreview;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.driverreview.DriverReview;
import com.route4me.sdk.services.driverreview.DriverReviewManager;
import com.route4me.sdk.services.driverreview.DriverReviewResponse;

/**
 * Changes the rating of a driver review with {@link DriverReviewManager#patchDriverReview}. Set {@code RATING_ID} to
 * a review of a test account: the example changes its rating to 5.
 */
public class PatchDriverReview {
    public static void main(String[] args) {
        String ratingId = System.getenv("RATING_ID");
        if (ratingId == null || ratingId.isEmpty()) {
            System.err.println("Set RATING_ID to a driver review of a test account; this example changes its rating.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        DriverReviewManager manager = new DriverReviewManager(apiKey);
        try {
            DriverReview change = new DriverReview();
            change.setRating(5.0);
            DriverReviewResponse response = manager.patchDriverReview(ratingId, change);
            System.out.println("New rating: " + response.getData().getRating());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
