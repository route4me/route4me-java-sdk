package com.route4me.sdk.services.optimizationprofile;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class OptimizationProfileDeleteEntitiesRequestItem {
    @SerializedName("id")
    private String id;
}
