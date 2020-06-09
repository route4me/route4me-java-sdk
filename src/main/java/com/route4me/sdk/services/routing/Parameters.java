package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
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
    @SerializedName("vehicle_capacity")
    private String vehicleCapacity;
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

}
