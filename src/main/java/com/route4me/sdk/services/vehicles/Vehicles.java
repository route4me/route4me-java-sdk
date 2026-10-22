/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.vehicles;
import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;
/** A vehicle, as the older vehicle API of {@link VehiclesManager} takes and returns it. */
@Data
public class Vehicles {


    /** Vehicle makes; {@code toString()} and {@link #getValue()} give the name the API uses. */
    public enum VehicleMake {

        /** American Coleman. */
        AMERICAN_COLEMAN("American Coleman"),
        /** BMW. */
        BMW("BMW"),
        /** Chevrolet. */
        CHEVROLET("Chevrolet"),
        /** Ford. */
        FORD("Ford"),
        /** Freightliner. */
        FREIGHTLINER("Freightliner"),
        /** GMC. */
        GMC("GMC"),
        /** Hino. */
        HINO("Hino"),
        /** Honda. */
        HONDA("Honda"),
        /** Isuzu. */
        ISUZU("Isuzu"),
        /** Kenworth. */
        KENWORTH("Kenworth"),
        /** Mack. */
        MACK("Mack"),
        /** Mercedes-Benz. */
        MERCEDES_BENZ("Mercedes-Benz"),
        /** Mitsubishi. */
        MITSUBISHI("Mitsubishi"),
        /** Navistar. */
        NAVISTAR("Navistar"),
        /** Nissan. */
        NISSAN("Nissan"),
        /** Peterbilt. */
        PETERBILT("Peterbilt"),
        /** Renault. */
        RENAULT("Renault"),
        /** Scania. */
        SCANIA("Scania"),
        /** Sterling. */
        STERLING("Sterling"),
        /** Toyota. */
        TOYOTA("Toyota"),
        /** Volvo. */
        VOLVO("Volvo"),
        /** Western Star. */
        WESTERN_STAR("Western Star");

        private final String value;

        VehicleMake(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        /**
         * Returns the name of the make.
         *
         * @return the name, such as {@code Ford}
         */
        public String getValue() {
            return value;
        }


    }

    /** The vehicle ID ({@code vehicle_id}). */
    @SerializedName("vehicle_id")
    @QueryParameter("vehicle_id")
    private String vehicleId;
    /** The member ID ({@code member_id}). */
    @SerializedName("member_id")
    private String memberId;
    /** Specifies if the vehicle is marked as deleted ({@code is_deleted}). */
    @SerializedName("is_deleted")
    private boolean isDeleted;
    /** Vehicle alias ({@code vehicle_alias}). */
    @SerializedName("vehicle_alias")
    private String vehicleAlias;
    /** Vehicle VIN ({@code vehicle_vin}). */
    @SerializedName("vehicle_vin")
    private String vehicleVin;
    /** Vehicle registration state ID ({@code vehicle_reg_state_id}). */
    @SerializedName("vehicle_reg_state_id")
    private String vehicleRegStateId;
    /** Vehicle registration country ID ({@code vehicle_reg_country_id}). */
    @SerializedName("vehicle_reg_country_id")
    private String vehicleRegCountryId;
    /** A license plate of the vehicle ({@code vehicle_license_plate}). */
    @SerializedName("vehicle_license_plate")
    private String vehicleLicensePlate;
    /** The type of the vehicle, such as {@code sedan}, {@code van} or {@code 18wheeler} ({@code vehicle_type_id}). */
    @SerializedName("vehicle_type_id")
    private String vehicleTypeId;
    /** When the vehicle was added ({@code timestamp_added}). */
    @SerializedName("timestamp_added")
    private String timestampAdded;
    /** The make of the vehicle, such as {@code ford} ({@code vehicle_make}). */
    @SerializedName("vehicle_make")
    private String vehicleMake;
    /** Vehicle model year ({@code vehicle_model_year}). */
    @SerializedName("vehicle_model_year")
    private Integer vehicleModelYear;
    /** Vehicle model ({@code vehicle_model}). */
    @SerializedName("vehicle_model")
    private String vehicleModel;
    /** The year the vehicle was acquired ({@code vehicle_year_acquired}). */
    @SerializedName("vehicle_year_acquired")
    private Integer vehicleYearAcquired;
    /** A cost of the new vehicle ({@code vehicle_cost_new}). */
    @SerializedName("vehicle_cost_new")
    private Long vehicleCostNew;
    /** If true, the vehicle was purchased new ({@code purchased_new}). */
    @SerializedName("purchased_new")
    private Integer purshasedNew;
    /** Start date of the license ({@code license_start_date}). */
    @SerializedName("license_start_date")
    private String licenseStartDate;
    /** End date of the license ({@code license_end_date}). */
    @SerializedName("license_end_date")
    private String licenseEndDate;
    /** Whether the vehicle is operational ({@code is_operational}). */
    @SerializedName("is_operational")
    private boolean isOperational;
    /** The fuel type, such as {@code diesel} or {@code electric} ({@code fuel_type}). */
    @SerializedName("fuel_type")
    private String fuelType;
    /** External telematics vehicle ID ({@code external_telematics_vehicle_id}). */
    @SerializedName("external_telematics_vehicle_id")
    private String externalTelematicsVehicleId;
    /** When the vehicle was removed ({@code timestamp_removed}). */
    @SerializedName("timestamp_removed")
    private String timestampRemoved;
    /** Vehicle profile ID ({@code vehicle_profile_id}). */
    @SerializedName("vehicle_profile_id")
    private String vehicleProfileId;
    /** Fuel consumption city ({@code fuel_consumption_city}). */
    @SerializedName("fuel_consumption_city")
    private String fuelConsumptionCity;
    /** Fuel consumption in the highway area ({@code fuel_consumption_highway}). */
    @SerializedName("fuel_consumption_highway")
    private String fuelConsumptionHighway;
    /** Fuel consumption unit in the city area (e.g. mpg) ({@code fuel_consumption_city_unit}). */
    @SerializedName("fuel_consumption_city_unit")
    private String fuelConsumptionCityUnit;
    /** Fuel consumption unit in the highway area (e.g. mpg) ({@code fuel_consumption_highway_unit}). */
    @SerializedName("fuel_consumption_highway_unit")
    private String fuelConsumptionHighwayUnit;
    /** Miles per gallon in the city area ({@code mpg_city}). */
    @SerializedName("mpg_city")
    private String mpgCity;
    /** Miles per gallon in the highway area ({@code mpg_highway}). */
    @SerializedName("mpg_highway")
    private String mpgHighway;
    /** Fuel consumption city (UF value, e.g. "20.01 mi/l") ({@code fuel_consumption_city_uf_value}). */
    @SerializedName("fuel_consumption_city_uf_value")
    private String fuelConsumptionCityUFValue;
    /** Fuel consumption highway (UF value, e.g. "2,000.01 mpg") ({@code fuel_consumption_highway_uf_value}). */
    @SerializedName("fuel_consumption_highway_uf_value")
    private String fuelConsumptionHighwayUFValue;


}
