package com.route4me.sdk.services.routestatus;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The body of {@link RouteStatusManager#updateRouteStatus(String, RouteStatusRequest)}. */
@Data
public class RouteStatusRequest {
    /**
     * The route status: {@code planned}, {@code started}, {@code paused}, {@code resumed}, {@code completed} or {@code
     * canceled} ({@code status}).
     */
    @SerializedName("status")
    private String status;

    /** The latitude where the status changes ({@code lat}). */
    @SerializedName("lat")
    private double latitude;

    /** The longitude where the status changes ({@code lng}). */
    @SerializedName("lng")
    private double longitude;

    /** Event timestamp (Unix timestamp in seconds) ({@code event_timestamp}). */
    @SerializedName("event_timestamp")
    private Long eventTimestamp;
}
