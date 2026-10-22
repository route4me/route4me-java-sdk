package com.route4me.sdk.services.optimizationprofile;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** One entity of an {@link OptimizationProfileSaveEntities}. */
@Data
public class OptimizationProfileSaveEntitiesItem {
    /** The GUID of the optimization profile entity ({@code guid}). */
    @SerializedName("guid")
    private String guid;

    /** The ID of the optimization profile entity ({@code id}). */
    @SerializedName("id")
    private String id;

    /** Parts ({@code parts}). */
    @SerializedName("parts")
    private List<OptimizationProfileSaveEntitiesItemPart> parts;
}
