package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class VehicleLocation {
    @SerializedName("vehicle_id")
    private String vehicleId;
    @SerializedName("lat")
    private Double lat;
    @SerializedName("lng")
    private Double lng;
    @SerializedName("last_position_received")
    private String lastPositionReceived;
}
