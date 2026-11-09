package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of the single capacity-profile methods of {@link VehiclesManagerV5}. */
@Data
public class VehicleCapacityProfileResponse {
    /** The data object containing a vehicle capacity profile ({@code data}). */
    @SerializedName("data")
    private VehicleCapacityProfile data;
}
