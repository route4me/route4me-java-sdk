package com.route4me.sdk.services.routing;

import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * The query flags of {@link RoutingManager#updateRoute(Route, RouteUpdateParameters)}
 * ({@code PUT https://api.route4me.com/api.v4/route.php}): whether to re-optimize the route, or recompute its
 * directions or its actual distance, after the update. Every {@code null} field is left out of the query; a flag is
 * sent as {@code 1} or {@code 0}.
 */
@Data
@Accessors(chain = true)
public class RouteUpdateParameters {
    /** {@code true} to re-optimize the route after the update ({@code reoptimize}). */
    @QueryParameter("reoptimize")
    private Boolean reoptimize;

    /**
     * With {@code reoptimize}, {@code true} to re-optimize only the stops not visited yet ({@code remaining}).
     */
    @QueryParameter("remaining")
    private Boolean remaining;

    /**
     * What orders the remaining stops when the route is re-optimized: {@code priority}, {@code customer_importance}
     * or {@code time_window} ({@code prioritize_by}).
     */
    @QueryParameter("prioritize_by")
    private String prioritizeBy;

    /**
     * {@code true} to recompute the directions of the route without changing its sequence
     * ({@code recompute_directions}).
     */
    @QueryParameter("recompute_directions")
    private Boolean recomputeDirections;

    /** {@code true} to recompute the actual distance of the route ({@code recompute_actual_distance}). */
    @QueryParameter("recompute_actual_distance")
    private Boolean recomputeActualDistance;
}
