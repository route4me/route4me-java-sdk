package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** One date of a {@link RouteSchedulePreview}. */
@Data
public class RouteSchedulePreviewDate {
    /** The date, such as {@code 2026-05-01} ({@code date}). */
    @SerializedName("date")
    private String date;

    /** Whether the schedule creates a route on this date ({@code scheduled}). */
    @SerializedName("scheduled")
    private Boolean scheduled;

    /** Whether the route of this date is already created ({@code created}). */
    @SerializedName("created")
    private Boolean created;

    /** Whether the date is excluded by the blacklist ({@code excluded}). */
    @SerializedName("excluded")
    private Boolean excluded;
}
