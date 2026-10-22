package com.route4me.sdk.services.driverreview;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * The answer of {@link DriverReviewManager#getDriverReviewSettingsByMember()} and
 * {@link DriverReviewManager#uploadDriverReviewSettings(DriverReviewSettingsRequest)}: {@code {"data": {...}}}.
 */
@Data
public class DriverReviewSettingsResponse {
    /** The settings ({@code data}). */
    @SerializedName("data")
    private DriverReviewSettings data;
}
