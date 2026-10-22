package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** The body of {@link LocationManager#getLocationsHeatmap(LocationHeatmapRequest)}. */
@Data
@EqualsAndHashCode(callSuper = true)
public class LocationHeatmapRequest extends LocationCombinedRequest {
    /** Zoom level for heatmap granularity (0-22, where higher values show more detail) ({@code zoom}). */
    @SerializedName("zoom")
    private Integer zoom;
}
