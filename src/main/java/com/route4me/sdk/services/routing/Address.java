// codebeat:disable[TOO_MANY_FUNCTIONS]
package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import com.route4me.sdk.services.notes.Note;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

/** An address (stop) of an optimization or a route, as the V4 API takes and returns it. */
@Data
public class Address {

    /** Internal unique address identifier ({@code route_destination_id}). */
    @SerializedName("route_destination_id")
    private Long routeDestinationId;
    /** Address alias ({@code alias}). */
    @SerializedName("alias")
    private String alias;
    /** Member ID ({@code member_id}). */
    @SerializedName("member_id")
    private Integer memberId;
    /** The route Address Line 1 ({@code address}). */
    @SerializedName("address")
    private String address;
    /** Designate this stop as a depot. A route may have multiple depots/points of origin ({@code is_depot}). */
    @SerializedName("is_depot")
    private Boolean depot;
    /** The latitude of this address ({@code lat}). */
    @SerializedName("lat")
    private Double latitude;
    /** The longitude of this address ({@code lng}). */
    @SerializedName("lng")
    private Double longitude;
    /** The ID of the route being viewed, modified, or erased ({@code route_id}). */
    @SerializedName("route_id")
    private String routeId;
    /**
     * If this route was duplicated from an existing route, this value would have the original route's ID ({@code
     * original_route_id}).
     */
    @SerializedName("original_route_id")
    private String originalRouteId;
    /**
     * The ID of the optimization request that was used to initially instantiate this route ({@code
     * optimization_problem_id}).
     */
    @SerializedName("optimization_problem_id")
    private String optimizationProblemId;
    /** The destination's sequence number in the route ({@code sequence_no}). */
    @SerializedName("sequence_no")
    private Long sequenceNo;
    /** True if the address is geocoded ({@code geocoded}). */
    @SerializedName("geocoded")
    private Boolean geocoded;
    /** Index of 'geocodings' array that the user has chosen ({@code preferred_geocoding}). */
    @SerializedName("preferred_geocoding")
    private Integer preferredGeocoding;
    /**
     * True means there was a geocoding attempt which failed. False means success or no geocoding ({@code
     * failed_geocoding}).
     */
    @SerializedName("failed_geocoding")
    private Boolean failedGeocoding;
    /**
     * When planning a route from the address book or using existing address book IDs, pass the address book ID
     * (contact_id) for an address so that Route4Me can run analytics on the address book addresses that were used to
     * plan routes, and to find previous visits to favorite addresses ({@code contact_id}).
     */
    @SerializedName("contact_id")
    private Integer contactId;
    /** The status flag to mark an address as visited (aka check in) ({@code is_visited}). */
    @SerializedName("is_visited")
    private Boolean isVisited;
    /**
     * The number of notes that are already associated with this address on the route ({@code destination_note_count}).
     */
    @SerializedName("destination_note_count")
    private Integer destinationNoteCount;
    /** The unique socket channel name which should be used to get real time alerts ({@code channel_name}). */
    @SerializedName("channel_name")
    private String channelName;
    /** The address time window start ({@code time_window_start}). */
    @SerializedName("time_window_start")
    private Long timeWindowStart;
    /** The address time window end ({@code time_window_end}). */
    @SerializedName("time_window_end")
    private Long timeWindowEnd;
    /** The address time window start 2 ({@code time_window_start_2}). */
    @SerializedName("time_window_start_2")
    private Long timeWindowStart2;
    /** The address time window end 2 ({@code time_window_end_2}). */
    @SerializedName("time_window_end_2")
    private Long timeWindowEnd2;
    /** The expected amount of time that will be spent at this address by the driver/user ({@code time}). */
    @SerializedName("time")
    private Long time;
    /** The address notes ({@code notes}). */
    @SerializedName("notes")
    private List<Note> notes;
    /** Any unrecognized fields from the original CSV upload will be found here ({@code custom_fields}). */
    @SerializedName("custom_fields")
    @SuppressWarnings("checkstyle:MemberName") // the name of the Lombok accessors, public API
    private Map<String, Object> custom_fields;
    /** Manifest of a route address ({@code manifest}). */
    @SerializedName("manifest")
    private Map<String, Object> manifest;
    /** The address order ID ({@code order_id}). */
    @SerializedName("order_id")
    private Integer orderId;
    /** The address group ({@code group}). */
    @SerializedName("group")
    private String group;
    /**
     * Pass-through data about this route destination. The data will be visible on the manifest, website, and mobile
     * apps ({@code customer_po}).
     */
    @SerializedName("customer_po")
    private String customerPO;
    /**
     * Pass-through data about this route destination. The data will be visible on the manifest, website, and mobile
     * apps ({@code invoice_no}).
     */
    @SerializedName("invoice_no")
    private String invoiceNumber;
    /**
     * Pass-through data about this route destination. The data will be visible on the manifest, website, and mobile
     * apps ({@code reference_no}).
     */
    @SerializedName("reference_no")
    private String referenceNumber;
    /**
     * Pass-through data about this route destination. The data will be visible on the manifest, website, and mobile
     * apps ({@code order_no}).
     */
    @SerializedName("order_no")
    private String orderNumber;
    /** The first name of the receiving address ({@code first_name}). */
    @SerializedName("first_name")
    private String firstName;
    /** The last name of the receiving address ({@code last_name}). */
    @SerializedName("last_name")
    private String lastName;
    /**
     * Pass-through data about this route destination. The data will be visible on the manifest, website, and mobile
     * apps. Also used to email clients when vehicles are approaching (future capability) ({@code email}).
     */
    @SerializedName("email")
    private String email;
    /**
     * Pass-through data about this route destination. The data will be visible on the manifest, website, and mobile
     * apps. Also used to send SMS messages to clients when vehicles are approaching (future capability) ({@code
     * phone}).
     */
    @SerializedName("phone")
    private String phone;
    /** State of the timeframe violation. In a response only ({@code timeframe_violation_state}). */
    @SerializedName("timeframe_violation_state")
    private Integer timeframeViolationState;
    /** Moment of the timeframe violation. In a response only ({@code timeframe_violation_time}). */
    @SerializedName("timeframe_violation_time")
    private Integer timeframeViolationTime;
    /** Rate of the timeframe violation. In a response only ({@code timeframe_violation_rate}). */
    @SerializedName("timeframe_violation_rate")
    private Double timeframeViolationRate;
    /**
     * Curbside latitude. Generate optimal routes and driving directions to this curbside latitude ({@code
     * curbside_lat}).
     */
    @SerializedName("curbside_lat")
    private Double curbsideLatitude;
    /**
     * Curbside longitude. Generate optimal routes and driving directions to the curbside longitude ({@code
     * curbside_lng}).
     */
    @SerializedName("curbside_lng")
    private Double curbsideLongitude;
    /**
     * If present, the priority will sequence addresses in all the optimal routes so that higher priority addresses are
     * general at the beginning of the route sequence. 1 is the highest priority, 100000 is the lowest ({@code
     * priority}).
     */
    @SerializedName("priority")
    private Integer priority;
    /** An array containing Geocoding objects ({@code geocodings}). */
    @SerializedName("geocodings")
    private List<Geocodings> geocodings;
    /**
     * The type of stop that this is (DELIVERY, PICKUP, BREAK, MEETUP, SERVICE, VISIT, DRIVEBY, REFUEL). Matched without
     * regard to case or surrounding spaces, so `refuel` is read as `REFUEL`. Any other value is refused with 400. An
     * empty string or null keeps the current type. In a response, a depot has null ({@code address_stop_type}).
     */
    @SerializedName("address_stop_type")
    private String addressStopType;
    /** The last known visited timestamp of this address ({@code timestamp_last_visited}). */
    @SerializedName("timestamp_last_visited")
    private Long timestampLastVisited;
    /** Last known check in latitude ({@code visited_lat}). */
    @SerializedName("visited_lat")
    private Double visitedLatitude;
    /** Last known check in longitude ({@code visited_lng}). */
    @SerializedName("visited_lng")
    private Double visitedLongitude;
    /** The status flag to mark an address as departed (aka check out) ({@code is_departed}). */
    @SerializedName("is_departed")
    private Boolean isDeparted;
    /** Last known departed latitude ({@code departed_lat}). */
    @SerializedName("departed_lat")
    private Double departedLatitude;
    /** Last known departed longitude ({@code departed_lng}). */
    @SerializedName("departed_lng")
    private Double departedLongitude;
    /** The last known departed timestamp of this address ({@code timestamp_last_departed}). */
    @SerializedName("timestamp_last_departed")
    private Long timestampLastDeparted;
    /** The address cargo weight ({@code weight}). */
    @SerializedName("weight")
    private Double weight;
    /** The cost of the order for the address ({@code cost}). */
    @SerializedName("cost")
    private Double cost;
    /** The total revenue for the address ({@code revenue}). */
    @SerializedName("revenue")
    private Double revenue;
    /**
     * The cubic volume that this destination/order/line-item consumes/contains. This is how much space it will take up
     * on a vehicle ({@code cube}).
     */
    @SerializedName("cube")
    private Double cube;
    /**
     * The number of pieces/pallets that this destination/order/line-item consumes/contains on a vehicle ({@code
     * pieces}).
     */
    @SerializedName("pieces")
    private Integer pieces;
    /**
     * Server-side generated time, in seconds, to drive to the next location on the route ({@code
     * drive_time_to_next_destination}).
     */
    @SerializedName("drive_time_to_next_destination")
    private Long driveTimetoNextDestination;
    /** Abnormal traffic time to next destination ({@code abnormal_traffic_time_to_next_destination}). */
    @SerializedName("abnormal_traffic_time_to_next_destination")
    private Long abnormalTrafficTimetoNextDestination;
    /** Uncongested time to next destination ({@code uncongested_time_to_next_destination}). */
    @SerializedName("uncongested_time_to_next_destination")
    private Long uncongestedTimetoNextDestination;
    /** Traffic time to next destination ({@code traffic_time_to_next_destination}). */
    @SerializedName("traffic_time_to_next_destination")
    private Long trafficTimetoNextDestination;
    /**
     * Server-side generated distance to the next location, in the route's distance unit ({@code
     * distance_to_next_destination}).
     */
    @SerializedName("distance_to_next_destination")
    private Double distancetoNextDestination;
    /** UDU distance to next destination ({@code udu_distance_to_next_destination}). */
    @SerializedName("udu_distance_to_next_destination")
    private Double uduDistancetoNextDestination;
    /** Generated Time Window Start in seconds ({@code generated_time_window_start}). */
    @SerializedName("generated_time_window_start")
    private Long generatedTimeWindowStart;
    /**
     * Estimated time window end based on the optimization engine, after all the sequencing has been completed ({@code
     * generated_time_window_end}).
     */
    @SerializedName("generated_time_window_end")
    private Long generatedTimeWindowEnd;
    /** Timestamp of a geofence detected visited ({@code geofence_detected_visited_timestamp}). */
    @SerializedName("geofence_detected_visited_timestamp")
    private Long geofenceDetectedVisitedTimestamp;
    /** Timestamp of a geofence detected departed ({@code geofence_detected_departed_timestamp}). */
    @SerializedName("geofence_detected_departed_timestamp")
    private Long geofenceDetectedDepartedTimestamp;
    /** The service time of a detected geofence ({@code geofence_detected_service_time}). */
    @SerializedName("geofence_detected_service_time")
    private Long geofenceDetectedServiceTime;
    /** Latitude of a visited detected geofence ({@code geofence_detected_visited_lat}). */
    @SerializedName("geofence_detected_visited_lat")
    private Double geofenceDetectedVisitedLatitude;
    /** Longitude of a visited detected geofence ({@code geofence_detected_visited_lng}). */
    @SerializedName("geofence_detected_visited_lng")
    private Double geofenceDetectedVisitedLongitude;
    /** Latitude of a departed detected geofence ({@code geofence_detected_departed_lat}). */
    @SerializedName("geofence_detected_departed_lat")
    private Double geofenceDetectedDepartedLatitude;
    /** Longitude of a departed detected geofence ({@code geofence_detected_departed_lng}). */
    @SerializedName("geofence_detected_departed_lng")
    private Double geofenceDetectedDepartedLongitude;
    /**
     * System-wide unique code, which permits end-users (recipients) to track the status of their order ({@code
     * tracking_number}).
     */
    @SerializedName("tracking_number")
    private String trackingNumber;
    /** The route path point ({@code path_to_next}). */
    @SerializedName("path_to_next")
    private List<GeoCoordinates> pathtoNext;
    /** Wait time to next destination ({@code wait_time_to_next_destination}). */
    @SerializedName("wait_time_to_next_destination")
    private Long waitTimetoNextDestination;
    /** Alias of a pickup point ({@code pickup}). */
    @SerializedName("pickup")
    private String pickUp;
    /** Alias of the paired pickup point ({@code dropoff}). */
    @SerializedName("dropoff")
    private String dropOff;
    /**
     * If true, the pickup and dropoff addresses are joint (one by one despite the regular pickup-dropoff addresses when
     * it's possible to have multiple pickup addresses with one dropoff address) ({@code joint}).
     */
    @SerializedName("joint")
    private Boolean joint;

