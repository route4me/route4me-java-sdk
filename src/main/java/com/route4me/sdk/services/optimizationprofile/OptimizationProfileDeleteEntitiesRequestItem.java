package com.route4me.sdk.services.optimizationprofile;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** One entity to delete, in an {@link OptimizationProfileDeleteEntitiesRequest}. */
@Data
public class OptimizationProfileDeleteEntitiesRequestItem {
    /** The ID of the optimization profile entity to delete ({@code id}). */
    @SerializedName("id")
    private String id;
}
