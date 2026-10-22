package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of {@link VehiclesManagerV5#getVehicleByLicensePlate(String)}. */
@Data
public class VehicleResponse {
    /** Vehicle data ({@code data}). */
    @SerializedName("data")
    private VehicleV5 data;
}
