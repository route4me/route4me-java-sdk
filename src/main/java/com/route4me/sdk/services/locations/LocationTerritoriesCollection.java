package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The answer of {@link LocationManager#getLocationsTerritories(LocationTerritoriesRequest)}. */
@Data
public class LocationTerritoriesCollection {
    /** Array of territory items ({@code items}). */
    @SerializedName("items")
    private List<LocationTerritoryResource> items;

    /** Current page index (0-based) ({@code current_page_index}). */
    @SerializedName("current_page_index")
    private Integer currentPageIndex;

    /** Total number of items across all pages ({@code total_items_count}). */
    @SerializedName("total_items_count")
    private Integer totalItemsCount;
}
