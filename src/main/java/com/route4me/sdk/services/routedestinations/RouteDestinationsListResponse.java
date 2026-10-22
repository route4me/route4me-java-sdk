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
}
