package com.route4me.sdk.examples.tracking;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.tracking.TrackingManager;

/** Verifies the license of a device with {@link TrackingManager#verifyDeviceLicense}. */
public class VerifyDeviceLicense {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TrackingManager manager = new TrackingManager(apiKey);
        try {
            String answer = manager.verifyDeviceLicense("EXAMPLE_DEVICE", "android_phone", "json");
            System.out.println(answer);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
