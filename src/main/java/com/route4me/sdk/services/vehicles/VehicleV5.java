package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A vehicle, as the V5 vehicle API takes and returns it. */
@Data
public class VehicleV5 {
    /** Unique ID of a vehicle ({@code vehicle_id}). */
    @SerializedName("vehicle_id")
    private String vehicleId;
    /** Unique ID of a member ({@code member_id}). */
    @SerializedName("member_id")
    private Long memberId;
    /** Specifies if the vehicle is marked as deleted ({@code is_deleted}). */
    @SerializedName("is_deleted")
    private Boolean isDeleted;
    /** Vehicle alias ({@code vehicle_alias}). */
    @SerializedName("vehicle_alias")
    private String vehicleAlias;
    /** Vehicle VIN (vehicle identification number) ({@code vehicle_vin}). */
    @SerializedName("vehicle_vin")
    private String vehicleVin;
    /** Vehicle registration state ID ({@code vehicle_reg_state_id}). */
    @SerializedName("vehicle_reg_state_id")
    private Integer vehicleRegStateId;
    /** Vehicle registration country ID ({@code vehicle_reg_country_id}). */
    @SerializedName("vehicle_reg_country_id")
    private Integer vehicleRegCountryId;
    /** Vehicle registration country ID ({@code vehicle_license_plate}). */
    @SerializedName("vehicle_license_plate")
    private String vehicleLicensePlate;
    /** The type of the vehicle, such as {@code sedan}, {@code van} or {@code 18wheeler} ({@code vehicle_type_id}). */
    @SerializedName("vehicle_type_id")
    private String vehicleTypeId;
    /** The make of the vehicle, such as {@code Ford} ({@code vehicle_make}). */
    @SerializedName("vehicle_make")
    private String vehicleMake;
    /** A model of the vehicle ({@code vehicle_model}). */
    @SerializedName("vehicle_model")
    private String vehicleModel;
    /** A year of the vehicle model ({@code vehicle_model_year}). */
    @SerializedName("vehicle_model_year")
    private Integer vehicleModelYear;
    /** A year the vehicle was acquired ({@code vehicle_year_acquired}). */
    @SerializedName("vehicle_year_acquired")
    private Integer vehicleYearAcquired;
    /** A cost of the new vehicle ({@code vehicle_cost_new}). */
    @SerializedName("vehicle_cost_new")
    private Double vehicleCostNew;
    /** When the vehicle was added ({@code timestamp_added}). */
    @SerializedName("timestamp_added")
    private String timestampAdded;
    /**
     * A type of the fuel. Available values: 'unleaded 87', 'unleaded 89', 'unleaded 91', 'unleaded 93', 'diesel',
     * 'electric', 'hybrid' ({@code fuel_type}).
     */
    @SerializedName("fuel_type")
    private String fuelType;
    /** External telematics vehicle IDs ({@code external_telematics_vehicle_ids}). */
    @SerializedName("external_telematics_vehicle_ids")
    private Long externalTelematicsVehicleIds;
    /** When the vehicle was removed ({@code timestamp_removed}). */
    @SerializedName("timestamp_removed")
    private String timestampRemoved;
    /** Vehicle profile ID ({@code vehicle_profile_id}). */
    @SerializedName("vehicle_profile_id")
    private Integer vehicleProfileId;
    /** Vehicle capacity profile ID ({@code vehicle_capacity_profile_id}). */
    @SerializedName("vehicle_capacity_profile_id")
    private Integer vehicleCapacityProfileId;
    /**
     * The telematics connection the vehicle comes from ({@code r4m_telematics_gateway_connection_id}); used by
     * {@link VehiclesManagerV5#syncPendingTelematicsData(VehicleV5)}.
     */
    @SerializedName("r4m_telematics_gateway_connection_id")
    private Long r4mTelematicsGatewayConnectionId;
    /**
     * The vehicle on the telematics connection ({@code r4m_telematics_gateway_vehicle_id}); used by
     * {@link VehiclesManagerV5#syncPendingTelematicsData(VehicleV5)}.
     */
    @SerializedName("r4m_telematics_gateway_vehicle_id")
    private Long r4mTelematicsGatewayVehicleId;
    /** Fuel consumption city ({@code fuel_consumption_city}). */
    @SerializedName("fuel_consumption_city")
    private Double fuelConsumptionCity;
    /** Fuel consumption in the highway area ({@code fuel_consumption_highway}). */
    @SerializedName("fuel_consumption_highway")
    private Double fuelConsumptionHighway;
    /** Fuel consumption unit in the city area (e.g. mpg) ({@code fuel_consumption_city_unit}). */
    @SerializedName("fuel_consumption_city_unit")
    private String fuelConsumptionCityUnit;
    /** Fuel consumption unit in the highway area (e.g. mpg) ({@code fuel_consumption_highway_unit}). */
    @SerializedName("fuel_consumption_highway_unit")
    private String fuelConsumptionHighwayUnit;
    /** Miles per gallon in the city area ({@code mpg_city}). */
    @SerializedName("mpg_city")
    private Double mpgCity;
    /** Miles per gallon in the highway area ({@code mpg_highway}). */
    @SerializedName("mpg_highway")
    private Double mpgHighway;
}
