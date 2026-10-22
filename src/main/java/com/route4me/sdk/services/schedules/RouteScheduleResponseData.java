package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A route schedule with the route name and the schedule, in a {@link RouteScheduleResponse}. */
@Data
public class RouteScheduleResponseData {
    /** The ID of the route ({@code route_id}). */
    @SerializedName("route_id")
    private String routeId;

    /** The name of the route ({@code route_name}). */
    @SerializedName("route_name")
    private String routeName;

    /** The ID of the schedule ({@code schedule_uid}). */
    @SerializedName("schedule_uid")
    private String scheduleUid;

    /** The member ID of the driver of the scheduled routes ({@code member_id}). */
    @SerializedName("member_id")
    private Long memberId;

    /** The ID of the vehicle of the scheduled routes ({@code vehicle_id}). */
    @SerializedName("vehicle_id")
    private String vehicleId;

    /** The schedule of the route ({@code schedule}). */
    @SerializedName("schedule")
    private Schedule schedule;
}
