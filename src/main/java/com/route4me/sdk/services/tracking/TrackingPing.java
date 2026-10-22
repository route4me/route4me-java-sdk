package com.route4me.sdk.services.tracking;

import com.google.gson.annotations.SerializedName;
import java.util.Map;
import lombok.Data;

/**
 * One GPS ping for {@link TrackingManager#setTrackingPings(TrackingPingsRequest)}: an item of a batch, or, through
 * {@link TrackingPingsRequest}, a single ping. Fields left {@code null} are not sent.
 */
@Data
public class TrackingPing {
    /** The latitude, from -90 to 90; 0 rejects the ping. Required ({@code lat}). */
    @SerializedName("lat")
    private Double lat;
    /** The longitude, from -180 to 180; 0 rejects the ping. Required ({@code lng}). */
    @SerializedName("lng")
    private Double lng;
    /**
     * The time of the ping, as Unix seconds ({@link Long}) or a date string; missing means the time the request is
     * received ({@code device_timestamp}).
     */
    @SerializedName("device_timestamp")
    private Object deviceTimestamp;
    /** The time zone used to read a date string in {@code device_timestamp} ({@code device_timezone}). */
    @SerializedName("device_timezone")
    private String deviceTimezone;
    /** The time zone offset of the device ({@code device_timezone_offset}). */
    @SerializedName("device_timezone_offset")
    private String deviceTimezoneOffset;
    /** The speed, in {@code speed_unit} ({@code speed}). */
    @SerializedName("speed")
    private Double speed;
    /** The speed accuracy reported by the device ({@code speed_accuracy}). */
    @SerializedName("speed_accuracy")
    private Double speedAccuracy;
    /** The speed calculated by the device ({@code calculated_speed}). */
    @SerializedName("calculated_speed")
    private Double calculatedSpeed;
    /** The course, in degrees ({@code course}). */
    @SerializedName("course")
    private Double course;
    /** The bearing, in degrees ({@code bearing}). */
    @SerializedName("bearing")
    private Double bearing;
    /** The bearing accuracy reported by the device ({@code bearing_accuracy}). */
    @SerializedName("bearing_accuracy")
    private Double bearingAccuracy;
    /** The location accuracy reported by the device ({@code accuracy}). */
    @SerializedName("accuracy")
    private Double accuracy;
    /** The altitude ({@code altitude}). */
    @SerializedName("altitude")
    private Double altitude;
    /** The step count reported by the device ({@code footsteps}). */
    @SerializedName("footsteps")
    private Integer footsteps;
    /** The route of the ping, 32 hex characters ({@code route_id}). */
    @SerializedName("route_id")
    private String routeId;
    /** Custom data stored with the ping ({@code custom_data}). */
    @SerializedName("custom_data")
    private Map<String, Object> customData;
    /** Whether a single-ping answer also returns {@code activity_at}, {@code is_mobile} and {@code mph_speed}. */
    @SerializedName("extend_response_data")
    private Boolean extendResponseData;
}
