package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class RouteSchedule {
    @SerializedName("member_id")
    private Long memberId;

    @SerializedName("schedule_uid")
    private String scheduleUid;

    @SerializedName("route_id")
    private String routeId;

    @SerializedName("vehicle_id")
    private String vehicleId;
}