    /**
     * The driver tags specified in a team member's custom data. (e.g. "driver skills": ["Class A CDL", "Class B CDL",
     * "Forklift", "Skid Steer Loader", "Independent Contractor"] ({@code tags}).
     */
    @SerializedName("tags")
    @QueryParameter("tags")
    private List<String> tags;
    /** The skills a driver needs to serve this stop, matched against the drivers' skills. */
    @SerializedName("required_skills")
    private List<String> requiredSkills;
    /** The additional status of the stop. */
    @SerializedName("additional_status")
    private String additionalStatus;
    /** How many stops are bundled into this one. Read-only. */
    @SerializedName("bundle_count")
    private Integer bundleCount;
    /** The bundles of this stop. Read-only. */
    @SerializedName("bundle_items")
    private List<BundledItem> bundleItems;
    /** The custom field configuration of the stop. */
    @SerializedName("custom_fields_config")
    private List<String> customFieldsConfig;
    /** The custom field configuration, as a JSON string. */
    @SerializedName("custom_fields_config_str_json")
    private String customFieldsConfigStrJson;
    /** The custom fields, as a JSON string. */
    @SerializedName("custom_fields_str_json")
    private String customFieldsStrJson;
    /** The inventory lines of the order behind the stop. */
    @SerializedName("order_inventory")
    private List<OrderInventory> orderInventory;
    /** The name of the route the stop belongs to. */
    @SerializedName("route_name")
    private String routeName;

