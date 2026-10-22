package com.route4me.sdk.examples.driverreview;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.driverreview.DriverReview;
import com.route4me.sdk.services.driverreview.DriverReviewManager;
import com.route4me.sdk.services.driverreview.DriverReviewResponse;

/** Changes the rating of a driver review with {@link DriverReviewManager#patchDriverReview}. */
public class PatchDriverReview {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        DriverReviewManager manager = new DriverReviewManager(apiKey);
        try {
            DriverReview change = new DriverReview();
            change.setRating(5.0);
            DriverReviewResponse response = manager.patchDriverReview("RATING_ID_HERE", change);
            System.out.println("New rating: " + response.getData().getRating());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
