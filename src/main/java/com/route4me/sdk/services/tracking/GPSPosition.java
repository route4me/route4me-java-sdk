package com.route4me.sdk.services.tracking;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.services.routing.Constants.DeviceType;
import com.route4me.sdk.services.routing.Constants.Format;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GPSPosition {

    @SerializedName("route_id")
    private String routeId;
    private Format format;
    @SerializedName("lat")
    private Double latitude;
    @SerializedName("lng")
    private Double longitude;
    private Double altitude;
    private Integer course;
    private Float speed;
    @SerializedName("device_type")
    private DeviceType deviceType;
    @SerializedName("device_guid")
    private String deviceGUID;
    @SerializedName("device_timestamp")
    private String deviceTimestamp;
    @SerializedName("app_version")
    private String appVersion;
    @SerializedName("member_id")
    private String memberId;
}
