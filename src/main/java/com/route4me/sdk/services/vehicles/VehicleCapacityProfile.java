package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A vehicle capacity profile: how much a vehicle can carry. */
@Data
public class VehicleCapacityProfile {
    /** Vehicle capacity profile ID ({@code vehicle_capacity_profile_id}). */
    @SerializedName("vehicle_capacity_profile_id")
    private Integer vehicleCapacityProfileId;
    /** Vehicle capacity profile name ({@code name}). */
    @SerializedName("name")
    private String name;
    /** A maximum cargo volume the vehicle can carry ({@code max_volume}). */
    @SerializedName("max_volume")
    private Double maxVolume;
    /** The maximum weight a vehicle can carry ({@code max_weight}). */
    @SerializedName("max_weight")
    private Double maxWeight;
    /** The maximum number of items a vehicle can carry ({@code max_items_number}). */
    @SerializedName("max_items_number")
    private Integer maxItemsNumber;
    /** The maximum revenue an owner company can gain from a vehicle ({@code max_revenue}). */
    @SerializedName("max_revenue")
    private Double maxRevenue;
}
