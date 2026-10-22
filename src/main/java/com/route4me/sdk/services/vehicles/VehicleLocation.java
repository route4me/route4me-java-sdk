package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The last known position of a vehicle. */
@Data
public class VehicleLocation {
    /** The vehicle ID ({@code vehicle_id}). */
    @SerializedName("vehicle_id")
    private String vehicleId;
    /** Latitude of a vehicle position ({@code lat}). */
    @SerializedName("lat")
    private Double lat;
    /** Longitude of a vehicle position ({@code lng}). */
    @SerializedName("lng")
    private Double lng;
    /** When the last position was received, such as {@code 2026-10-21 10:00:00} ({@code last_position_received}). */
    @SerializedName("last_position_received")
    private String lastPositionReceived;
}
