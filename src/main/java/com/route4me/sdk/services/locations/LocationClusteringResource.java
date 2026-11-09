package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The answer of {@link LocationManager#getLocationsClustering(LocationClusteringRequest)}. */
@Data
public class LocationClusteringResource {
    /** The clusters of locations ({@code clusters}). */
    @SerializedName("clusters")
    private List<LocationCluster> clusters;

    /** The locations that are not in a cluster ({@code locations}). */
    @SerializedName("locations")
    private List<LocationClusterItem> locations;

    /** Whether the answer groups the locations in clusters ({@code with_clusters}). */
    @SerializedName("with_clusters")
    private Boolean withClusters;
}
