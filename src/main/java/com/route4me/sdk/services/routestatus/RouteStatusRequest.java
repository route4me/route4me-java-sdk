package com.route4me.sdk.services.routestatus;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class RouteStatusRequest {
    @SerializedName("status")
    private String status;

    @SerializedName("lat")
    private double latitude;

    @SerializedName("lng")
    private double longitude;

    @SerializedName("event_timestamp")
    private Long eventTimestamp;
}
