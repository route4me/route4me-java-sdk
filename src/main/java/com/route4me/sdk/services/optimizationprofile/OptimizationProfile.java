package com.route4me.sdk.services.optimizationprofile;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class OptimizationProfile {
    @SerializedName("optimization_profile_id")
    private String optimizationProfileId;

    @SerializedName("profile_name")
    private String profileName;

    @SerializedName("is_default")
    private Boolean isDefault;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("append_date_to_route_name")
    private Boolean appendDateToRouteName;

    @SerializedName("driver_id")
    private Integer driverId;

    @SerializedName("vehicle_id")
    private String vehicleId;

    @SerializedName("avoid_highways")
    private Boolean avoidHighways;

    @SerializedName("avoid_tolls")
    private Boolean avoidTolls;

    @SerializedName("avoidance_zones_enabled")
    private Boolean avoidanceZonesEnabled;

    @SerializedName("avoidance_zones_ids")
    private List<String> avoidanceZonesIds;

    @SerializedName("breaks_enabled")
    private Boolean breaksEnabled;

    @SerializedName("breaks_mode")
    private Integer breaksMode;

    @SerializedName("breaks_mode_params")
    private List<Integer> breaksModeParams;

    @SerializedName("breaks_repeats_number")
    private Integer breaksRepeatsNumber;

    @SerializedName("breaks_duration")
    private Integer breaksDuration;

    @SerializedName("bundling_settings_is_enabled")
    private Boolean bundlingSettingsIsEnabled;

    @SerializedName("bundling_settings_mode")
    private Integer bundlingSettingsMode;

    @SerializedName("bundling_settings_mode_params")
    private List<Integer> bundlingSettingsModeParams;

    @SerializedName("bundling_settings_items_mode")
    private Integer bundlingSettingsItemsMode;

    @SerializedName("bundling_settings_items_mode_params")
    private List<Integer> bundlingSettingsItemsModeParams;

    @SerializedName("bundling_settings_first_item_mode")
    private Integer bundlingSettingsFirstItemMode;

    @SerializedName("bundling_settings_first_item_mode_params")
    private List<Integer> bundlingSettingsFirstItemModeParams;

    @SerializedName("bundling_settings_merge_mode")
    private Integer bundlingSettingsMergeMode;

    @SerializedName("multidepot_enabled")
    private Boolean multidepotEnabled;

    @SerializedName("depot_id")
    private String depotId;

    @SerializedName("depot_title")
    private String depotTitle;

    @SerializedName("depot_latitude")
    private Double depotLatitude;

    @SerializedName("depot_longitude")
    private Double depotLongitude;

    @SerializedName("depot_timezone")
    private String depotTimezone;

    @SerializedName("facility_id")
    private String facilityId;

    @SerializedName("ignore_time_windows")
    private Boolean ignoreTimeWindows;

    @SerializedName("max_distance")
    private Integer maxDistance;

    @SerializedName("max_distance_between_destinations")
    private Integer maxDistanceBetweenDestinations;

    @SerializedName("max_pieces")
    private Integer maxPieces;

    @SerializedName("max_revenue")
    private Integer maxRevenue;

    @SerializedName("max_stops_count")
    private Integer maxStopsCount;

    @SerializedName("max_volume")
    private Double maxVolume;

    @SerializedName("max_weight")
    private Double maxWeight;

    @SerializedName("mixed_fleet_is_enabled")
    private Boolean mixedFleetIsEnabled;

    @SerializedName("mixed_fleet_vehicles_ids")
    private List<String> mixedFleetVehiclesIds;

    @SerializedName("optimization_mode")
    private String optimizationMode;

    @SerializedName("balance_mode")
    private String balanceMode;

    @SerializedName("balance_routes_number")
    private Integer balanceRoutesNumber;

    @SerializedName("routes_max_number")
    private Integer routesMaxNumber;

    @SerializedName("prioritize_distance")
    private Integer prioritizeDistance;

    @SerializedName("prioritize_travel_time")
    private Integer prioritizeTravelTime;

    @SerializedName("prioritize_waiting_time")
    private Integer prioritizeWaitingTime;

    @SerializedName("optimization_type")
    private String optimizationType;

    @SerializedName("route_duration")
    private Integer routeDuration;

    @SerializedName("route_end_mode")
    private String routeEndMode;

    @SerializedName("route_name")
    private String routeName;

    @SerializedName("enable_flexible_start_time")
    private Boolean enableFlexibleStartTime;

    @SerializedName("depot_local_time")
    private Integer depotLocalTime;

    @SerializedName("flexible_start_time_earliest_start")
    private Integer flexibleStartTimeEarliestStart;

    @SerializedName("flexible_start_time_latest_start")
    private Integer flexibleStartTimeLatestStart;

    @SerializedName("service_time_override_is_enabled")
    private Boolean serviceTimeOverrideIsEnabled;

    @SerializedName("service_time_override_duration")
    private Integer serviceTimeOverrideDuration;

    @SerializedName("service_time_slowdown")
    private Integer serviceTimeSlowdown;

    @SerializedName("travel_time_slowdown")
    private Integer travelTimeSlowdown;

    @SerializedName("turn_avoidance")
    private String turnAvoidance;

    @SerializedName("use_depot_as_advanced_constraint")
    private Boolean useDepotAsAdvancedConstraint;

    @SerializedName("resolve_drivers_by_skills")
    private Boolean resolveDriversBySkills;

    @SerializedName("speed_cap")
    private Double speedCap;

    @SerializedName("speed_cap_unit")
    private String speedCapUnit;
}
