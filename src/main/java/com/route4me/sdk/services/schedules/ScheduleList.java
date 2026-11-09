package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** A list of schedules, as the API returns it. */
@Data
public class ScheduleList {
    /** The schedules ({@code data}). */
    @SerializedName("data")
    private List<Schedule> data;
}
