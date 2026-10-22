package com.route4me.sdk.services.tracking;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The body of {@link TrackingManager#setTrackingPings(TrackingPingsRequest)}: the member, device and vehicle that send
 * the pings, and either one ping (the fields inherited from {@link TrackingPing}) or a batch of 1 to 100 pings in
 * {@code items}. In a batch, the other top-level fields are defaults for every item; the member, device and vehicle
 * are read from the top level only. Fields left {@code null} are not sent.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TrackingPingsRequest extends TrackingPing {
    /** The most pings a batch may hold. */
    public static final int MAX_ITEMS = 100;

    /** The member that sends the pings; a missing or invalid value rejects them ({@code member_id}). */
    @SerializedName("member_id")
    private Long memberId;
    /** The device identifier, used before {@code device_id} except on Android ({@code device_guid}). */
    @SerializedName("device_guid")
    private String deviceGuid;
    /** The device identifier when {@code device_guid} is not set, and always on Android ({@code device_id}). */
    @SerializedName("device_id")
    private String deviceId;
    /** The device type, such as {@code iphone}, {@code ipad}, {@code android_phone} ({@code device_type}). */
    @SerializedName("device_type")
    private String deviceType;
    /** The tracking source, such as {@code R4M} ({@code vendor_name}). */
    @SerializedName("vendor_name")
    private String vendorName;
    /** The vehicle, 32 hex characters ({@code vehicle_id}). */
    @SerializedName("vehicle_id")
    private String vehicleId;
    /** The vehicle ID in an external telematics system ({@code external_telematics_vehicle_id}). */
    @SerializedName("external_telematics_vehicle_id")
    private String externalTelematicsVehicleId;
    /** The unit of {@code speed}: {@code m/s}, {@code km/h} or {@code mph} ({@code speed_unit}). */
    @SerializedName("speed_unit")
    private String speedUnit;
    /** The version of the mobile app ({@code app_version}). */
    @SerializedName("app_version")
    private String appVersion;
    /** The build number of the mobile app ({@code app_build_number}). */
    @SerializedName("app_build_number")
    private String appBuildNumber;
    /** The e-mail of the driver ({@code email}). */
    @SerializedName("email")
    private String email;
    /** The pings of a batch, 1 to 100; {@code null} sends a single ping ({@code items}). */
    @SerializedName("items")
    private List<TrackingPing> items;
}
