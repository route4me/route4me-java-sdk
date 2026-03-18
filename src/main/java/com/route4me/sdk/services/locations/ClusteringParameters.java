package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ClusteringParameters {
    @SerializedName("zoom")
    private Integer zoom;
}
