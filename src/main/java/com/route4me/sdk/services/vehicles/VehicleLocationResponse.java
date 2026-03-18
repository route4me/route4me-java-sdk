package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class VehicleLocationResponse {
    @SerializedName("data")
    private List<VehicleLocation> data;
}
