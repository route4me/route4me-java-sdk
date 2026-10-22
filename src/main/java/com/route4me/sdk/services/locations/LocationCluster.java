package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A cluster of locations, in a {@link LocationClusteringResource}. */
@Data
public class LocationCluster {
    /** Cluster data with location and bounding box ({@code cluster}). */
    @SerializedName("cluster")
    private LocationClusterData cluster;
}
