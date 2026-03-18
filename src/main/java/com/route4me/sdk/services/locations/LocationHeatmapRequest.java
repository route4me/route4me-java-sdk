package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LocationHeatmapRequest extends LocationCombinedRequest {
    @SerializedName("zoom")
    private Integer zoom;
}
