package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import com.route4me.sdk.services.routing.advancedconstraints.AdvancedConstraints;
import com.route4me.sdk.services.routing.balance.Balance;
import com.route4me.sdk.services.routing.override.addresses.OverrideAddresses;
import java.util.List;
import lombok.Data;

/**
 * The route parameters of an optimization or a route ({@code parameters}): the algorithm, the fleet, the times, the
 * vehicle and what to optimize for. Fields left {@code null} are not sent.
 */
@Data
public class Parameters {

    /**
     * Let the R4M API know if this SDK request is comming from a file upload within your environment (for analytics)
     * ({@code is_upload}).
     */
    @SerializedName("is_upload")
    private String isUpload;
    /**
     * The tour type of this route. rt is short for round trip, the optimization engine changes its behavior for round
     * trip routes ({@code rt}).
     */
    @SerializedName("rt")
    private Boolean rt;
    /** Whether to share the route by email with the member ({@code share_route}). */
    @SerializedName("share_route")
    private Boolean shareRoute;
    /**
     * The name of this route. this route name will be accessible in the search API, and also will be displayed on the
     * mobile device of a user ({@code route_name}).
     */
    @SerializedName("route_name")
    private String routeName;
    /**
     * The route start date in UTC, unix timestamp seconds. Used to show users when the route will begin, also used for
     * reporting and analytics ({@code route_date}).
     */
    @SerializedName("route_date")
    private Number routeDate;
    /** Offset in seconds relative to the route start date (i.e. 9AM would be 60 * 60 * 9) ({@code route_time}). */
    @SerializedName("route_time")
    private Number routeTime;
    /** Specify if the route can be viewed by unauthenticated users ({@code shared_publicly}). */
    @SerializedName("shared_publicly")
    private String sharedPublicly;
    /**
     * By disabling optimization, the route optimization engine will not resequence the stops in your ({@code
     * disable_optimization}).
     */
    @SerializedName("disable_optimization")
    private Boolean disableOptimization;
    /** The optimize parameter. Availabale values: Distance , Time , timeWithTraffic ({@code optimize}). */
    @SerializedName("optimize")
    private String optimize;
    /**
     * When the tour type is not round trip (rt = false), enable lock last so that the final destination is fixed.
     * Example: driver leaves a depot, but must always arrive at home (or a specific gas station) at the end of the
     * route ({@code lock_last}).
     */
    @SerializedName("lock_last")
    private Boolean lockLast;
    /** If true, the time windows ignored ({@code ignore_tw}). */
    @QueryParameter("ignore_tw")
    @SerializedName("ignore_tw")
    private Boolean ignoreTW;
    /** Vehicle capacity. How much cargo can the vehicle carry (units, e.g. cubic meters) ({@code vehicle_capacity}). */
    @SerializedName("vehicle_capacity")
    private Integer vehicleCapacity;
    /** Maximum cargo weight a vehicle can carry ({@code vehicle_max_cargo_weight}). */
    @SerializedName("vehicle_max_cargo_weight")
    private Double vehicleMaxCargoWeight;
    /** Maximum cargo volume a vehicle can carry ({@code vehicle_max_cargo_volume}). */
    @SerializedName("vehicle_max_cargo_volume")
    private Double vehicleMaxCargoVolume;
    /** Maximum distance for a single vehicle in the route (always in miles) ({@code vehicle_max_distance_mi}). */
    @SerializedName("vehicle_max_distance_mi")
    private String vehicleMaxDistanceMi;
    /** The distance measurement unit for the route ({@code distance_unit}). */
    @SerializedName("distance_unit")
    private String distanceUnit;
    /**
     * The mode of travel that the directions should be optimized for. Available values: Driving, Walking, Trucking
     * ({@code travel_mode}).
     */
    @SerializedName("travel_mode")
    private String travelMode;
    /**
     * Options which let the user choose which road obstacles to avoid. This has no impact on route sequencing.
     * Available values: - Highways - Tolls - minimizeHighways - minimizeTolls - highways,tolls - "" ({@code avoid}).
     */
    @SerializedName("avoid")
    private String avoid;
    /** The unique internal id of a vehicle to assign to the route ({@code vehicle_id}). */
    @SerializedName("vehicle_id")
    private String vehicleId;
    /** The ID of the driver to assign to the route; the API prefers {@code member_id} ({@code driver_id}). */
    @SerializedName("driver_id")
    private String driverId;
    /**
     * When using a multiple driver algorithm, this is the maximum permissible duration of a generated route. The
     * optimization system will automatically create more routes when the route_max_duration is exceeded for a route.
     * However it will create an 'unrouted' list of addresses if the maximum number of drivers is exceeded ({@code
     * route_max_duration}).
     */
    @SerializedName("route_max_duration")
    private Number routeMaxDuration;
    /** The IP address of the client ({@code remote_ip}). */
    @SerializedName("remote_ip")
    private Number remoteIp;
    /** The email address to notify upon completion of an optimization request ({@code route_email}). */
    @SerializedName("route_email")
    private String routeEmail;
    /** The type of the route ({@code route_type}). */
    @SerializedName("route_type")
    private String routeType;
    /** Whether to save the route; the API always saves it ({@code store_route}). */
    @SerializedName("store_route")
    private Boolean storeRoute;
    /** How distances between stops are measured; see {@code Constants.Metric} ({@code metric}). */
    @SerializedName("metric")
    private Number metric;
    /** The optimization algorithm; see {@code Constants.AlgorithmType} ({@code algorithm_type}). */
    @SerializedName("algorithm_type")
    private String algorithmType;
    /**
     * The route owner's member ID. In order for users in your organization to have routes assigned to them, you must
     * provide their member ID within the Route4Me system. A list of member IDs can be retrieved with view_users API
     * method ({@code member_id}).
     */
    @SerializedName("member_id")
    private String memberId;
    /** Specify the ip address of the remote user making this optimization request ({@code ip}). */
    @SerializedName("ip")
    private String ip;
    /** 32 Character MD5 String ID of the device that was used to plan this route ({@code device_id}). */
    @SerializedName("device_id")
    private String deviceId;
    /**
     * The type of device making this request. Available values: web , iphone , ipad , android_phone , android_tablet
     * ({@code device_type}).
     */
    @SerializedName("device_type")
    private String deviceType;
    /**
     * If true, the vehicle has a trailer. For routes that have trucking directions enabled, directions generated will
     * ensure compliance so that road directions generated do not take the vehicle where trailers are prohibited ({@code
     * has_trailer}).
     */
    @SerializedName("has_trailer")
    private Boolean hasTrailer;
    /** Legacy feature which permits a user to request an example number of optimized routes ({@code parts}). */
    @SerializedName("parts")
    private Number parts;
    /** Minimum number of optimized routes ({@code parts_min}). */
    @SerializedName("parts_min")
    private Number partsMin;
    /**
     * The vehicle's trailer weight For routes that have trucking directions enabled, directions generated will ensure
     * compliance so that road directions generated do not take the vehicle on roads where the weight of the vehicle in
     * tons exceeds this value ({@code trailer_weight_t}).
     */
    @SerializedName("trailer_weight_t")
    private Number trailerWeightT;
    /** If travel_mode is Trucking, specifies the truck weight ({@code limited_weight_t}). */
    @SerializedName("limited_weight_t")
    private Number limitedWeightT;
    /**
     * The vehicle's weight per axle (tons) For routes that have trucking directions enabled, directions generated will
     * ensure compliance so that road directions generated do not take the vehicle where the weight per axle in tons
     * exceeds this value ({@code weight_per_axle_t}).
     */
    @SerializedName("weight_per_axle_t")
    private Number weightPerAxleT;
    /** The height of the truck, in meters, for truck directions ({@code truck_height_meters}). */
    @SerializedName("truck_height_meters")
    private Number truckHeightMeters;
    /** The width of the truck, in meters, for truck directions ({@code truck_width_meters}). */
    @SerializedName("truck_width_meters")
    private Number truckWidthMeters;
    /** The length of the truck, in meters, for truck directions ({@code truck_length_meters}). */
    @SerializedName("truck_length_meters")
    private Number truckLengthMeters;
    /** The minimum number of stops permitted per created subroute ({@code min_tour_size}). */
    @SerializedName("min_tour_size")
    private Number minTourSize;
    /** The maximum number of stops permitted per created subroute ({@code max_tour_size}). */
    @SerializedName("max_tour_size")
    private Number maxTourSize;
    /**
     * The method that computes the distances between the stops: {@code 1} Route4Me routing (the default), {@code 3}
     * Route4Me traffic engine, {@code 6} trucking; 2, 4 and 5 are deprecated ({@code dm}).
     */
    @SerializedName("dm")
    private String dm;
    /**
     * Directions method. Available values: 1 = DEFAULT (R4M PROPRIETARY INTERNAL NAVIGATION SYSTEM) 2 = DEPRECATED 3 =
     * TRUCKING 4 = DEPRECATED ({@code dirm}).
     */
    @SerializedName("dirm")
    private String dirm;
    /**
     * **POST only.** Bundles stops that share an address, coordinates or field values into one stop before optimizing
     * ({@code bundling}).
     */
    @QueryParameter("bundling")
    @SerializedName("bundling")
    private Bundling bundling;
    /** Slowdown of the optimization parameters ({@code slowdowns}). */
    @QueryParameter("slowdowns")
    @SerializedName("slowdowns")
    private SlowDowns slowdowns;
    /** If equal to 1, uturn is allowed for the vehicle ({@code uturn}). */
    @QueryParameter("uturn")
    @SerializedName("uturn")
    private Integer uTurn;
    /** If equal to 1, leftturn is allowed for the vehicle ({@code leftturn}). */
    @QueryParameter("leftturn")
    @SerializedName("leftturn")
    private Integer leftTurn;
    /** If equal to 1, rightturn is allowed for the vehicle ({@code rightturn}). */
    @QueryParameter("rightturn")
    @SerializedName("rightturn")
    private Integer rightTurn;
    /**
     * **POST only.** Fleet groups, each with its own drivers, vehicles, skills, capacity and working hours. A non-empty
     * list sets `algorithm_type` to 9, which is an account feature ({@code advanced_constraints}).
     */
    @QueryParameter("advanced_constraints")
    @SerializedName("advanced_constraints")
    private List<AdvancedConstraints> advancedConstraints;
    /** Values that replace those of every address of the optimization ({@code override_addresses}). */
    @QueryParameter("override_addresses")
    @SerializedName("override_addresses")
    private OverrideAddresses overrideAddresses;
    /**
     * If true, the route start time is moved so that the driver leaves the depot just in time for the first stop's time
     * window ({@code is_dynamic_start_time}).
     */
    @SerializedName("is_dynamic_start_time")
    private Boolean isDynamicStartTime;
    /**
     * An array of the depots (a mix of the Address and Source object types). You can use this property when you create
     * or update a route with the advanced constraints ({@code depots}).
     */
    @SerializedName("depots")
    private List<Object> depots;
    /** {@code null} (the default) leaves the option out of the request. */
    @QueryParameter("use_mixed_pickup_delivery_demands")
    @SerializedName("use_mixed_pickup_delivery_demands")
    private Boolean useMixedPickupDeliveryDemands;
    /**
     * **POST only.** Balances the routes by their number of stops. It needs room for at least 2 routes per group in
     * `parts` ({@code balance}).
     */
    @QueryParameter("balance")
    @SerializedName("balance")
    private Balance balance;
    /** The parameter specifies fine-tuning of an optimization process by route duration ({@code target_duration}). */
    @QueryParameter("target_duration")
    @SerializedName("target_duration")
    private Double targetDuration;
    /** The parameter specifies fine-tuning of an optimization process by route distance ({@code target_distance}). */
    @QueryParameter("target_distance")
    @SerializedName("target_distance")
    private Double targetDistance;
    /**
     * The parameter specifies fine-tuning of an optimization process by waiting time ({@code
     * target_wait_by_tail_size}).
     */
    @QueryParameter("target_wait_by_tail_size")
    @SerializedName("target_wait_by_tail_size")
    private Double targetWaitByTailSize;
    /** The latitude of the device making this sdk request ({@code dev_lat}). */
    @QueryParameter("dev_lat")
    @SerializedName("dev_lat")
    private Double devLat;
    /** The longitude of the device making this sdk request ({@code dev_lng}). */
    @QueryParameter("dev_lng")
    @SerializedName("dev_lng")
    private Double devLng;
    /** An array of the Avoidance zones IDs ({@code avoidance_zones}). */
    @QueryParameter("avoidance_zones")
    @SerializedName("avoidance_zones")
    private List<String> avoidanceZones;
    /** Maximum allowed revenue from a subtour ({@code subtour_max_revenue}). */
    @QueryParameter("subtour_max_revenue")
    @SerializedName("subtour_max_revenue")
    private Integer subtourMaxRevenue;

