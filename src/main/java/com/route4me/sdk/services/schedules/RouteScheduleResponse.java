package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * The answer of the single route-schedule methods of
 * {@link SchedulesManager}.
 */
@Data
public class RouteScheduleResponse {
    /** The route schedule ({@code data}). */
    @SerializedName("data")
    private List<RouteScheduleResponseData> data;
}
