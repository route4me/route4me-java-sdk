package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The answer of {@link LocationManager#getLocationsList(LocationCombinedRequest)}. */
@Data
public class LocationListResource {
    /** Array of location items ({@code items}). */
    @SerializedName("items")
    private List<LocationListItem> items;

    /** Total number of items matching the query ({@code total_items_count}). */
    @SerializedName("total_items_count")
    private Integer totalItemsCount;
}
