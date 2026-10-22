package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The driving directions of a route leg. */
@Data
public class Direction {

    /** Where the directions start ({@code location}). */
    @SerializedName("location")
    private Location location;
    /** The steps of the directions ({@code steps}). */
    @SerializedName("steps")
    private List<Step> steps;
}
