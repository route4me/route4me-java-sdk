package com.route4me.sdk.examples.driverreview;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.driverreview.DriverReviewManager;
import com.route4me.sdk.services.driverreview.DriverReviewSettingsRequest;
import com.route4me.sdk.services.driverreview.DriverReviewSettingsResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * Uploads the logo, the question and the logo color of the driver review page with
 * {@link DriverReviewManager#uploadDriverReviewSettings}. The logo is read from {@code logo.png} and sent
 * Base64-encoded.
 */
public class UploadDriverReviewSettings {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        DriverReviewManager manager = new DriverReviewManager(apiKey);
        try {
            DriverReviewSettingsRequest settings = new DriverReviewSettingsRequest();
            settings.setLogoPicture(Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get("logo.png"))));
            settings.setQuestion("How was your delivery?");
            settings.setLogoBackgroundColor("#FFFFFF");
            DriverReviewSettingsResponse stored = manager.uploadDriverReviewSettings(settings);
            System.out.println("Logo stored at " + stored.getData().getLogoPath());
        } catch (APIException | IOException e) {
            e.printStackTrace();
        }
    }
}
