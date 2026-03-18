package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class GeoPoint {
    @SerializedName("lat")
    private Double lat;

    @SerializedName("lng")
    private Double lng;
}
