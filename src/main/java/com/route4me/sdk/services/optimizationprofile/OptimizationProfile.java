package com.route4me.sdk.services.optimizationprofile;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** An optimization profile, as the API returns it. */
@Data
public class OptimizationProfile {
    /** Optimization profile ID ({@code optimization_profile_id}). */
    @SerializedName("optimization_profile_id")
    private String optimizationProfileId;

    /** Profile name ({@code profile_name}). */
    @SerializedName("profile_name")
    private String profileName;

    /** Is default profile ({@code is_default}). */
    @SerializedName("is_default")
    private Boolean isDefault;

    /** Created timestamp ({@code created_at}). */
    @SerializedName("created_at")
    private String createdAt;

    /** Updated timestamp ({@code updated_at}). */
    @SerializedName("updated_at")
    private String updatedAt;

    /** Append date to route name ({@code append_date_to_route_name}). */
    @SerializedName("append_date_to_route_name")
    private Boolean appendDateToRouteName;

    /** Assigned driver ID ({@code driver_id}). */
    @SerializedName("driver_id")
    private Integer driverId;

    /** Assigned vehicle ID ({@code vehicle_id}). */
    @SerializedName("vehicle_id")
    private String vehicleId;

    /** Avoid highways ({@code avoid_highways}). */
    @SerializedName("avoid_highways")
    private Boolean avoidHighways;

    /** Avoid tolls ({@code avoid_tolls}). */
    @SerializedName("avoid_tolls")
    private Boolean avoidTolls;

    /** Avoidance zones enabled ({@code avoidance_zones_enabled}). */
    @SerializedName("avoidance_zones_enabled")
    private Boolean avoidanceZonesEnabled;

    /** Avoidance zones IDs ({@code avoidance_zones_ids}). */
    @SerializedName("avoidance_zones_ids")
    private List<String> avoidanceZonesIds;

    /** Breaks enabled ({@code breaks_enabled}). */
    @SerializedName("breaks_enabled")
    private Boolean breaksEnabled;

    /** Breaks mode ({@code breaks_mode}). */
    @SerializedName("breaks_mode")
    private Integer breaksMode;

    /** Breaks mode parameters ({@code breaks_mode_params}). */
    @SerializedName("breaks_mode_params")
    private List<Integer> breaksModeParams;

    /** Breaks repeats number ({@code breaks_repeats_number}). */
    @SerializedName("breaks_repeats_number")
    private Integer breaksRepeatsNumber;

    /** Breaks duration ({@code breaks_duration}). */
    @SerializedName("breaks_duration")
    private Integer breaksDuration;

    /** Bundling settings enabled ({@code bundling_settings_is_enabled}). */
    @SerializedName("bundling_settings_is_enabled")
    private Boolean bundlingSettingsIsEnabled;

    /** Bundling settings mode ({@code bundling_settings_mode}). */
    @SerializedName("bundling_settings_mode")
    private Integer bundlingSettingsMode;

    /** Bundling settings mode parameters ({@code bundling_settings_mode_params}). */
    @SerializedName("bundling_settings_mode_params")
    private List<Integer> bundlingSettingsModeParams;

    /** Bundling settings items mode ({@code bundling_settings_items_mode}). */
    @SerializedName("bundling_settings_items_mode")
    private Integer bundlingSettingsItemsMode;

    /** Bundling settings items mode parameters ({@code bundling_settings_items_mode_params}). */
    @SerializedName("bundling_settings_items_mode_params")
    private List<Integer> bundlingSettingsItemsModeParams;

    /** Bundling settings first item mode ({@code bundling_settings_first_item_mode}). */
    @SerializedName("bundling_settings_first_item_mode")
    private Integer bundlingSettingsFirstItemMode;

    /** Bundling settings first item mode parameters ({@code bundling_settings_first_item_mode_params}). */
    @SerializedName("bundling_settings_first_item_mode_params")
    private List<Integer> bundlingSettingsFirstItemModeParams;

    /** Bundling settings merge mode ({@code bundling_settings_merge_mode}). */
    @SerializedName("bundling_settings_merge_mode")
    private Integer bundlingSettingsMergeMode;

    /** Multi-depot enabled ({@code multidepot_enabled}). */
    @SerializedName("multidepot_enabled")
    private Boolean multidepotEnabled;

    /** Depot ID ({@code depot_id}). */
    @SerializedName("depot_id")
    private String depotId;

    /** Depot title ({@code depot_title}). */
    @SerializedName("depot_title")
    private String depotTitle;

    /** Depot latitude ({@code depot_latitude}). */
    @SerializedName("depot_latitude")
    private Double depotLatitude;

    /** Depot longitude ({@code depot_longitude}). */
    @SerializedName("depot_longitude")
    private Double depotLongitude;

    /** The time zone of the depot ({@code depot_timezone}). */
    @SerializedName("depot_timezone")
    private String depotTimezone;

    /** Facility ID ({@code facility_id}). */
    @SerializedName("facility_id")
    private String facilityId;

