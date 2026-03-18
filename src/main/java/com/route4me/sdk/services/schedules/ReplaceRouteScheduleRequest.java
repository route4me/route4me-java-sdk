package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class ReplaceRouteScheduleRequest {
    @SerializedName("member_id")
    private Long memberId;

    @SerializedName("vehicle_id")
    private String vehicleId;

    @SerializedName("name")
    private String name;

    @SerializedName("schedule_uid")
    private String scheduleUid;

    @SerializedName("advance_interval")
    private Integer advanceInterval;

    @SerializedName("advance_schedule_interval_days")
    private Integer advanceScheduleIntervalDays;

    @SerializedName("schedule_blacklist")
    private List<String> scheduleBlacklist;

    @SerializedName("timezone")
    private String timezone;

    @SerializedName("schedule")
    private Object scheduleData;
}
