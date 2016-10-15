package com.route4me.sdk.services.tracking;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class TrackingHistory {
    @SerializedName("s")
    private String s;
    @SerializedName("lt")
    private Double latitude;
    @SerializedName("lg")
    private Double longitude;
    @SerializedName("d")
    private String d;
    @SerializedName("ts")
    private String timestamp;
    @SerializedName("ts_friendly")
    private String friendlyTimestamp;
}
