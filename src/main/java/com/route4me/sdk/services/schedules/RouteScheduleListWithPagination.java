package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** A page of route schedules, the answer of {@link SchedulesManager#getRouteSchedulesWithPagination(int, int)}. */
@Data
@EqualsAndHashCode(callSuper = true)
public class RouteScheduleListWithPagination extends RouteScheduleList {
    /** The links to the other pages ({@code links}). */
    @SerializedName("links")
    private PaginationLinks links;

    /** The position of this page ({@code meta}). */
    @SerializedName("meta")
    private PaginationMeta meta;
}
