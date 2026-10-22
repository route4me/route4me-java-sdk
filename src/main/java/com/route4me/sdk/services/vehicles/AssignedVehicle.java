package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A vehicle assigned to a member by {@link VehiclesManagerV5#createTemporaryVehicle(VehicleTemporaryRequest)}. */
@Data
public class AssignedVehicle {
    /** The vehicle, a 32-character hex ID ({@code vehicle_id}). */
    @SerializedName("vehicle_id")
    private String vehicleId;
    /** When the assignment ends ({@code expires_at}). */
    @SerializedName("expires_at")
    private String expiresAt;
    /** The member the vehicle is assigned to ({@code assigned_member_id}). */
    @SerializedName("assigned_member_id")
    private Long assignedMemberId;
}
