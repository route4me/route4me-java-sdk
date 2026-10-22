package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * The answer of {@link RouteDestinationsManager#getDestinationByLabelCode(String)}: the destination of a label
 * code, with its route, driver and vehicle.
 */
@Data
public class SortingFilteringResource {
    /** Integer ID of the route destination ({@code destination_id}). */
    @SerializedName("destination_id")
    private Integer destinationId;

    /** UUID of the route destination (32-character hex string) ({@code destination_uuid}). */
    @SerializedName("destination_uuid")
    private String destinationUuid;

    /** Display name of the route this destination belongs to ({@code route_name}). */
    @SerializedName("route_name")
    private String routeName;

    /** 32-character hex ID of the parent route ({@code route_id}). */
    @SerializedName("route_id")
    private String routeId;

    /** Unix timestamp of the scheduled date for this destination ({@code scheduled_for}). */
    @SerializedName("scheduled_for")
    private Integer scheduledFor;

    /** Street address of the destination ({@code destination_address}). */
    @SerializedName("destination_address")
    private String destinationAddress;

    /** Zero-based stop sequence number within the route ({@code sequence_no}). */
    @SerializedName("sequence_no")
    private Integer sequenceNo;

    /** Full name of the assigned driver. Null if unassigned ({@code assigned_user_driver}). */
    @SerializedName("assigned_user_driver")
    private String assignedUserDriver;

    /** Member (driver) ID. Null if unassigned ({@code member_id}). */
    @SerializedName("member_id")
    private Integer memberId;

    /** Alias of the assigned vehicle. Null if unassigned ({@code assigned_vehicle}). */
    @SerializedName("assigned_vehicle")
    private String assignedVehicle;

    /** 32-character hex ID of the assigned vehicle. Null if unassigned ({@code vehicle_id}). */
    @SerializedName("vehicle_id")
    private String vehicleId;

    /** Tracking number associated with this destination ({@code tracking_number}). */
    @SerializedName("tracking_number")
    private String trackingNumber;

    /** Label code used to look up this destination (e.g., "label_5") ({@code label_code}). */
    @SerializedName("label_code")
    private String labelCode;

    /** Encoded polyline path to this destination. Null if not available ({@code path}). */
    @SerializedName("path")
    private String path;
}
