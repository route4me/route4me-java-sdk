package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of {@link VehiclesManagerV5#executeVehicleOrder(VehicleOrderRequest)}. */
@Data
public class VehicleOrderResponse {
    /** The vehicle, a 32-character hex ID ({@code vehicle_id}). */
    @SerializedName("vehicle_id")
    private String vehicleId;
    /** The executed order ({@code order_id}). */
    @SerializedName("order_id")
    private Long orderId;
}
