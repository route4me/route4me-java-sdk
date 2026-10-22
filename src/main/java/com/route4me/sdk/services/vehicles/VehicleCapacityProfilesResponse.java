package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class VehicleCapacityProfilesResponse {
    @SerializedName("data")
    private List<VehicleCapacityProfile> data;
}
