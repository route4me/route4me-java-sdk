package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** A page of schedules, the answer of {@link SchedulesManager#getSchedulesWithPagination(int, int)}. */
@Data
@EqualsAndHashCode(callSuper = true)
public class ScheduleListWithPagination extends ScheduleList {
    /** The links to the other pages ({@code links}). */
    @SerializedName("links")
    private PaginationLinks links;

    /** The position of this page ({@code meta}). */
    @SerializedName("meta")
    private PaginationMeta meta;
}
