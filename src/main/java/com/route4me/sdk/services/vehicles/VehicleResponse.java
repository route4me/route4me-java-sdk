package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of {@link VehiclesManagerV5#getVehicleByLicensePlate(String)}. */
@Data
public class VehicleResponse {
    /** The vehicle, under {@code data.vehicle} ({@code data}). */
    @SerializedName("data")
    private VehicleResponseData data;
}
