package com.route4me.sdk.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ManeuverPoint {
    @SerializedName("lat")
    private Number latitude;
    @SerializedName("longitude")
    private Number longitude;
}