    /** **POST only.** **Account feature.** Driver breaks ({@code breaks}). */
    @SerializedName("breaks")
    private List<Break> breaks;

    /** Specifies a maximum number of the routes by advanced constraint groups ({@code group_max_routes}). */
    @QueryParameter("group_max_routes")
    @SerializedName("group_max_routes")
    private List<List<Object>> groupMaxRoutes;

    /**
     * Sets {@code use_mixed_pickup_delivery_demands}; the same as
     * {@link #setUseMixedPickupDeliveryDemands(boolean)}.
     *
     * @param value whether pickup and delivery demands may mix
     */
    public void isUseMixedPickupDeliveryDemands(boolean value) {
        this.useMixedPickupDeliveryDemands = value;
    }

    /**
     * Returns whether pickup and delivery demands may mix on a route.
     *
     * @return the value; {@code false} when the option is not set
     */
    public boolean isUseMixedPickupDeliveryDemands() {
        return Boolean.TRUE.equals(this.useMixedPickupDeliveryDemands);
    }

    /**
     * Returns {@code use_mixed_pickup_delivery_demands} as it is sent.
     *
     * @return the value, or {@code null} when it is not set and so not sent
     */
    public Boolean getUseMixedPickupDeliveryDemands() {
        return this.useMixedPickupDeliveryDemands;
    }

