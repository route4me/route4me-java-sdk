package com.route4me.sdk.services.driverreview;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

/**
 * The settings to upload, the body of
 * {@link DriverReviewManager#uploadDriverReviewSettings(DriverReviewSettingsRequest)}.
 */
@Data
public class DriverReviewSettingsRequest {
    /**
     * The logo image, Base64-encoded (the spec's {@code format: byte}); see {@link java.util.Base64}
     * ({@code logo_picture}). It can be hundreds of kilobytes, so {@code toString()} leaves it out.
     */
    @ToString.Exclude
    @SerializedName("logo_picture")
    private String logoPicture;

    /** The question the page asks the customer ({@code question}). */
    @SerializedName("question")
    private String question;

    /** The background color of the logo, such as {@code #FFFFFF} ({@code logo_background_color}). */
    @SerializedName("logo_background_color")
    private String logoBackgroundColor;
}
