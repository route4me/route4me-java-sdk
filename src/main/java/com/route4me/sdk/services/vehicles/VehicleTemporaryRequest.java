package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * The body of {@link VehiclesManagerV5#createTemporaryVehicle(VehicleTemporaryRequest)}: a vehicle assigned to a
 * member until a given time. {@code vehicleId}, {@code assignedMemberId} and {@code expiresAt} are required.
 */
@Data
@Accessors(chain = true)
public class VehicleTemporaryRequest {
    /** The vehicle, a 32-character hex ID ({@code vehicle_id}). Required. */
    @SerializedName("vehicle_id")
    private String vehicleId;
    /** The license plate of the vehicle, at most 7 characters ({@code vehicle_license_plate}). */
    @SerializedName("vehicle_license_plate")
    private String vehicleLicensePlate;
    /** The member the vehicle is assigned to ({@code assigned_member_id}). Required. */
    @SerializedName("assigned_member_id")
    private Long assignedMemberId;
    /** When the assignment ends, as a Unix timestamp in seconds ({@code expires_at}). Required. */
    @SerializedName("expires_at")
    private Long expiresAt;
    /** Whether the vehicle accepts new tasks ({@code accepting_new_tasks}). */
    @SerializedName("accepting_new_tasks")
    private Boolean acceptingNewTasks;
    /** {@code true} to force the assignment ({@code force-assignment}). */
    @SerializedName("force-assignment")
    private Boolean forceAssignment;
}
