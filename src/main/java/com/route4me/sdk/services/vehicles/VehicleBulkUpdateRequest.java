package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The body of {@link VehiclesManagerV5#updateVehicles(List)}. */
@Data
public class VehicleBulkUpdateRequest {
    /** The vehicles, each with its {@code vehicle_id} and the fields to change ({@code vehicles}). */
    @SerializedName("vehicles")
    private List<VehicleV5> vehicles;
}
