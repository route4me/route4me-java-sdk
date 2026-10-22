package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The {@code data} object of a {@link VehicleResponse}. */
@Data
public class VehicleResponseData {
    /** The vehicle ({@code vehicle}). */
    @SerializedName("vehicle")
    private VehicleV5 vehicle;
}
