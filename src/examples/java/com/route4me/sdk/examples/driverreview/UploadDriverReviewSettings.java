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
 * {@link DriverReviewManager#uploadDriverReviewSettings}. The logo is sent Base64-encoded. It replaces the driver
 * review settings that the account's customers see: set {@code LOGO_FILE} to the path of a PNG logo and run it on a
 * test account.
 */
public class UploadDriverReviewSettings {
    public static void main(String[] args) {
        String logo = System.getenv("LOGO_FILE");
        if (logo == null || logo.isEmpty()) {
            System.err.println("Set LOGO_FILE to the path of a PNG logo; this example replaces the driver review"
                    + " settings of the account (use a test account).");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        DriverReviewManager manager = new DriverReviewManager(apiKey);
        try {
            DriverReviewSettingsRequest settings = new DriverReviewSettingsRequest();
            settings.setLogoPicture(Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(logo))));
            settings.setQuestion("How was your delivery?");
            settings.setLogoBackgroundColor("#FFFFFF");
            DriverReviewSettingsResponse stored = manager.uploadDriverReviewSettings(settings);
            if (stored != null && stored.getData() != null) {
                System.out.println("Logo stored at " + stored.getData().getLogoPath());
            } else {
                System.out.println("Settings stored: " + stored);
            }
        } catch (APIException | IOException e) {
            e.printStackTrace();
        }
    }
}
