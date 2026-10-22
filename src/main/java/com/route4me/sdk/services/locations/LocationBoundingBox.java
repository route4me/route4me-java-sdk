package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class LocationBoundingBox {
    @SerializedName("top")
    private Double top;

    @SerializedName("left")
    private Double left;

    @SerializedName("bottom")
    private Double bottom;

    @SerializedName("right")
    private Double right;
}
