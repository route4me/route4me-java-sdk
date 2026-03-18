package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class RouteScheduleList {
    @SerializedName("data")
    private List<RouteSchedule> data;
}
