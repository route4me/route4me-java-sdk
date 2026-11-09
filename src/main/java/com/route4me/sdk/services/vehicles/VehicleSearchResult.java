package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** One vehicle found by {@link VehiclesManagerV5#searchVehicles(VehicleSearchRequest)}. */
@Data
public class VehicleSearchResult {
    /** The vehicle ({@code vehicle}). */
    @SerializedName("vehicle")
    private VehicleV5 vehicle;
    /** The last known position of the vehicle; only {@code lat} and {@code lng} are set ({@code vehicleLocation}). */
    @SerializedName("vehicleLocation")
    private VehicleLocation vehicleLocation;
    /** The distance between the vehicle and the searched location ({@code distance}). */
    @SerializedName("distance")
    private Double distance;
}
