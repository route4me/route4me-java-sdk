package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class LocationClusteringResource {
    @SerializedName("clusters")
    private List<LocationCluster> clusters;

    @SerializedName("locations")
    private List<LocationClusterItem> locations;

    @SerializedName("with_clusters")
    private Boolean withClusters;
}
