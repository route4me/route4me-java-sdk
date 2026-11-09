package com.route4me.sdk.services.routestatus;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** One status change of a route. */
@Data
public class RouteStatusHistoryData {
    /** The ID of this history entry ({@code route_status_history_id}). */
    @SerializedName("route_status_history_id")
    private String routeStatusHistoryId;

    /** The ID of the route ({@code route_id}). */
    @SerializedName("route_id")
    private String routeId;

    /** The member ID of the user who changed the status ({@code member_id}). */
    @SerializedName("member_id")
    private Long memberId;

    /** The status the route changed to ({@code status}). */
    @SerializedName("status")
    private String status;

    /** The latitude where the status changed ({@code lat}). */
    @SerializedName("lat")
    private Double latitude;

    /** The longitude where the status changed ({@code lng}). */
    @SerializedName("lng")
    private Double longitude;

    /** Timestamp (Unix timestamp in seconds) ({@code timestamp}). */
    @SerializedName("timestamp")
    private Long timestamp;

    /** Rollback timestamp (Unix timestamp in seconds) ({@code rollback_timestamp}). */
    @SerializedName("rollback_timestamp")
    private Long rollbackTimestamp;
}
