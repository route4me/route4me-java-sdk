package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class LocationClusterData {
    @SerializedName("lat")
    private Double lat;

    @SerializedName("lng")
    private Double lng;

    @SerializedName("bounding_box")
    private LocationBoundingBox boundingBox;

    @SerializedName("locations_count")
    private Integer locationsCount;
}
