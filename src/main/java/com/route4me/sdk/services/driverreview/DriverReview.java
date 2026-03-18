package com.route4me.sdk.services.driverreview;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class DriverReview {
    @SerializedName("rating_id")
    private String ratingId;

    @SerializedName("tracking_number")
    private String trackingNumber;

    @SerializedName("review")
    private String review;

    @SerializedName("rating")
    private Double rating;

    @SerializedName("added_at")
    private String addedAt;
}
