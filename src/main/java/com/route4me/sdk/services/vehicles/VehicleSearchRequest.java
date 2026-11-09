package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * The body of {@link VehiclesManagerV5#searchVehicles(VehicleSearchRequest)}: which vehicles to look for, and the
 * location to measure their distance from. All fields are required.
 */
@Data
@Accessors(chain = true)
public class VehicleSearchRequest {
    /** The vehicles to look for, as 32-character hex IDs ({@code vehicle_ids}). */
    @SerializedName("vehicle_ids")
    private List<String> vehicleIds;
    /** The latitude of the location ({@code lat}). */
    @SerializedName("lat")
    private Double lat;
    /** The longitude of the location ({@code lng}). */
    @SerializedName("lng")
    private Double lng;
}
