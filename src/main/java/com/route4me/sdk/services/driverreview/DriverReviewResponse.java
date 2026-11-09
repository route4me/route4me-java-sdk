package com.route4me.sdk.services.driverreview;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of the single-review methods of {@link DriverReviewManager}: {@code {"data": {...}}}. */
@Data
public class DriverReviewResponse {
    /** The review ({@code data}). */
    @SerializedName("data")
    private DriverReview data;
}
