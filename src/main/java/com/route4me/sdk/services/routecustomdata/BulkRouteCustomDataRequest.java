package com.route4me.sdk.services.routecustomdata;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The body of {@link RouteCustomDataManager#getBulkRouteCustomData(String[])}. */
@Data
public class BulkRouteCustomDataRequest {
    /** The IDs of the routes whose custom data to get ({@code route_ids}). */
    @SerializedName("route_ids")
    private String[] routeIds;
}
