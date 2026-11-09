package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The body of the bulk vehicle methods of {@link VehiclesManagerV5}. */
@Data
public class VehicleIdsRequest {
    /** The vehicle IDs ({@code vehicle_ids}). */
    @SerializedName("vehicle_ids")
    private String[] vehicleIds;
}
