package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The body of {@link SchedulesManager#createMasterRoute(CreateMasterRouteRequest)}. */
@Data
public class CreateMasterRouteRequest {
    /** The ID of the route ({@code route_id}). */
    @SerializedName("route_id")
    private String routeId;

    /** The name of the route ({@code route_name}). */
    @SerializedName("route_name")
    private String routeName;

    /** The member ID of the driver of the scheduled routes ({@code member_id}). */
    @SerializedName("member_id")
    private Long memberId;

    /** The ID of the vehicle of the scheduled routes ({@code vehicle_id}). */
    @SerializedName("vehicle_id")
    private String vehicleId;

    /** The name of the schedule ({@code name}). */
    @SerializedName("name")
    private String name;

    /** The dates on which the schedule creates no route ({@code schedule_blacklist}). */
    @SerializedName("schedule_blacklist")
    private List<String> scheduleBlacklist;

    /** How far in advance the routes are created ({@code advance_interval}). */
    @SerializedName("advance_interval")
    private Integer advanceInterval;

    /**
     * How many days in advance the schedule is checked for routes to create ({@code advance_schedule_interval_days}).
     */
    @SerializedName("advance_schedule_interval_days")
    private Integer advanceScheduleIntervalDays;

    /** The time zone of the schedule, such as {@code America/New_York} ({@code timezone}). */
    @SerializedName("timezone")
    private String timezone;

    /** The recurrence rules of the schedule (daily, weekly or monthly), as the API takes them ({@code schedule}). */
    @SerializedName("schedule")
    private Object scheduleData;

    /** Whether to create the routes of the schedule at once ({@code sync}). */
    @SerializedName("sync")
    private Boolean sync;
}
