package com.route4me.sdk.services.optimizationprofile;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The body of {@link OptimizationProfileManager#deleteEntities(OptimizationProfileDeleteEntitiesRequest)}. */
@Data
public class OptimizationProfileDeleteEntitiesRequest {
    /** Items ({@code items}). */
    @SerializedName("items")
    private List<OptimizationProfileDeleteEntitiesRequestItem> items;
}
