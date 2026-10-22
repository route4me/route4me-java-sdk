package com.route4me.sdk.services.tracking;

import com.google.gson.annotations.SerializedName;
import java.util.Map;
import lombok.Data;

/** The last known position of a user, in a {@link UserLocation}. */
@Data
public class UserTracking {
    /** Longitude ({@code position_lng}). */
    @SerializedName("position_lng")
    private Double positionLng;
    /** Latitude ({@code position_lat}). */
    @SerializedName("position_lat")
    private Double positionLat;
    /** The direction, in degrees ({@code direction}). */
    @SerializedName("direction")
    private Integer direction;
    /** The source of the position ({@code data_source_name}). */
    @SerializedName("data_source_name")
    private String dataSourceName;
    /** When the server received the position, as a Unix timestamp ({@code activity_timestamp}). */
    @SerializedName("activity_timestamp")
    private Long activityTimestamp;
    /** The same time, readable ({@code activity_timestamp_friendly}). */
    @SerializedName("activity_timestamp_friendly")
    private String activityTimestampFriendly;
    /** When the device recorded the position, as a Unix timestamp ({@code device_timestamp}). */
    @SerializedName("device_timestamp")
    private Long deviceTimestamp;
    /** The route of the position ({@code route_id}). */
    @SerializedName("route_id")
    private String routeId;
    /** The device ID ({@code device_id}). */
    @SerializedName("device_id")
    private String deviceId;
    /** The device type ({@code device_type}). */
    @SerializedName("device_type")
    private String deviceType;
    /** The speed ({@code speed}). */
    @SerializedName("speed")
    private Double speed;
    /** The unit of the speed ({@code speed_unit}). */
    @SerializedName("speed_unit")
    private String speedUnit;
    /** The speed calculated by the device ({@code calculated_speed}). */
    @SerializedName("calculated_speed")
    private String calculatedSpeed;
    /** The bearing, in degrees ({@code bearing}). */
    @SerializedName("bearing")
    private Integer bearing;
    /** The location accuracy ({@code accuracy}). */
    @SerializedName("accuracy")
    private String accuracy;
    /** The altitude ({@code altitude}). */
    @SerializedName("altitude")
    private Integer altitude;
    /** The vehicle ID ({@code vehicle_id}). */
    @SerializedName("vehicle_id")
    private String vehicleId;
    /** The member ID ({@code member_id}). */
    @SerializedName("member_id")
    private Long memberId;
    /** The account owner's member ID ({@code root_member_id}). */
    @SerializedName("root_member_id")
    private Long rootMemberId;
    /** The time zone of the device ({@code device_timezone}). */
    @SerializedName("device_timezone")
    private String deviceTimezone;
    /** Custom data stored with the position ({@code custom_data}). */
    @SerializedName("custom_data")
    private Map<String, Object> customData;
}
