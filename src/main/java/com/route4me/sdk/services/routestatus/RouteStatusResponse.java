package com.route4me.sdk.services.routestatus;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class RouteStatusResponse {
    @SerializedName("route_id")
    private String routeId;

    @SerializedName("status")
    private String status;

    @SerializedName("lat")
    private Double latitude;

    @SerializedName("lng")
    private Double longitude;

    @SerializedName("planned_at")
    private Long plannedAt;

    @SerializedName("started_at")
    private Long startedAt;

    @SerializedName("paused_at")
    private Long pausedAt;

    @SerializedName("completed_at")
    private Long completedAt;
}
