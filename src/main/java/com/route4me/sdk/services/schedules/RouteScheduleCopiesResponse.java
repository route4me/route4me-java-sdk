package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class RouteScheduleCopiesResponse {
    @SerializedName("parent_route_id")
    private String parentRouteId;

    @SerializedName("scheduled_routes")
    private List<ScheduledRoute> scheduledRoutes;
}
