package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The body of {@link LocationManager#getLocationsClustering(LocationClusteringRequest)}: the location filters and
 * the clustering options.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LocationClusteringRequest extends LocationCombinedRequest {
    /** Clustering configuration parameters ({@code clustering}). */
    @SerializedName("clustering")
    private ClusteringParameters clustering;

    /** Whether to include cluster information in the response ({@code with_clusters}). */
    @SerializedName("with_clusters")
    private Boolean withClusters;
}
