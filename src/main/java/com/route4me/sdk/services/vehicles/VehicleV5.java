package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class VehicleV5 {
    @SerializedName("vehicle_id")
    private String vehicleId;
    @SerializedName("member_id")
    private Long memberId;
    @SerializedName("is_deleted")
    private Boolean isDeleted;
    @SerializedName("vehicle_alias")
    private String vehicleAlias;
    @SerializedName("vehicle_vin")
    private String vehicleVin;
    @SerializedName("vehicle_reg_state_id")
    private Integer vehicleRegStateId;
    @SerializedName("vehicle_reg_country_id")
    private Integer vehicleRegCountryId;
    @SerializedName("vehicle_license_plate")
    private String vehicleLicensePlate;
    @SerializedName("vehicle_type_id")
    private String vehicleTypeId;
    @SerializedName("vehicle_make")
    private String vehicleMake;
    @SerializedName("vehicle_model")
    private String vehicleModel;
    @SerializedName("vehicle_model_year")
    private Integer vehicleModelYear;
    @SerializedName("vehicle_year_acquired")
    private Integer vehicleYearAcquired;
    @SerializedName("vehicle_cost_new")
    private Double vehicleCostNew;
    @SerializedName("timestamp_added")
    private String timestampAdded;
    @SerializedName("fuel_type")
    private String fuelType;
    @SerializedName("external_telematics_vehicle_ids")
    private Long externalTelematicsVehicleIds;
    @SerializedName("timestamp_removed")
    private String timestampRemoved;
    @SerializedName("vehicle_profile_id")
    private Integer vehicleProfileId;
    @SerializedName("fuel_consumption_city")
    private Double fuelConsumptionCity;
    @SerializedName("fuel_consumption_highway")
    private Double fuelConsumptionHighway;
    @SerializedName("fuel_consumption_city_unit")
    private String fuelConsumptionCityUnit;
    @SerializedName("fuel_consumption_highway_unit")
    private String fuelConsumptionHighwayUnit;
    @SerializedName("mpg_city")
    private Double mpgCity;
    @SerializedName("mpg_highway")
    private Double mpgHighway;
}
