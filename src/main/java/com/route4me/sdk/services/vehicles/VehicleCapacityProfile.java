package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class VehicleCapacityProfile {
    @SerializedName("vehicle_capacity_profile_id")
    private Integer vehicleCapacityProfileId;
    @SerializedName("name")
    private String name;
    @SerializedName("max_volume")
    private Double maxVolume;
    @SerializedName("max_weight")
    private Double maxWeight;
    @SerializedName("max_items_number")
    private Integer maxItemsNumber;
    @SerializedName("max_revenue")
    private Double maxRevenue;
}