    /** Creates an empty address. */
    public Address() {
    }

    /**
     * Creates an address to geocode.
     *
     * @param address the address text
     */
    public Address(String address) {
        this.address = address;
    }

    /**
     * Creates an address with coordinates.
     *
     * @param address the address text
     * @param lat the latitude
     * @param lng the longitude
     */
    public Address(String address, double lat, double lng) {
        this(address);
        this.latitude = lat;
        this.longitude = lng;
    }

    /**
     * Creates an address with coordinates and a service time.
     *
     * @param address the address text
     * @param lat the latitude
     * @param lng the longitude
     * @param time the service time, in seconds
     */
    public Address(String address, double lat, double lng, long time) {
        this(address, lat, lng);
        this.time = time;
    }

    /**
     * Creates an address that may be a depot.
     *
     * @param address the address text
     * @param isDepot whether it is a depot
     * @param lat the latitude
     * @param lng the longitude
     * @param time the service time, in seconds
     */
    public Address(String address, boolean isDepot, double lat, double lng, long time) {
        this(address, lat, lng, time);
        this.depot = isDepot;
    }

    /**
     * Creates an address that may be a depot, with an alias.
     *
     * @param address the address text
     * @param isDepot whether it is a depot
     * @param alias the alias, such as the customer name
     * @param lat the latitude
     * @param lng the longitude
     * @param time the service time, in seconds
     */
    public Address(String address, boolean isDepot, String alias, double lat, double lng, long time) {
        this(address, isDepot, lat, lng, time);
        this.alias = alias;
    }

