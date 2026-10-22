package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The answer of {@link VehiclesManagerV5#getVehicleProfiles(Integer, Integer)}. */
@Data
public class VehicleProfilesResponse {
    /** An array of the vehicle profiles ({@code data}). */
    @SerializedName("data")
    private List<VehicleProfile> data;
}
