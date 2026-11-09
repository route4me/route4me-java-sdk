package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The answer of {@link RouteDestinationsManager#getDestinationsByOrder(String)}. */
@Data
public class RouteDestinationsByOrderResponse {
    /** Array of route destinations linked to the specified order ({@code items}). */
    @SerializedName("items")
    private List<RouteDestinationResource> items;
}
