package com.route4me.sdk.examples.driverreview;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.driverreview.DriverReviewManager;
import com.route4me.sdk.services.driverreview.DriverReviewSettings;

/** Prints the settings of the driver review page with {@link DriverReviewManager#getDriverReviewSettingsByMember}. */
public class GetDriverReviewSettings {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        DriverReviewManager manager = new DriverReviewManager(apiKey);
        try {
            DriverReviewSettings settings = manager.getDriverReviewSettingsByMember().getData();
            System.out.println("Question: " + settings.getQuestion());
            System.out.println("Logo: " + settings.getLogoPath());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
