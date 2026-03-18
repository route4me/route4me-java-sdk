package com.route4me.sdk.services.facilities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class FacilityCoordinates {
    @SerializedName("lat")
    private Double lat;

    @SerializedName("lng")
    private Double lng;
}
