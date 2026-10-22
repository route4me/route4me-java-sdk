package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class LocationColumn {
    @SerializedName("full_width")
    private Boolean fullWidth;

    @SerializedName("sortable")
    private Boolean sortable;

    @SerializedName("id")
    private String id;

    @SerializedName("cell_config")
    private String cellConfig;

    @SerializedName("label")
    private String label;
}
