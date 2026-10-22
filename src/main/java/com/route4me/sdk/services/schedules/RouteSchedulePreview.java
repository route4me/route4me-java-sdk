package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The answer of {@link SchedulesManager#getRouteSchedulePreview(String, String, String)}. */
@Data
public class RouteSchedulePreview {
    /** The version of the schedule rules ({@code version}). */
    @SerializedName("version")
    private String version;

    /** The dates of the preview period ({@code dates}). */
    @SerializedName("dates")
    private List<RouteSchedulePreviewDate> dates;
}
