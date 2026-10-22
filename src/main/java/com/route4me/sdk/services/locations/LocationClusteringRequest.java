package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LocationClusteringRequest extends LocationCombinedRequest {
    @SerializedName("clustering")
    private ClusteringParameters clustering;

    @SerializedName("with_clusters")
    private Boolean withClusters;
}
