package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class VehicleIdsRequest {
    @SerializedName("vehicle_ids")
    private String[] vehicleIds;
}
