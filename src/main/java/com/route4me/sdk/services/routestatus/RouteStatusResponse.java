package com.route4me.sdk.services.routestatus;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The status of a route, with the time of each change, as the methods of {@link RouteStatusManager} return it. */
@Data
public class RouteStatusResponse {
    /** The ID of the route ({@code route_id}). */
    @SerializedName("route_id")
    private String routeId;

    /** The current route status, such as {@code planned} or {@code started} ({@code status}). */
    @SerializedName("status")
    private String status;

    /** The latitude of the last status change ({@code lat}). */
    @SerializedName("lat")
    private Double latitude;

    /** The longitude of the last status change ({@code lng}). */
    @SerializedName("lng")
    private Double longitude;

    /** Planned at (Unix timestamp in seconds) ({@code planned_at}). */
    @SerializedName("planned_at")
    private Long plannedAt;

    /** Started at (Unix timestamp in seconds) ({@code started_at}). */
    @SerializedName("started_at")
    private Long startedAt;

    /** Paused at (Unix timestamp in seconds) ({@code paused_at}). */
    @SerializedName("paused_at")
    private Long pausedAt;

    /** Completed at (Unix timestamp in seconds) ({@code completed_at}). */
    @SerializedName("completed_at")
    private Long completedAt;
}
