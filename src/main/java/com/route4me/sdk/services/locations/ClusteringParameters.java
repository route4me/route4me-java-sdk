package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The clustering options of a {@link LocationClusteringRequest}. */
@Data
public class ClusteringParameters {
    /** Zoom level for clustering (0-22, where higher values show more detail) ({@code zoom}). */
    @SerializedName("zoom")
    private Integer zoom;
}
