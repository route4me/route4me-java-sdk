package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class LocationListResource {
    @SerializedName("items")
    private List<LocationListItem> items;

    @SerializedName("total_items_count")
    private Integer totalItemsCount;
}
