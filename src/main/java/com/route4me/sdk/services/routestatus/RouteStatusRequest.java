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

    /** The latitude where the status changes; {@code null} leaves it out ({@code lat}). */
    @SerializedName("lat")
    private Double latitude;

    /** The longitude where the status changes; {@code null} leaves it out ({@code lng}). */
    @SerializedName("lng")
    private Double longitude;

    /** Why the status changes, in words ({@code reason}). */
    @SerializedName("reason")
    private String reason;

    /** The code of the reason for the change ({@code reason_code}). */
    @SerializedName("reason_code")
    private String reasonCode;

    /** Event timestamp (Unix timestamp in seconds) ({@code event_timestamp}). */
    @SerializedName("event_timestamp")
    private Long eventTimestamp;
}
