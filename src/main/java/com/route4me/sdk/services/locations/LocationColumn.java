package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A column of the location list. */
@Data
public class LocationColumn {
    /** Indicates if column should take full width ({@code full_width}). */
    @SerializedName("full_width")
    private Boolean fullWidth;

    /** Indicates if column is sortable ({@code sortable}). */
    @SerializedName("sortable")
    private Boolean sortable;

    /** Unique identifier for the column ({@code id}). */
    @SerializedName("id")
    private String id;

    /** Configuration string for cell rendering ({@code cell_config}). */
    @SerializedName("cell_config")
    private String cellConfig;

    /** Display label for the column ({@code label}). */
    @SerializedName("label")
    private String label;
}
