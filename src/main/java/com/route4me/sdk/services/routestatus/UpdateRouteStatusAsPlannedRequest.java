package com.route4me.sdk.services.routestatus;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The body of {@link RouteStatusManager#updateRouteStatusAsPlanned(String[])}. */
@Data
public class UpdateRouteStatusAsPlannedRequest {
    /** The IDs of the routes to mark as planned ({@code route_ids}). */
    @SerializedName("route_ids")
    private String[] routeIds;
}
