package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The answer of {@link VehiclesManagerV5#getVehicleLocations(String[])}. */
@Data
public class VehicleLocationResponse {
    /** An array of the vehicle locations ({@code data}). */
    @SerializedName("data")
    private List<VehicleLocation> data;
}
