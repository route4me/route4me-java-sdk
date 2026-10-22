package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A route a schedule created. */
@Data
public class ScheduledRoute {
    /** The ID of the schedule ({@code schedule_uid}). */
    @SerializedName("schedule_uid")
    private String scheduleUid;

    /** The ID of the route the schedule created ({@code child_route_id}). */
    @SerializedName("child_route_id")
    private String childRouteId;

    /** The date of that route ({@code route_date}). */
    @SerializedName("route_date")
    private String routeDate;
}
