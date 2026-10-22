package com.route4me.sdk.services.addressbook;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
import java.util.Map;
/**
 * A contact, as the V5 address book API takes and returns it. Fields left {@code null} are not sent. Creating or
 * updating a contact needs {@code address_1}, {@code cached_lat} and {@code cached_lng}.
 */
@Data
public class AddressBookContactV5 {
    /** Unique ID of the contact ({@code address_id}). */
    @SerializedName("address_id") private Long addressId;
    /** Unique ID of the member ({@code member_id}). */
    @SerializedName("member_id") private Long memberId;
    /** The geographic address of the contact ({@code address_1}). */
    @SerializedName("address_1") private String address1;
    /** Second geographic address of the contact ({@code address_2}). */
    @SerializedName("address_2") private String address2;
    /** The contact's alias ({@code address_alias}). */
    @SerializedName("address_alias") private String addressAlias;
    /** A group the contact belongs ({@code address_group}). */
    @SerializedName("address_group") private String addressGroup;
    /** The first name of the contact person ({@code first_name}). */
    @SerializedName("first_name") private String firstName;
    /** The last name of the contact person ({@code last_name}). */
    @SerializedName("last_name") private String lastName;
    /** The contact's email ({@code address_email}). */
    @SerializedName("address_email") private String addressEmail;
    /** The contact's phone number ({@code address_phone_number}). */
    @SerializedName("address_phone_number") private String addressPhoneNumber;
    /** A latitude of the contact's cached position ({@code cached_lat}). */
    @SerializedName("cached_lat") private Double cachedLat;
    /** A longitude of the contact's cached position ({@code cached_lng}). */
    @SerializedName("cached_lng") private Double cachedLng;
    /** A latitude of the contact's curbside ({@code curbside_lat}). */
    @SerializedName("curbside_lat") private Double curbsideLat;
    /** A longitude of the contact's curbside ({@code curbside_lng}). */
    @SerializedName("curbside_lng") private Double curbsideLng;
    /** A city the contact belongs ({@code address_city}). */
    @SerializedName("address_city") private String addressCity;
    /** The ID of the state the contact belongs ({@code address_state_id}). */
    @SerializedName("address_state_id") private String addressStateId;
    /** The ID of the country the contact belongs ({@code address_country_id}). */
    @SerializedName("address_country_id") private String addressCountryId;
    /** The ZIP code the address is located in ({@code address_zip}). */
    @SerializedName("address_zip") private String addressZip;
    /** Start of the contact's local time window ({@code local_time_window_start}). */
    @SerializedName("local_time_window_start") private Long localTimeWindowStart;
    /** End of the contact's local time window ({@code local_time_window_end}). */
    @SerializedName("local_time_window_end") private Long localTimeWindowEnd;
    /** Start of the contact's second local time window ({@code local_time_window_start_2}). */
    @SerializedName("local_time_window_start_2") private Long localTimeWindowStart2;
    /** End of the contact's second local time window ({@code local_time_window_end_2}). */
    @SerializedName("local_time_window_end_2") private Long localTimeWindowEnd2;
    /** An array of the contact's custom field-value pairs ({@code address_custom_data}). */
    @SerializedName("address_custom_data") private Map<String, String> addressCustomData;
    /** Time when the contact was created ({@code created_timestamp}). */
    @SerializedName("created_timestamp") private Long createdTimestamp;
    /** A territory shape name the contact belongs ({@code territory_name}). */
    @SerializedName("territory_name") private String territoryName;
    /** The time zone of the time windows, such as {@code America/New_York} ({@code local_timezone_string}). */
    @SerializedName("local_timezone_string") private String localTimezoneString;
    /** The service time at the contact, in seconds, from 0 to 65535 ({@code service_time}). */
    @SerializedName("service_time") private Long serviceTime;
    /** The color of the contact on the map, as a hex code ({@code color}). */
    @SerializedName("color") private String color;
    /** The icon of the contact on the map ({@code address_icon}). */
    @SerializedName("address_icon") private String addressIcon;
    /**
     * The kind of stop: {@code DELIVERY}, {@code PICKUP}, {@code BREAK}, {@code MEETUP}, {@code SERVICE},
     * {@code VISIT}, {@code DRIVEBY} or {@code REFUEL} ({@code address_stop_type}).
     */
    @SerializedName("address_stop_type") private String addressStopType;
    /** The schedules of the contact, as JSON names and values ({@code schedule}). */
    @SerializedName("schedule") private List<Map<String, Object>> schedule;
    /** The dates, as {@code YYYY-MM-DD}, when the schedules do not apply ({@code schedule_blacklist}). */
    @SerializedName("schedule_blacklist") private List<String> scheduleBlacklist;
    /** The volume of the cargo ({@code address_cube}). */
    @SerializedName("address_cube") private Double addressCube;
    /** The number of pieces of the cargo ({@code address_pieces}). */
    @SerializedName("address_pieces") private Integer addressPieces;
    /** A reference number of the contact ({@code address_reference_no}). */
    @SerializedName("address_reference_no") private String addressReferenceNo;
    /** The revenue of the stop ({@code address_revenue}). */
    @SerializedName("address_revenue") private Double addressRevenue;
    /** The weight of the cargo ({@code address_weight}). */
    @SerializedName("address_weight") private Double addressWeight;
    /** The man-hours the stop takes ({@code man_hours}). */
    @SerializedName("man_hours") private Double manHours;
    /** The priority of the stop ({@code address_priority}). */
    @SerializedName("address_priority") private Integer addressPriority;
    /** The customer purchase order number ({@code address_customer_po}). */
    @SerializedName("address_customer_po") private String addressCustomerPo;
    /** Whether the contact can be a pickup ({@code eligible_pickup}). */
    @SerializedName("eligible_pickup") private Boolean eligiblePickup;
    /** Whether the contact can be a depot ({@code eligible_depot}). */
    @SerializedName("eligible_depot") private Boolean eligibleDepot;
    /** Whether the address of the contact was validated ({@code is_validated}). */
    @SerializedName("is_validated") private Boolean isValidated;
    /** The ID of the customer of the contact ({@code customer_id}). */
    @SerializedName("customer_id") private String customerId;
    /** The ID of the service duration rule of the contact ({@code service_duration_rule_id}). */
    @SerializedName("service_duration_rule_id") private String serviceDurationRuleId;
    /** The ID of the parent location of the contact ({@code parent_location_id}). */
    @SerializedName("parent_location_id") private Long parentLocationId;
    /** The tags of the contact ({@code tags}). */
    @SerializedName("tags") private List<String> tags;
    /** The IDs of the assets of the contact ({@code asset_ids}). */
    @SerializedName("asset_ids") private List<String> assetIds;
    /** How many routes the contact is in; read-only ({@code in_route_count}). */
    @SerializedName("in_route_count") private Integer inRouteCount;
    /** How many times the contact was visited; read-only ({@code visited_count}). */
    @SerializedName("visited_count") private Integer visitedCount;
    /** When the contact was last visited, as a Unix timestamp; read-only ({@code last_visited_timestamp}). */
    @SerializedName("last_visited_timestamp") private Long lastVisitedTimestamp;
    /** When the contact was last routed, as a Unix timestamp; read-only ({@code last_routed_timestamp}). */
    @SerializedName("last_routed_timestamp") private Long lastRoutedTimestamp;
}
