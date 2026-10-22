package com.route4me.sdk.services.optimizationprofile;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Map;

@Data
public class OptimizationProfileSaveEntitiesItemPart {
    @SerializedName("guid")
    private String guid;

    @SerializedName("data")
    private Map<String, Object> data;

    @SerializedName("config")
    private Map<String, Object> config;
}
