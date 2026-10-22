package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The start or end of a route leg, in {@link Direction}. */
@Data
public class Location {

    /** Required time for passing the segment (seconds) ({@code time}). */
    @SerializedName("time")
    private Number time;
    /** Segment distance ({@code segment_distance}). */
    @SerializedName("segment_distance")
    private Number segmentDistance;
    /** Direction name ({@code name}). */
    @SerializedName("name")
    private String name;
    /** Start Location ({@code start_location}). */
    @SerializedName("start_location")
    private String startLocation;
    /** End Location ({@code end_location}). */
    @SerializedName("end_location")
    private String endLocation;
    /** Directions Error ({@code directions_error}). */
    @SerializedName("directions_error")
    private String directionsError;
    /** Error Code ({@code error_code}). */
    @SerializedName("error_code")
    private Number errorCode;
}