    /**
     * Creates an address that may be a depot, with a time window.
     *
     * @param address the address text
     * @param isDepot whether it is a depot
     * @param lat the latitude
     * @param lng the longitude
     * @param time the service time, in seconds
     * @param timeWindowStart the start of the time window, in seconds from midnight
     * @param timeWindowEnd the end of the time window, in seconds from midnight
     */
    public Address(String address, boolean isDepot, double lat, double lng, long time, long timeWindowStart,
            long timeWindowEnd) {
        this(address, isDepot, lat, lng, time);
        this.timeWindowStart = timeWindowStart;
        this.timeWindowEnd = timeWindowEnd;
    }

    /**
     * Creates an address with a time window.
     *
     * @param address the address text
     * @param lat the latitude
     * @param lng the longitude
     * @param time the service time, in seconds
     * @param timeWindowStart the start of the time window, in seconds from midnight
     * @param timeWindowEnd the end of the time window, in seconds from midnight
     */
    public Address(String address, double lat, double lng, long time, long timeWindowStart, long timeWindowEnd) {
        this(address, lat, lng, time);
        this.time = time;
        this.timeWindowStart = timeWindowStart;
        this.timeWindowEnd = timeWindowEnd;
    }

    /**
     * Creates an address with an alias.
     *
     * @param address the address text
     * @param alias the alias, such as the customer name
     * @param lat the latitude
     * @param lng the longitude
     * @param time the service time, in seconds
     */
    public Address(String address, String alias, double lat, double lng, long time) {
        this(address, lat, lng, time);
        this.alias = alias;
    }

    /**
     * Adds a custom field to the address ({@code custom_fields}).
     *
     * @param key the field name
     * @param value the value
     */
    public void addCustomField(String key, Object value) {
        if (this.custom_fields == null) {
            this.custom_fields = new HashMap<>();
        }
        this.custom_fields.put(key, value);
    }

    /**
     * Sets whether the pickup and dropoff of this address are joint, from a number.
     *
     * @param joint {@code 0} for {@code false}, anything else for {@code true}
     */
    public void setJoint(int joint) {
        this.setJoint(joint != 0);
    }

    /**
     * Returns whether the pickup and dropoff of this address are joint ({@code joint}).
     *
     * @return the value, or {@code null} when it is not set
     */
    public Boolean getJoint() {
        return joint;
    }

    /**
     * Sets whether the pickup and dropoff of this address are joint ({@code joint}).
     *
     * @param joint the value; {@code null} leaves it out
     */
    public void setJoint(Boolean joint) {
        this.joint = joint;
    }
}
// codebeat:enable[TOO_MANY_FUNCTIONS]
