package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A vehicle profile: the size, weight and fuel data the routing uses for a kind of vehicle. */
@Data
public class VehicleProfile {
    /** Vehicle profile ID ({@code vehicle_profile_id}). */
    @SerializedName("vehicle_profile_id")
    private Integer vehicleProfileId;
    /** Vehicle profile name ({@code name}). */
    @SerializedName("name")
    private String name;
    /** Vehicle height ({@code height}). */
    @SerializedName("height")
    private Double height;
    /** Vehicle width ({@code width}). */
    @SerializedName("width")
    private Double width;
    /** Vehicle length ({@code length}). */
    @SerializedName("length")
    private Double length;
    /** Vehicle weight ({@code weight}). */
    @SerializedName("weight")
    private Double weight;
    /** The maximum weight per axle ({@code max_weight_per_axle}). */
    @SerializedName("max_weight_per_axle")
    private Double maxWeightPerAxle;
    /** If true, the profile is predefined ({@code is_predefined}). */
    @SerializedName("is_predefined")
    private Boolean isPredefined;
    /** If true, the profile is default ({@code is_default}). */
    @SerializedName("is_default")
    private Boolean isDefault;
}
