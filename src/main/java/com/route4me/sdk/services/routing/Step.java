package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** One step of the driving directions of a {@link Direction}. */
@Data
public class Step {

    /** Name (brief) ({@code directions}). */
    @SerializedName("directions")
    private String directions;
    /** Name (detailed) ({@code direction}). */
    @SerializedName("direction")
    private String direction;
    /** Distance ({@code distance}). */
    @SerializedName("distance")
    private Number distance;
    /** Distance unit ({@code distance_unit}). */
    @SerializedName("distance_unit")
    private String distanceUnit;
    /** The kind of maneuver, such as a turn ({@code maneuver_type}). */
    @SerializedName("maneuver_type")
    private String maneuverType;
    /** Compass Direction. Available values: N, S, W, E, NW, NE, SW, SE ({@code compass_direction}). */
    @SerializedName("compass_direction")
    private String compassDirection;
    /** Where the maneuver takes place ({@code maneuver_point}). */
    @SerializedName("maneuver_point")
    private ManeuverPoint maneuverPoint;
    /** Direction step duration(seconds) ({@code duration_sec}). */
    @SerializedName("duration_sec")
    private Number durationSec;

}
