package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A point given by its latitude and longitude. */
@Data
public class GeoPoint {
    /** Latitude ({@code lat}). */
    @SerializedName("lat")
    private Double lat;

    /** Longitude ({@code lng}). */
    @SerializedName("lng")
    private Double lng;
}
