package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The answer of {@link RoutingManager#deleteRoutes(String...)}. */
@Data
public class RouteDeletedResponse {
    /** If true, the route duplicated successfully ({@code deleted}). */
    @SerializedName("deleted")
    private boolean deleted;

    /** If true, the route duplication process was asynchronous ({@code async}). */
    @SerializedName("async")
    private boolean async;

    /** Removed route(s) (comma-delimited) ({@code route_id}). */
    @SerializedName("route_id")
    private String routeId;

    /** An array of the duplicated route IDs ({@code route_ids}). */
    @SerializedName("route_ids")
    private List<String> addresses;
}
