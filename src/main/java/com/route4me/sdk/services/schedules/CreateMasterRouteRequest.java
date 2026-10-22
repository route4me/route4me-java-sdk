package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class CreateMasterRouteRequest {
    @SerializedName("route_id")
    private String routeId;

    @SerializedName("route_name")
    private String routeName;

    @SerializedName("member_id")
    private Long memberId;

    @SerializedName("vehicle_id")
    private String vehicleId;

    @SerializedName("name")
    private String name;

    @SerializedName("schedule_blacklist")
    private List<String> scheduleBlacklist;

    @SerializedName("advance_interval")
    private Integer advanceInterval;

    @SerializedName("advance_schedule_interval_days")
    private Integer advanceScheduleIntervalDays;

    @SerializedName("timezone")
    private String timezone;

    @SerializedName("schedule")
    private Object scheduleData;

    @SerializedName("sync")
    private Boolean sync;
}
