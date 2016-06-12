package com.route4me.sdk.services.tracking;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class TrackingHistory {
    @SerializedName("s")
    private Number s;
    @SerializedName("lt")
    private Number latitude;
    @SerializedName("lg")
    private Number longitude;
    private String d;
    private String ts;
    private String ts_friendly;
}
