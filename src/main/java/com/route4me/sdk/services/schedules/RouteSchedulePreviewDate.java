package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class RouteSchedulePreviewDate {
    @SerializedName("date")
    private String date;

    @SerializedName("scheduled")
    private Boolean scheduled;

    @SerializedName("created")
    private Boolean created;

    @SerializedName("excluded")
    private Boolean excluded;
}
