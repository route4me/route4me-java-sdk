package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class RouteDestinationFieldItem {
    @SerializedName("label")
    private String label;

    @SerializedName("value")
    private String value;

    @SerializedName("kind")
    private String kind;

    @SerializedName("type")
    private String type;
}
