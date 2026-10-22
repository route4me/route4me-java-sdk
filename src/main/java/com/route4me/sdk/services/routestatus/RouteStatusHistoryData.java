package com.route4me.sdk.services.routestatus;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class RouteStatusHistoryData {
    @SerializedName("route_status_history_id")
    private String routeStatusHistoryId;

    @SerializedName("route_id")
    private String routeId;

    @SerializedName("member_id")
    private Long memberId;

    @SerializedName("status")
    private String status;

    @SerializedName("lat")
    private Double latitude;

    @SerializedName("lng")
    private Double longitude;

    @SerializedName("timestamp")
    private Long timestamp;

    @SerializedName("rollback_timestamp")
    private Long rollbackTimestamp;
}
