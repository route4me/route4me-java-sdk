package com.route4me.sdk.services.driverreview;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class DriverReviewResponse {
    @SerializedName("data")
    private DriverReview data;
}
