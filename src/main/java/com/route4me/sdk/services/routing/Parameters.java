package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import com.route4me.sdk.services.routing.advancedconstraints.AdvancedConstraints;
import com.route4me.sdk.services.routing.balance.Balance;
import com.route4me.sdk.services.routing.override.addresses.OverrideAddresses;
import java.util.List;
import lombok.Data;

@Data
public class Parameters {

    @SerializedName("is_upload")
    private String isUpload;
    @SerializedName("rt")
    private Boolean rt;
    @SerializedName("share_route")
    private Boolean shareRoute;
    @SerializedName("route_name")
    private String routeName;
    @SerializedName("route_date")
    private Number routeDate;
    @SerializedName("route_time")
    private Number routeTime;
    @SerializedName("shared_publicly")
    private String sharedPublicly;
    @SerializedName("disable_optimization")
    private Boolean disableOptimization;
    @SerializedName("optimize")
    private String optimize;
    @SerializedName("lock_last")
    private Boolean lockLast;
    @QueryParameter("ignore_tw")   
    @SerializedName("ignore_tw")
    private Boolean ignoreTW;
    @SerializedName("vehicle_capacity")
    private Integer vehicleCapacity;
    @SerializedName("vehicle_max_cargo_weight")
    private Double vehicleMaxCargoWeight;
    @SerializedName("vehicle_max_cargo_volume")
    private Double vehicleMaxCargoVolume;
    @SerializedName("vehicle_max_distance_mi")
    private String vehicleMaxDistanceMi;
    @SerializedName("distance_unit")
    private String distanceUnit;
    @SerializedName("travel_mode")
    private String travelMode;
    @SerializedName("avoid")
    private String avoid;
    @SerializedName("vehicle_id")
    private String vehicleId;
    @SerializedName("driver_id")
    private String driverId;
    @SerializedName("route_max_duration")
    private Number routeMaxDuration;
    @SerializedName("remote_ip")
    private Number remoteIp;
    @SerializedName("route_email")
    private String routeEmail;
    @SerializedName("route_type")
    private String routeType;
    @SerializedName("store_route")
    private Boolean storeRoute;
    @SerializedName("metric")
    private Number metric;
    @SerializedName("algorithm_type")
    private String algorithmType;
    @SerializedName("member_id")
    private String memberId;
    @SerializedName("ip")
    private String ip;
    @SerializedName("device_id")
    private String deviceId;
    @SerializedName("device_type")
    private String deviceType;
    @SerializedName("has_trailer")
    private Boolean hasTrailer;
    @SerializedName("parts")
    private Number parts;
    @SerializedName("parts_min")
    private Number partsMin;
    @SerializedName("trailer_weight_t")
    private Number trailerWeightT;
    @SerializedName("limited_weight_t")
    private Number limitedWeightT;
    @SerializedName("weight_per_axle_t")
    private Number weightPerAxleT;
    @SerializedName("truck_height_meters")
    private Number truckHeightMeters;
    @SerializedName("truck_width_meters")
    private Number truckWidthMeters;
    @SerializedName("truck_length_meters")
    private Number truckLengthMeters;
    @SerializedName("min_tour_size")
    private Number minTourSize;
    @SerializedName("max_tour_size")
    private Number maxTourSize;
    @SerializedName("dm")
    private String dm;
    @SerializedName("dirm")
    private String dirm;
    @QueryParameter("bundling")
    @SerializedName("bundling")
    private Bundling bundling;
    @QueryParameter("slowdowns")
    @SerializedName("slowdowns")
    private SlowDowns slowdowns;
    @QueryParameter("uturn")
    @SerializedName("uturn")
    private Integer uTurn;
    @QueryParameter("leftturn")
    @SerializedName("leftturn")
    private Integer leftTurn;
    @QueryParameter("rightturn")
    @SerializedName("rightturn")
    private Integer rightTurn;
    @QueryParameter("advanced_constraints")
    @SerializedName("advanced_constraints")
    private List<AdvancedConstraints> advancedConstraints;
    @QueryParameter("override_addresses")
    @SerializedName("override_addresses")
    private OverrideAddresses overrideAddresses;
    @SerializedName("is_dynamic_start_time")
    private Boolean isDynamicStartTime;
    @SerializedName("depots")
    private List<Object> depots;
    @QueryParameter("use_mixed_pickup_delivery_demands")
    @SerializedName("use_mixed_pickup_delivery_demands")
    private boolean useMixedPickupDeliveryDemands;
    @QueryParameter("balance")
    @SerializedName("balance")
    private Balance balance;
    @QueryParameter("target_duration")
    @SerializedName("target_duration")
    private Double targetDuration;
    @QueryParameter("target_distance")
    @SerializedName("target_distance")
    private Double targetDistance;
    @QueryParameter("target_wait_by_tail_size")
    @SerializedName("target_wait_by_tail_size")
    private Double targetWaitByTailSize;
    @QueryParameter("dev_lat")
    @SerializedName("dev_lat")
    private Double devLat;
    @QueryParameter("dev_lng")
    @SerializedName("dev_lng")
    private Double devLng;
    @QueryParameter("avoidance_zones")
    @SerializedName("avoidance_zones")
    private List<String> avoidanceZones;
    @QueryParameter("subtour_max_revenue")
    @SerializedName("subtour_max_revenue")
    private Integer subtourMaxRevenue;
    
    
    public void isUseMixedPickupDeliveryDemands(boolean useMixedPickupDeliveryDemands) {
        this.useMixedPickupDeliveryDemands = useMixedPickupDeliveryDemands;
    }

    /**
     * @param vehicleCapacity the vehicleCapacity to set
     */
    public void setVehicleCapacity(Integer vehicleCapacity) {
        this.vehicleCapacity = vehicleCapacity;
    }

    /**
     * @param vehicleCapacity the vehicleCapacity to set
     */
    public void setVehicleCapacity(String vehicleCapacity) {
        this.vehicleCapacity = Integer.parseInt(vehicleCapacity);
    }
    

}
