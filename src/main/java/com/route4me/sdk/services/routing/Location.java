package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Location {

    @SerializedName("time")
    private Number time;
    @SerializedName("segment_distance")
    private Number segmentDistance;
    @SerializedName("name")
    private String name;
    @SerializedName("start_location")
    private String startLocation;
    @SerializedName("end_location")
    private String endLocation;
    @SerializedName("directions_error")
    private String directionsError;
    @SerializedName("error_code")
    private Number errorCode;
}
