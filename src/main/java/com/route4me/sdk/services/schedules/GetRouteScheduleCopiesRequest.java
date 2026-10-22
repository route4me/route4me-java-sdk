package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class GetRouteScheduleCopiesRequest {
    @SerializedName("route_id")
    private String routeId;

    @SerializedName("schedule_id")
    private String scheduleId;

    @SerializedName("route_date")
    private String routeDate;
}
