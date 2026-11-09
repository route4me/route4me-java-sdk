package com.route4me.sdk.services.optimizationprofile;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The body of {@link OptimizationProfileManager#assignOptimizationProfile(String, String)}. */
@Data
public class OptimizationProfileAssignRequest {
    /** The ID of the group of orders ({@code group_id}). */
    @SerializedName("group_id")
    private String groupId;
    /** The ID of the optimization profile ({@code optimization_profile_id}). */
    @SerializedName("optimization_profile_id")
    private String optimizationProfileId;
}
