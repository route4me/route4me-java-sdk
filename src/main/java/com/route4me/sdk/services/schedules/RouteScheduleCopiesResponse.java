package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The answer of {@link SchedulesManager#getRouteScheduleCopies(GetRouteScheduleCopiesRequest)}. */
@Data
public class RouteScheduleCopiesResponse {
    /** The ID of the master route ({@code parent_route_id}). */
    @SerializedName("parent_route_id")
    private String parentRouteId;

    /** The routes the schedule created from it ({@code scheduled_routes}). */
    @SerializedName("scheduled_routes")
    private List<ScheduledRoute> scheduledRoutes;
}
