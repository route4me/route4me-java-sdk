package com.route4me.sdk.services.territories;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** An address to cluster with {@link TerritoriesManagerV5#createAutomaticTerritoriesJob}. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutomaticTerritoriesAddress {
    /** The address ID, a string or an integer, distinct in the job; required ({@code id}). */
    @SerializedName("id")
    private Object id;
    /** Latitude, from -90 to 90 ({@code lat}). */
    @SerializedName("lat")
    private Double lat;
    /** Longitude, from -180 to 180 ({@code lng}). */
    @SerializedName("lng")
    private Double lng;
}
