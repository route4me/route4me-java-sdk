package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import com.route4me.sdk.services.notes.Note;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
    /** {@code null} (the default) leaves {@code is_unrouted} out of the request. */
    @SerializedName("is_unrouted")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Boolean isUnrouted;
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
    private Double payingMiles;
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
    @SerializedName("unique_destination_count")
    private Integer uniqueDestinationCount;
    /** The rating the user gave the route. */
    @SerializedName("user_route_rating")
    private Long userRouteRating;
    /** The first name of the member the route belongs to. */
    @SerializedName("member_first_name")
    private String memberFirstName;
    /** The last name of the member the route belongs to. */
    @SerializedName("member_last_name")
    private String memberLastName;
    /** The real-time channel of the route. Read-only. */
    @SerializedName("channel_name")
    private String channelName;
    /** The picture of the member the route belongs to. */
    @SerializedName("member_picture")
    private String memberPicture;
    /** The tracking subheadline of the member the route belongs to. */
    @SerializedName("member_tracking_subheadline")
    private String memberTrackingSubheadline;
    /** Whether the route is approved for execution; {@code null} leaves it unchanged on update. */
    @SerializedName("approved_for_execution")
    private Boolean approvedForExecution;
    /** The scheduled start of the route, as a Unix timestamp in seconds. */
    @SerializedName("scheduled_start_timestamp")
    private Long scheduledStartTimestamp;
    /** When the route was dispatched, as a Unix timestamp in seconds. */
    @SerializedName("dispatched_timestamp")
    private Long dispatchedTimestamp;
    /** How many times the route was approved. Read-only. */
    @SerializedName("approved_revisions_counter")
    private Long approvedRevisionsCounter;
    /** The geofence shape of the route's stops, such as {@code circle}. */
    @SerializedName("geofence_polygon_type")
    private String geofencePolygonType;
    /** The geofence size of the route's stops, in meters. */
    @SerializedName("geofence_polygon_size")
    private Long geofencePolygonSize;
    /** The route before its last change, when the API returns it. Read-only. */
    @SerializedName("original_route")
    private Route originalRoute;
    /** The bundles of the route. Read-only. */
    @SerializedName("bundle_items")
    private List<BundledItem> bundleItems;
    /** The IDs of the facilities of the route. */
    @SerializedName("facility_ids")
    private List<String> facilityIds;

    /**
     * @return whether the route is unrouted; {@code false} when the value is not set
     */
    public boolean isUnrouted() {
        return Boolean.TRUE.equals(this.isUnrouted);
    }

    /**
     * @return {@code is_unrouted}, or {@code null} when it is not set and so not sent
     */
    public Boolean getUnrouted() {
        return this.isUnrouted;
    }

    /**
     * @param unrouted whether the route is unrouted
     */
    public void setUnrouted(boolean unrouted) {
        this.isUnrouted = unrouted;
    }

    /**
     * @param unrouted whether the route is unrouted; {@code null} leaves
     *                 {@code is_unrouted} out of the request
     */
    public void setUnrouted(Boolean unrouted) {
        this.isUnrouted = unrouted;
    }

}
