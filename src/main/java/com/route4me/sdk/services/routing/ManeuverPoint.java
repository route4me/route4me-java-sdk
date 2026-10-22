package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** Where a maneuver of a {@link Step} takes place. */
@Data
public class ManeuverPoint {
    /** Latitude ({@code lat}). */
    @SerializedName("lat")
    private Number latitude;
    /** Longitude ({@code longitude}). */
    @SerializedName("longitude")
    private Number longitude;
}
