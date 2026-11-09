package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A period on which a schedule creates no route: one item of {@code schedule_blacklist}. The API requires all three
 * fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleBlacklistItem {
    /** Whether the period applies ({@code enabled}). */
    @SerializedName("enabled")
    private Boolean enabled;

    /** The first day of the period, as {@code YYYY-MM-DD} ({@code start}). */
    @SerializedName("start")
    private String start;

    /** The last day of the period, as {@code YYYY-MM-DD} ({@code end}). */
    @SerializedName("end")
    private String end;
}