    /** Ignore time windows ({@code ignore_time_windows}). */
    @SerializedName("ignore_time_windows")
    private Boolean ignoreTimeWindows;

    /** Max distance ({@code max_distance}). */
    @SerializedName("max_distance")
    private Integer maxDistance;

    /** Max distance between destinations ({@code max_distance_between_destinations}). */
    @SerializedName("max_distance_between_destinations")
    private Integer maxDistanceBetweenDestinations;

    /** Max pieces ({@code max_pieces}). */
    @SerializedName("max_pieces")
    private Integer maxPieces;

    /** Max revenue ({@code max_revenue}). */
    @SerializedName("max_revenue")
    private Integer maxRevenue;

    /** Max stops count ({@code max_stops_count}). */
    @SerializedName("max_stops_count")
    private Integer maxStopsCount;

    /** Max volume ({@code max_volume}). */
    @SerializedName("max_volume")
    private Double maxVolume;

    /** Max weight ({@code max_weight}). */
    @SerializedName("max_weight")
    private Double maxWeight;

    /** Mixed fleet enabled ({@code mixed_fleet_is_enabled}). */
    @SerializedName("mixed_fleet_is_enabled")
    private Boolean mixedFleetIsEnabled;

    /** Mixed fleet vehicles IDs ({@code mixed_fleet_vehicles_ids}). */
    @SerializedName("mixed_fleet_vehicles_ids")
    private List<String> mixedFleetVehiclesIds;

    /** Optimization mode ({@code optimization_mode}). */
    @SerializedName("optimization_mode")
    private String optimizationMode;

    /** Balance mode ({@code balance_mode}). */
    @SerializedName("balance_mode")
    private String balanceMode;

    /** Balance routes number ({@code balance_routes_number}). */
    @SerializedName("balance_routes_number")
    private Integer balanceRoutesNumber;

    /** Routes max number ({@code routes_max_number}). */
    @SerializedName("routes_max_number")
    private Integer routesMaxNumber;

    /** Prioritize distance ({@code prioritize_distance}). */
    @SerializedName("prioritize_distance")
    private Integer prioritizeDistance;

    /** Prioritize travel time ({@code prioritize_travel_time}). */
    @SerializedName("prioritize_travel_time")
    private Integer prioritizeTravelTime;

    /** Prioritize waiting time ({@code prioritize_waiting_time}). */
    @SerializedName("prioritize_waiting_time")
    private Integer prioritizeWaitingTime;

    /** Optimization type ({@code optimization_type}). */
    @SerializedName("optimization_type")
    private String optimizationType;

    /** Route duration ({@code route_duration}). */
    @SerializedName("route_duration")
    private Integer routeDuration;

    /** Route end mode ({@code route_end_mode}). */
    @SerializedName("route_end_mode")
    private String routeEndMode;

    /** Route name ({@code route_name}). */
    @SerializedName("route_name")
    private String routeName;

    /** Enable flexible start time ({@code enable_flexible_start_time}). */
    @SerializedName("enable_flexible_start_time")
    private Boolean enableFlexibleStartTime;

    /** Depot local time ({@code depot_local_time}). */
    @SerializedName("depot_local_time")
    private Integer depotLocalTime;

    /** Flexible start time earliest start ({@code flexible_start_time_earliest_start}). */
    @SerializedName("flexible_start_time_earliest_start")
    private Integer flexibleStartTimeEarliestStart;

    /** Flexible start time latest start ({@code flexible_start_time_latest_start}). */
    @SerializedName("flexible_start_time_latest_start")
    private Integer flexibleStartTimeLatestStart;

    /** Service time override enabled ({@code service_time_override_is_enabled}). */
    @SerializedName("service_time_override_is_enabled")
    private Boolean serviceTimeOverrideIsEnabled;

    /** Service time override duration ({@code service_time_override_duration}). */
    @SerializedName("service_time_override_duration")
    private Integer serviceTimeOverrideDuration;

    /** Service time slowdown ({@code service_time_slowdown}). */
    @SerializedName("service_time_slowdown")
    private Integer serviceTimeSlowdown;

    /** Travel time slowdown ({@code travel_time_slowdown}). */
    @SerializedName("travel_time_slowdown")
    private Integer travelTimeSlowdown;

    /** Turn avoidance ({@code turn_avoidance}). */
    @SerializedName("turn_avoidance")
    private String turnAvoidance;

    /** Use depot as advanced constraint ({@code use_depot_as_advanced_constraint}). */
    @SerializedName("use_depot_as_advanced_constraint")
    private Boolean useDepotAsAdvancedConstraint;

    /** Resolve drivers by skills ({@code resolve_drivers_by_skills}). */
    @SerializedName("resolve_drivers_by_skills")
    private Boolean resolveDriversBySkills;

    /** Speed cap ({@code speed_cap}). */
    @SerializedName("speed_cap")
    private Double speedCap;

    /** Speed cap unit ({@code speed_cap_unit}). */
    @SerializedName("speed_cap_unit")
    private String speedCapUnit;
}
