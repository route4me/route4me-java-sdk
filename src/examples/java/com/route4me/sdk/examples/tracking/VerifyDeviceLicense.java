package com.route4me.sdk.examples.tracking;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.tracking.TrackingManager;

/**
 * Verifies the license of a device with {@link TrackingManager#verifyDeviceLicense}; the API may also add a record of
 * the device to the account. Set {@code DEVICE_ID} to an Android device of a test account.
 */
public class VerifyDeviceLicense {
    public static void main(String[] args) {
        String deviceId = System.getenv("DEVICE_ID");
        if (deviceId == null || deviceId.isEmpty()) {
            System.err.println("Set DEVICE_ID to the ID of an Android device of a test account; this example verifies"
                    + " its license.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        TrackingManager manager = new TrackingManager(apiKey);
        try {
            String answer = manager.verifyDeviceLicense(deviceId, "android_phone", "json");
            System.out.println(answer);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
