package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class LocationTerritoriesCollection {
    @SerializedName("items")
    private List<LocationTerritoryResource> items;

    @SerializedName("current_page_index")
    private Integer currentPageIndex;

    @SerializedName("total_items_count")
    private Integer totalItemsCount;
}
