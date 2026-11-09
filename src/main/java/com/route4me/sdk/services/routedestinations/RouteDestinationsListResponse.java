package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The answer of {@link RouteDestinationsManager#getDestinationsList(GetDestinationsRequest)}. */
@Data
public class RouteDestinationsListResponse {
    /** Array of destination list items matching the request filters ({@code items}). */
    @SerializedName("items")
    private List<RouteDestinationListResource> items;

    /** The number of the returned page ({@code current_page_index}). */
    @SerializedName("current_page_index")
    private Integer currentPageIndex;

    /** The number of destinations that match the filters, on all pages ({@code total_items_count}). */
    @SerializedName("total_items_count")
    private Integer totalItemsCount;
}
