package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class RouteScheduleResponseData {
    @SerializedName("route_id")
    private String routeId;

    @SerializedName("route_name")
    private String routeName;

    @SerializedName("schedule_uid")
    private String scheduleUid;

    @SerializedName("member_id")
    private Long memberId;

    @SerializedName("vehicle_id")
    private String vehicleId;

    @SerializedName("schedule")
    private Schedule schedule;
}
