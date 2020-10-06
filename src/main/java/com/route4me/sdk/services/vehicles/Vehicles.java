/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.vehicles;
import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;
/**
 *
 * @author juan
 */
@Data
public class Vehicles {
    

    public enum VehicleMake {
 
        AMERICAN_COLEMAN("American Coleman"),
        BMW("BMW"),
        CHEVROLET("Chevrolet"),
        FORD("Ford"),
        FREIGHTLINER("Freightliner"),
        GMC("GMC"),
        HINO("Hino"),
        HONDA("Honda"),
        ISUZU("Isuzu"),
        KENWORTH("Kenworth"),
        MACK("Mack"),
        MERCEDES_BENZ("Mercedes-Benz"),
        MITSUBISHI("Mitsubishi"),
        NAVISTAR("Navistar"),
        NISSAN("Nissan"),
        PETERBILT("Peterbilt"),
        RENAULT("Renault"),
        SCANIA("Scania"),
        STERLING("Sterling"),
        TOYOTA("Toyota"),
        VOLVO("Volvo"),
        WESTERN_STAR("Western Star" );
        
        private String value;

        private VehicleMake(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        /**
         * @return the value
         */
        public String getValue() {
            return value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(String value) {
            this.value = value;
        }        
        
        
    }

    @SerializedName("vehicle_id")
    @QueryParameter("vehicle_id")
    private String vehicleId;
    @SerializedName("member_id")
    private String memberId;
    @SerializedName("is_deleted")
    private boolean isDeleted;
    @SerializedName("vehicle_alias")
    private String vehicleAlias;
    @SerializedName("vehicle_vin")
    private String vehicleVin;
    @SerializedName("vehicle_reg_state_id")
    private String vehicleRegStateId;
    @SerializedName("vehicle_reg_country_id")
    private String vehicleRegCountryId;
    @SerializedName("vehicle_license_plate")
    private String vehicleLicensePlate;
    @SerializedName("vehicle_type_id")
    private String vehicleTypeId;
    @SerializedName("timestamp_added")
    private String timestampAdded;
    @SerializedName("vehicle_make")
    private String vehicleMake;
    @SerializedName("vehicle_model_year")
    private Integer vehicleModelYear;
    @SerializedName("vehicle_model")
    private String vehicleModel;
    @SerializedName("vehicle_year_acquired")
    private Integer vehicleYearAcquired;
    @SerializedName("vehicle_cost_new")
    private Long vehicleCostNew;
    @SerializedName("purchased_new")
    Integer purshasedNew;
    @SerializedName("license_start_date")
    private String licenseStartDate; 
    @SerializedName("license_end_date")
    private String licenseEndDate; 
    @SerializedName("is_operational")
    boolean isOperational;
    @SerializedName("fuel_type")
    private String fuelType; 
    @SerializedName("external_telematics_vehicle_id")
    private String externalTelematicsVehicleId; 
    @SerializedName("timestamp_removed")
    private String timestampRemoved; 
    @SerializedName("vehicle_profile_id")
    private String vehicleProfileId; 
    @SerializedName("fuel_consumption_city")
    private String fuelConsumptionCity; 
    @SerializedName("fuel_consumption_highway")
    private String fuelConsumptionHighway; 
    @SerializedName("fuel_consumption_city_unit")
    private String fuelConsumptionCityUnit; 
    @SerializedName("fuel_consumption_highway_unit")
    private String fuelConsumptionHighwayUnit; 
    @SerializedName("mpg_city")
    private String mpgCity; 
    @SerializedName("mpg_highway")
    private String mpgHighway; 
    @SerializedName("fuel_consumption_city_uf_value")
    private String fuelConsumptionCityUFValue; 
    @SerializedName("fuel_consumption_highway_uf_value")
    private String fuelConsumptionHighwayUFValue; 

    
}
