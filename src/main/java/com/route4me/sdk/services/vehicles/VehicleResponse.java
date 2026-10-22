package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class VehicleResponse {
    @SerializedName("data")
    private VehicleV5 data;
}
