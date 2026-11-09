package com.route4me.sdk.services.facilities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The coordinates of a facility. */
@Data
public class FacilityCoordinates {
    /** Latitude ({@code lat}). */
    @SerializedName("lat")
    private Double lat;

    /** Longitude ({@code lng}). */
    @SerializedName("lng")
    private Double lng;
}
