package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Step {

    @SerializedName("directions")
    private String directions;
    @SerializedName("direction")
    private String direction;
    @SerializedName("distance")
    private Number distance;
    @SerializedName("distance_unit")
    private String distanceUnit;
    @SerializedName("maneuver_type")
    private String maneuverType;
    @SerializedName("compass_direction")
    private String compassDirection;
    @SerializedName("maneuver_point")
    private ManeuverPoint maneuverPoint;
    @SerializedName("duration_sec")
    private Number durationSec;

}
