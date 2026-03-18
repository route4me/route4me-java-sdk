package com.route4me.sdk.services.optimizationprofile;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class OptimizationProfileSaveEntities {
    @SerializedName("items")
    private List<OptimizationProfileSaveEntitiesItem> items;
}
