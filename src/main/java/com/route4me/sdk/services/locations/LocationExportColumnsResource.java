package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A column a location export can have. */
@Data
public class LocationExportColumnsResource {
    /** Internal column key or identifier ({@code field_name}). */
    @SerializedName("field_name")
    private String fieldName;

    /** Human-readable, translated column title ({@code field_title}). */
    @SerializedName("field_title")
    private String fieldTitle;

    /** Logical group for UI grouping/filtering ({@code group}). */
    @SerializedName("group")
    private String group;

    /** Where the column is coming from ({@code scope}). */
    @SerializedName("scope")
    private String scope;

    /** Whether the user has permission to see the column ({@code allowed}). */
    @SerializedName("allowed")
    private Boolean allowed;
}
