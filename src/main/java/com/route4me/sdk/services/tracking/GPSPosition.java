package com.route4me.sdk.services.tracking;

import com.route4me.sdk.queryconverter.QueryParameter;
import com.route4me.sdk.services.routing.Constants.DeviceType;
import com.route4me.sdk.services.routing.Constants.Format;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GPSPosition {

    @QueryParameter("route_id")
    private String routeId;
    @QueryParameter("format")
    private Format format;
    @QueryParameter("lat")
    private Double latitude;
    @QueryParameter("lng")
    private Double longitude;
    @QueryParameter("altitude")
    private Double altitude;
    @QueryParameter("course")
    private Integer course;
    @QueryParameter("speed")
    private Float speed;
    @QueryParameter("device_type")
    private DeviceType deviceType;
    @QueryParameter("device_guid")
    private String deviceGUID;
    @QueryParameter("device_timestamp")
    private String deviceTimestamp;
    @QueryParameter("app_version")
    private String appVersion;
    @QueryParameter("member_id")
    private String memberId;
}
