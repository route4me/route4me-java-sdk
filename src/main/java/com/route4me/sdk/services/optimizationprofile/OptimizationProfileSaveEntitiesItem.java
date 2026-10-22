package com.route4me.sdk.services.optimizationprofile;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class OptimizationProfileSaveEntitiesItem {
    @SerializedName("guid")
    private String guid;

    @SerializedName("id")
    private String id;

    @SerializedName("parts")
    private List<OptimizationProfileSaveEntitiesItemPart> parts;
}
