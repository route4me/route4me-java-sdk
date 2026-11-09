package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The body of {@link SchedulesManager#getRouteScheduleCopies(GetRouteScheduleCopiesRequest)}. */
@Data
public class GetRouteScheduleCopiesRequest {
    /** The ID of the master route ({@code route_id}). */
    @SerializedName("route_id")
    private String routeId;

    /** The ID of the schedule ({@code schedule_id}). */
    @SerializedName("schedule_id")
    private String scheduleId;

    /** The date of the copies to find, such as {@code 2020-01-01} ({@code route_date}). */
    @SerializedName("route_date")
    private String routeDate;
}
