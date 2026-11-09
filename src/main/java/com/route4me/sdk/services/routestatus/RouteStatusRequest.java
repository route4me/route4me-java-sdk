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

    /**
     * The {@code status_reason_id} (32-character hex) of a cancel reason from the account's reasons list ({@code
     * reason}). Only with the status {@code canceled}, and not together with {@link #getReasonCode() reasonCode}.
     */
    @SerializedName("reason")
    private String reason;

    /**
     * The 3-character code of a cancel reason from the account's reasons list, instead of {@link #getReason() reason}
     * ({@code reason_code}). Only with the status {@code canceled}, and not together with {@code reason}.
     */
    @SerializedName("reason_code")
    private String reasonCode;

    /** Event timestamp (Unix timestamp in seconds) ({@code event_timestamp}). */
    @SerializedName("event_timestamp")
    private Long eventTimestamp;
}
