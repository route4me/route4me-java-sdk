package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The link between a route and a schedule, with its driver and vehicle. */
@Data
public class RouteSchedule {
    /** The member ID of the driver of the scheduled routes ({@code member_id}). */
    @SerializedName("member_id")
    private Long memberId;

    /** The ID of the schedule ({@code schedule_uid}). */
    @SerializedName("schedule_uid")
    private String scheduleUid;

    /** The ID of the route ({@code route_id}). */
    @SerializedName("route_id")
    private String routeId;

    /** The ID of the vehicle of the scheduled routes ({@code vehicle_id}). */
    @SerializedName("vehicle_id")
    private String vehicleId;
}
