package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SequenceCoordinate {
    @SerializedName("lat")
    private float lat;

    @SerializedName("lng")
    private float lng;
}
