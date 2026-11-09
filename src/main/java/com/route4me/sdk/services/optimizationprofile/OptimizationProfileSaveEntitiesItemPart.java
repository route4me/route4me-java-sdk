package com.route4me.sdk.services.optimizationprofile;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Map;

/** One part of an {@link OptimizationProfileSaveEntitiesItem}. */
@Data
public class OptimizationProfileSaveEntitiesItemPart {
    /** Guid ({@code guid}). */
    @SerializedName("guid")
    private String guid;

    /** Data ({@code data}). */
    @SerializedName("data")
    private Map<String, Object> data;

    /** Config ({@code config}). */
    @SerializedName("config")
    private Map<String, Object> config;
}
