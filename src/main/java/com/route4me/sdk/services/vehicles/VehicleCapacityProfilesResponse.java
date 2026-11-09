package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The answer of {@link VehiclesManagerV5#getVehicleCapacityProfiles(Integer, Integer)}. */
@Data
public class VehicleCapacityProfilesResponse {
    /** The vehicle capacity profiles of the page ({@code data}). */
    @SerializedName("data")
    private List<VehicleCapacityProfile> data;
    /** The paging data ({@code meta}). */
    @SerializedName("meta")
    private VehiclePageMeta meta;
}
