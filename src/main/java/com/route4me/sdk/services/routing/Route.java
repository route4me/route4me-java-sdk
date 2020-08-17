package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import com.route4me.sdk.services.notes.Note;
import lombok.Data;

import java.util.List;

@Data
public class Route extends DataObject {
    @SerializedName("route_id")
    @QueryParameter("route_id")
    private String id;
    @SerializedName("member_id")
    @QueryParameter("member_id")
    private Long memberId;
    @SerializedName("member_email")
    private String memberEmail;
    @SerializedName("is_unrouted")
    private boolean isUnrouted;
    @SerializedName("vehicle_alias")
    private String vehicleAlias;
    @SerializedName("route_cost")
    private Double routeCost;
    @SerializedName("route_revenue")
    private Double routeRevenue;
    @SerializedName("net_revenue_per_distance_unit")
    private Double netRevenuePerDistanceUnit;
    @SerializedName("created_timestamp")
    private Long created_timestamp;
    @SerializedName("mpg")
    private Double mpg;
    @SerializedName("trip_distance")
    private Double tripDistance;
    @SerializedName("gas_price")
    private Double gasPrice;
    @SerializedName("route_duration_sec")
    private Integer routeDurationSec;
    @SerializedName("planned_total_route_duration")
    private Integer plannedTotalRouteDuration;
    @SerializedName("udu_distance_unit")
    private String uduDistanceUnit;
    @SerializedName("udu_trip_distance")
    private Double uduTripDistance;
    @SerializedName("route_weight")
    private Double routeWeight;
    @SerializedName("route_cube")
    private Double routeCube;
    @SerializedName("route_pieces")
    private Integer routePieces;
    @SerializedName("total_wait_time")
    private Integer totalWaitTime;
    @SerializedName("udu_actual_travel_distance")
    private Double uduActualTravelDistance;
    @SerializedName("actual_travel_distance")
    private Double actualTravelDistance;
    @SerializedName("actual_footsteps")
    private Integer actualFootsteps;
    @SerializedName("working_time")
    private Integer workingTime;
    @SerializedName("driving_time")
    private Integer drivingTime;
    @SerializedName("idling_time")
    private Integer idlingTime;
    @SerializedName("paying_miles")
    private Integer payingMiles;
    @SerializedName("destination_count")
    private Integer destinationCount;
    @SerializedName("notes_count")
    private Integer notesCount;
    @SerializedName("actual_travel_time")
    private Integer actualTravelTime;
    @SerializedName("vehicle")
    private String vehicle;
    @SerializedName("directions")
    private List<Direction> routeDirections;
    @SerializedName("notes")
    List<Note> notes;
    @SerializedName("path")
    private List<GeoCoordinates> path;
}
