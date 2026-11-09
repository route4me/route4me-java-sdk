package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** A list of route schedules, as the API returns it. */
@Data
public class RouteScheduleList {
    /** The route schedules ({@code data}). */
    @SerializedName("data")
    private List<RouteSchedule> data;
}
