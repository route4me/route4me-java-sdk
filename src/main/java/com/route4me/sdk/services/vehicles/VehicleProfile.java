package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class VehicleProfile {
    @SerializedName("vehicle_profile_id")
    private Integer vehicleProfileId;
    @SerializedName("name")
    private String name;
    @SerializedName("height")
    private Double height;
    @SerializedName("width")
    private Double width;
    @SerializedName("length")
    private Double length;
    @SerializedName("weight")
    private Double weight;
    @SerializedName("max_weight_per_axle")
    private Double maxWeightPerAxle;
    @SerializedName("is_predefined")
    private Boolean isPredefined;
    @SerializedName("is_default")
    private Boolean isDefault;
}
