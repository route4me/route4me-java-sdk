package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class RouteSchedulePreview {
    @SerializedName("version")
    private String version;

    @SerializedName("dates")
    private List<RouteSchedulePreviewDate> dates;
}
