package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The answer of {@link LocationManager#getLocationsHeatmap(LocationHeatmapRequest)}. */
@Data
public class LocationHeatmapCollection {
    /** Array of heatmap data points [lat, lng, intensity] ({@code data}). */
    @SerializedName("data")
    private List<double[]> data;

    /** Field names for the data array ({@code fields}). */
    @SerializedName("fields")
    private List<String> fields;
}
