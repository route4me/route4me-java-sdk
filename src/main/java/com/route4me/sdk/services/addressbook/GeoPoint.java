package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A point on the map, in degrees. */
@Data
public class GeoPoint {
    /** The latitude ({@code lat}). */
    @SerializedName("lat")
    private Double lat;

    /** The longitude ({@code lng}). */
    @SerializedName("lng")
    private Double lng;
}
