package com.route4me.sdk.services.driverreview;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The settings of the page where customers review their driver. */
@Data
public class DriverReviewSettings {
    /** The path of the logo shown on the page ({@code logo_path}). */
    @SerializedName("logo_path")
    private String logoPath;

    /** The question the page asks the customer ({@code question}). */
    @SerializedName("question")
    private String question;

    /** The background color of the logo, such as {@code #FFFFFF} ({@code logo_background_color}). */
    @SerializedName("logo_background_color")
    private String logoBackgroundColor;
}