    /**
     * Sets whether pickup and delivery demands may mix on a route.
     *
     * @param useMixedPickupDeliveryDemands the value
     */
    public void setUseMixedPickupDeliveryDemands(boolean useMixedPickupDeliveryDemands) {
        this.useMixedPickupDeliveryDemands = useMixedPickupDeliveryDemands;
    }

    /**
     * Sets whether pickup and delivery demands may mix on a route.
     *
     * @param useMixedPickupDeliveryDemands the value; {@code null} leaves the option out of the request
     */
    public void setUseMixedPickupDeliveryDemands(Boolean useMixedPickupDeliveryDemands) {
        this.useMixedPickupDeliveryDemands = useMixedPickupDeliveryDemands;
    }

    /**
     * Sets the capacity of each vehicle ({@code vehicle_capacity}).
     *
     * @param vehicleCapacity the capacity, in the units of the addresses
     */
    public void setVehicleCapacity(Integer vehicleCapacity) {
        this.vehicleCapacity = vehicleCapacity;
    }

    /**
     * Sets the capacity of each vehicle from text ({@code vehicle_capacity}).
     *
     * @param vehicleCapacity the capacity, as a decimal integer
     * @throws NumberFormatException if the text is not an integer
     */
    public void setVehicleCapacity(String vehicleCapacity) {
        this.vehicleCapacity = Integer.parseInt(vehicleCapacity);
    }


}
