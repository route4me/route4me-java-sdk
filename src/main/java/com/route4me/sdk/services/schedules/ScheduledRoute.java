package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ScheduledRoute {
    @SerializedName("schedule_uid")
    private String scheduleUid;

    @SerializedName("child_route_id")
    private String childRouteId;

    @SerializedName("route_date")
    private String routeDate;
}
