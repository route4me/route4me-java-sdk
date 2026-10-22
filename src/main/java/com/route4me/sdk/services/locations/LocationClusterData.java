package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The center, the extent and the size of a {@link LocationCluster}. */
@Data
public class LocationClusterData {
    /** Latitude of the centroid of the clustered destinations ({@code lat}). */
    @SerializedName("lat")
    private Double lat;

    /** Longitude of the centroid of the clustered destinations ({@code lng}). */
    @SerializedName("lng")
    private Double lng;

    /** Geographic bounding box containing all locations in this cluster ({@code bounding_box}). */
    @SerializedName("bounding_box")
    private LocationBoundingBox boundingBox;

    /** Number of locations in this cluster ({@code locations_count}). */
    @SerializedName("locations_count")
    private Integer locationsCount;
}
