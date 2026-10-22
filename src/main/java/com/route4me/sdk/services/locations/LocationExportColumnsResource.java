package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class LocationExportColumnsResource {
    @SerializedName("field_name")
    private String fieldName;

    @SerializedName("field_title")
    private String fieldTitle;

    @SerializedName("group")
    private String group;

    @SerializedName("scope")
    private String scope;

    @SerializedName("allowed")
    private Boolean allowed;
}
