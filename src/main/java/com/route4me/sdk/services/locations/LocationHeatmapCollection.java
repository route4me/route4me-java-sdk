package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class LocationHeatmapCollection {
    @SerializedName("data")
    private List<double[]> data;

    @SerializedName("fields")
    private List<String> fields;
}
