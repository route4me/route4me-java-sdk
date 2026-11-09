package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** A break of a route: when it happens, how long it lasts and how often it repeats. */
@Data
public class Break {
    /** Break mode. 0 = elapsed time, 1 = service time, 2 = travel time, 3 = location number ({@code mode}). */
    @SerializedName("mode")
    private Integer mode;

    /**
     * The first item is the time in seconds (mode 0 or 2) or the number of stops (mode 3). It must be above 0 ({@code
     * mode_params}).
     */
    @SerializedName("mode_params")
    private List<Integer> modeParams;

    /** The break duration, in seconds ({@code duration}). */
    @SerializedName("duration")
    private Integer duration;

    /** How many times the break repeats ({@code repeats_number}). */
    @SerializedName("repeats_number")
    private Integer repeatsNumber;
}
