package com.route4me.sdk.services.optimizationprofile;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of {@link OptimizationProfileManager#assignOptimizationProfile(String, String)}. */
@Data
public class OptimizationProfileAssignResponse {
    /** Whether the profile was assigned ({@code status}). */
    @SerializedName("status")
    private Boolean status;
}
