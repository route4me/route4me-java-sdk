// codebeat:disable[TOO_MANY_FUNCTIONS]
package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.services.notes.Note;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class Address {
    
    @SerializedName("route_destination_id")
    private Long routeDestinationId;
    @SerializedName("alias")
    private String alias;
    @SerializedName("member_id")
    private Integer memberId;
    @SerializedName("address")
    private String address;
    @SerializedName("is_depot")
    private Boolean depot;
    @SerializedName("lat")
    private Double latitude;
    @SerializedName("lng")
    private Double longitude;
    @SerializedName("route_id")
    private String routeId;
    @SerializedName("original_route_id")
    private String originalRouteId;
    @SerializedName("optimization_problem_id")
    private String optimizationProblemId;
    @SerializedName("sequence_no")
    private Long sequenceNo;
    @SerializedName("geocoded")
    private Boolean geocoded;
    @SerializedName("preferred_geocoding")
    private Integer preferredGeocoding;
    @SerializedName("failed_geocoding")
    private Boolean failedGeocoding;
    @SerializedName("contact_id")
    private Integer contactId;
    @SerializedName("is_visited")
    private Boolean isVisited;
    @SerializedName("destination_note_count")
    private Integer destinationNoteCount;
    @SerializedName("channel_name")
    private String channelName;
    @SerializedName("time_window_start")
    private Long timeWindowStart;
    @SerializedName("time_window_end")
    private Long timeWindowEnd;
    @SerializedName("time_window_start_2")
    private Long timeWindowStart2;
    @SerializedName("time_window_end_2")
    private Long timeWindowEnd2;
    @SerializedName("time")
    private Long time;
    @SerializedName("notes")
    private List<Note> notes;
    @SerializedName("custom_fields")
    private Map<String, Object>  custom_fields;
    @SerializedName("manifest")
    private Map<String, Object>  manifest;
    @SerializedName("order_id")
    private Integer orderId;
    @SerializedName("group")
    private String group;
    @SerializedName("customer_po")
    private String customerPO;
    @SerializedName("invoice_no")
    private String invoiceNumber;
    @SerializedName("reference_no")
    private String referenceNumber;
    @SerializedName("order_no")
    private String orderNumber;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("email")
    private String email;
    @SerializedName("phone")
    private String phone;
    @SerializedName("timeframe_violation_state")
    private Integer timeframeViolationState;
    @SerializedName("timeframe_violation_time")
    private Integer timeframeViolationTime;
    @SerializedName("timeframe_violation_rate")
    private Double timeframeViolationRate;
    @SerializedName("curbside_lat")
    private Double curbsideLatitude;
    @SerializedName("curbside_lng")
    private Double curbsideLongitude;
    @SerializedName("priority")
    private Integer priority;
    @SerializedName("geocodings")
    private List<Geocodings> geocodings;
    @SerializedName("address_stop_type")
    private String addressStopType;
    @SerializedName("timestamp_last_visited")
    private Long timestampLastVisited;
    @SerializedName("visited_lat")
    private Double visitedLatitude;
    @SerializedName("visited_lng")
    private Double visitedLongitude;
    @SerializedName("is_departed")
    private Boolean isDeparted;
    @SerializedName("departed_lat")
    private Double departedLatitude;
    @SerializedName("departed_lng")
    private Double departedLongitude;
    @SerializedName("timestamp_last_departed")
    private Long timestampLastDeparted;
    @SerializedName("weight")
    private Double weight;
    @SerializedName("cost")
    private Double cost;
    @SerializedName("revenue")
    private Double revenue;
    @SerializedName("cube")
    private Double cube;
    @SerializedName("pieces")
    private Integer pieces;
    @SerializedName("drive_time_to_next_destination")
    private Long driveTimetoNextDestination;
    @SerializedName("abnormal_traffic_time_to_next_destination")
    private Long abnormalTrafficTimetoNextDestination;
    @SerializedName("uncongested_time_to_next_destination")
    private Long uncongestedTimetoNextDestination;
    @SerializedName("traffic_time_to_next_destination")
    private Long trafficTimetoNextDestination;
    @SerializedName("distance_to_next_destination")
    private Double distancetoNextDestination;
    @SerializedName("udu_distance_to_next_destination")
    private Double uduDistancetoNextDestination;
    @SerializedName("generated_time_window_start")
    private Long generatedTimeWindowStart;
    @SerializedName("generated_time_window_end")
    private Long generatedTimeWindowEnd;
    @SerializedName("geofence_detected_visited_timestamp")
    private Long geofenceDetectedVisitedTimestamp;
    @SerializedName("geofence_detected_departed_timestamp")
    private Long geofenceDetectedDepartedTimestamp;
    @SerializedName("geofence_detected_service_time")
    private Long geofenceDetectedServiceTime;
    @SerializedName("geofence_detected_visited_lat")
    private Double geofenceDetectedVisitedLatitude;
    @SerializedName("geofence_detected_visited_lng")
    private Double geofenceDetectedVisitedLongitude;
    @SerializedName("geofence_detected_departed_lat")
    private Double geofenceDetectedDepartedLatitude;
    @SerializedName("geofence_detected_departed_lng")
    private Double geofenceDetectedDepartedLongitude;
    @SerializedName("tracking_number")
    private String trackingNumber;
    @SerializedName("path_to_next")
    private List<GeoCoordinates> pathtoNext;
    @SerializedName("wait_time_to_next_destination")
    private Long waitTimetoNextDestination;

    
    
    public Address() {
    }
    
    public Address(String address, double lat, double lng, long time) {
        this.address = address;
        this.latitude = lat;
        this.longitude = lng;
        this.time = time;
    }

    public Address(String address, boolean isDepot, double lat, double lng, long time) {
        this(address, lat, lng, time);
        this.depot = isDepot;
    }

    public Address(String address, boolean isDepot, String alias, double lat, double lng, long time) {
        this(address, isDepot, lat, lng, time);
        this.alias = alias;
    }

    public Address(String address, boolean isDepot, double lat, double lng, long time, long timeWindowStart, long timeWindowEnd) {
        this(address, isDepot, lat, lng, time);
        this.timeWindowStart = timeWindowStart;
        this.timeWindowEnd = timeWindowEnd;
    }

    public Address(String address, double lat, double lng, long time, long timeWindowStart, long timeWindowEnd) {
        this(address, lat, lng, time);
        this.time = time;
        this.timeWindowStart = timeWindowStart;
        this.timeWindowEnd = timeWindowEnd;
    }

    public Address(String address, String alias, double lat, double lng, long time) {
        this(address, lat, lng, time);
        this.alias = alias;
    }
    
    public void addCustomField(String key, Object value) {
        this.custom_fields.put(key, value);
    }
}
// codebeat:enable[TOO_MANY_FUNCTIONS]
