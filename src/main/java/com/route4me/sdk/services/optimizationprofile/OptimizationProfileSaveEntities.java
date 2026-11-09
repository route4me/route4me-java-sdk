package com.route4me.sdk.services.optimizationprofile;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * Optimization profile entities, as {@link OptimizationProfileManager#saveEntities(OptimizationProfileSaveEntities)}
 * takes them and the entity methods return them.
 */
@Data
public class OptimizationProfileSaveEntities {
    /** Items ({@code items}). */
    @SerializedName("items")
    private List<OptimizationProfileSaveEntitiesItem> items;
}
