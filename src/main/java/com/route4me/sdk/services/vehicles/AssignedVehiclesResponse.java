package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The answer of {@link VehiclesManagerV5#createTemporaryVehicle(VehicleTemporaryRequest)}. */
@Data
public class AssignedVehiclesResponse {
    /** The assignments ({@code data}). */
    @SerializedName("data")
    private List<AssignedVehicle> data;
}
