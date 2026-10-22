package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * The body of {@link VehiclesManagerV5#executeVehicleOrder(VehicleOrderRequest)}: the vehicle and the location of
 * the order. All fields are required.
 */
@Data
@Accessors(chain = true)
public class VehicleOrderRequest {
    /** The vehicle, a 32-character hex ID ({@code vehicle_id}). */
    @SerializedName("vehicle_id")
    private String vehicleId;
    /** The latitude of the location ({@code lat}). */
    @SerializedName("lat")
    private Double lat;
    /** The longitude of the location ({@code lng}). */
    @SerializedName("lng")
    private Double lng;
}
