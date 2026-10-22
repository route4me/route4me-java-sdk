package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.services.tracking.TrackingHistory;
import lombok.Data;

import java.util.List;

/** An optimization, as the V4 optimization API returns it; {@link Route} and {@link Optimization} extend it. */
@Data
public class DataObject {

    /** The addresses (stops) of the optimization or route ({@code addresses}). */
    @SerializedName("addresses")
    private List<Address> addresses;
    /** The GPS positions recorded on the route ({@code tracking_history}). */
    @SerializedName("tracking_history")
    private List<TrackingHistory> trackingHistory;
    /** The route parameters of the optimization ({@code parameters}). */
    @SerializedName("parameters")
    private Parameters parameters;
    /** The ID of the optimization ({@code optimization_problem_id}). */
    @SerializedName("optimization_problem_id")
    private String optimizationProblemId;
    /** The state of the optimization; see {@code Constants.OptimizationState} ({@code state}). */
    @SerializedName("state")
    private Number state;
    /**
     * If true it means the solution was not returned (it is being computed in the background) ({@code
     * sent_to_background}).
     */
    @SerializedName("sent_to_background")
    private Boolean sentToBackground;
    /** The URLs of the optimization and its view ({@code links}). */
    @SerializedName("links")
    private Links links;
    /** The name of the driver ({@code driver_alias}). */
    @SerializedName("driver_alias")
    private String driverAlias;
    /** Whether new addresses go where they fit best in the route, rather than at the end ({@code optimal_position}). */
    @SerializedName("optimal_position")
    private Boolean optimalPosition;

}
