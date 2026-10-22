package com.route4me.sdk.services.optimizationprofile;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The answer of {@link OptimizationProfileManager#getOptimizationProfiles()}. */
@Data
public class OptimizationProfilesResponse {
    /** Optimization profiles ({@code items}). */
    @SerializedName("items")
    private List<OptimizationProfile> items;
}
