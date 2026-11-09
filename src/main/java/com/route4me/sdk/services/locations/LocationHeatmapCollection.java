package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The answer of {@link LocationManager#getLocationsHeatmap(LocationHeatmapRequest)}. */
@Data
public class LocationHeatmapCollection {
    /**
     * The heat map points, each a list of numbers in the order of {@link #getFields()}, such as
     * {@code [lat, lng, weight]}; a {@code null} number stays {@code null} ({@code data}).
     */
    @SerializedName("data")
    private List<List<Double>> data;

    /** Field names for the data array ({@code fields}). */
    @SerializedName("fields")
    private List<String> fields;
}
