package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * The answer of {@link SchedulesManager#deleteRouteScheduleItems(String)}. The route schedules spec answers with the
 * remaining route schedules ({@code {"data": [...]}}), and the C# SDK reads a status ({@code {"status": true}}), so
 * both are read: the field the reply does not carry is {@code null}.
 */
@Data
public class DeleteRouteScheduleItemsResponse {
    /** Whether the request succeeded, when the reply is a status ({@code status}). */
    @SerializedName("status")
    private Boolean status;
    /** The route schedules, when the reply is a list of them ({@code data}). */
    @SerializedName("data")
    private List<RouteSchedule> data;
}
