package com.route4me.sdk.services.routing;

import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * The query of the route methods of {@link RoutingManager}: the route ID or the period, the paging and what
 * to include.
 */
@Data
@Accessors(chain = true)
public class RoutesRequest {
    /** Route Identifier ({@code route_id}). */
    @QueryParameter("route_id")
    private String id;
    /** Pass True to return directions and the route path ({@code directions}). */
    @QueryParameter("directions")
    private Boolean directions;
    /** Output route tracking data in response ({@code device_tracking_history}). */
    @QueryParameter("device_tracking_history")
    private Boolean deviceTrackingHistory;
    /**
     * The number of existing routes that should be returned per response when looking at a list of all the routes
     * ({@code limit}).
     */
    @QueryParameter("limit")
    private Integer limit;
    /**
     * The page number for route listing pagination. Increment the offset by the limit number to move to the next page
     * ({@code offset}).
     */
    @QueryParameter("offset")
    private Integer offset;
    /**
     * Output addresses and directions in the original optimization request sequence. This is to allow us to compare
     * routes before &amp; after optimization ({@code original}).
     */
    @QueryParameter("original")
    private Boolean original;
    /**
     * Output route and stop-specific notes. The notes will have timestamps, note types, and geospatial information if
     * available ({@code notes}).
     */
    @QueryParameter("notes")
    private Boolean notes;
    /**
     * The path between the stops to return with each address: {@code Points} for the path points, {@code None} for
     * none ({@code route_path_output}).
     */
    @QueryParameter("route_path_output")
    private String routePathOutput;
    /**
     * With {@code route_path_output=Points}, {@code true} to return the path points compressed
     * ({@code compress_path_points}).
     */
    @QueryParameter("compress_path_points")
    private Boolean compressPathPoints;
    /** Search query ({@code query}). */
    @QueryParameter("query")
    private String query;
    /**
     * Updating a route supports the reoptimize=1 parameter, which reoptimizes only that route. Also supports the
     * parameters from GET ({@code reoptimize}).
     */
    @QueryParameter("reoptimize")
    private Boolean reoptimize;
    /**
     * By sending recompute_directions=1 we request that the route directions be recomputed (note that this does happen
     * automatically if certain properties of the route are updated, such as stop sequence_no changes or round-tripness)
     * ({@code recompute_directions}).
     */
    @QueryParameter("recompute_directions")
    private Boolean recomputeDirections;
    /** Start date ({@code start_date}). */
    @QueryParameter("start_date")
    private String startDate;
    /** End date ({@code end_date}). */
    @QueryParameter("end_date")
    private String endDate;
}
