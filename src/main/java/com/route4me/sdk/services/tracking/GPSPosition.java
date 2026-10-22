package com.route4me.sdk.services.tracking;

import com.route4me.sdk.queryconverter.QueryParameter;
import com.route4me.sdk.services.routing.Constants.DeviceType;
import com.route4me.sdk.services.routing.Constants.Format;
import lombok.Data;
import lombok.experimental.Accessors;

/** A GPS position to record with {@link TrackingManager#setGPS(GPSPosition)}, sent as query parameters. */
@Data
@Accessors(chain = true)
public class GPSPosition {

    /** Unique ID of a route the device assigned to ({@code route_id}). */
    @QueryParameter("route_id")
    private String routeId;
    /** The format of the answer: {@code json} or {@code xml} ({@code format}). */
    @QueryParameter("format")
    private Format format;
    /** Latitude of a device position ({@code lat}). */
    @QueryParameter("lat")
    private Double latitude;
    /** Longitude of a device position ({@code lng}). */
    @QueryParameter("lng")
    private Double longitude;
    /** Altitude of a device position ({@code altitude}). */
    @QueryParameter("altitude")
    private Double altitude;
    /** The direction in degrees in which the vehicle is heading ({@code course}). */
    @QueryParameter("course")
    private Integer course;
    /** Vehicle speed ({@code speed}). */
    @QueryParameter("speed")
    private Float speed;
    /** The type of the device, such as {@code iphone} or {@code android_phone} ({@code device_type}). */
    @QueryParameter("device_type")
    private DeviceType deviceType;
    /** Globally unique identifier of a device ({@code device_guid}). */
    @QueryParameter("device_guid")
    private String deviceGUID;
    /** The timestamp on the local (remote relative to the server) device ({@code device_timestamp}). */
    @QueryParameter("device_timestamp")
    private String deviceTimestamp;
    /** The version of the app submitting the data ({@code app_version}). */
    @QueryParameter("app_version")
    private String appVersion;
    /** Unique ID of a member ({@code member_id}). */
    @QueryParameter("member_id")
    private String memberId;
}
