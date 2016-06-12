package com.route4me.sdk.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Step {

    private String directions;
    private String direction;
    private Number distance;
    @SerializedName("distance_unit")
    private String distanceUnit;
    private String maneuverType;
    @SerializedName("compass_direction")
    private String compassDirection;
    private ManeuverPoint maneuverPoint;
    @SerializedName("duration_sec")
    private Number durationSec;

}
