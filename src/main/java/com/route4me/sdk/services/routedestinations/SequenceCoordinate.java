package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** One point of a {@link DestinationSequenceRequest}. */
@Data
public class SequenceCoordinate {
    /** Latitude of the location ({@code lat}). */
    @SerializedName("lat")
    private double lat;

    /** Longitude of the location ({@code lng}). */
    @SerializedName("lng")
    private double lng;
}
