package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;
import java.util.Map;

/** A contact, as the V4 address book API of {@link AddressBookManager} takes and returns it. */
@Data
public class Contact {

    /** When the contact was created, as a Unix timestamp ({@code created_timestamp}). */
    @SerializedName("created_timestamp")
    private Double createdTimestamp;
    /** Unique ID of the contact ({@code address_id}). */
    @SerializedName("address_id")
    private Number addressId;
    /** A group the contact belongs ({@code address_group}). */
    @SerializedName("address_group")
    private String addressGroup;
    /** Address alias ({@code address_alias}). */
    @SerializedName("address_alias")
    private String addressAlias;
    /** Address_1 field ({@code address_1}). */
    @SerializedName("address_1")
    private String address1;
    /** Address_2 field ({@code address_2}). */
    @SerializedName("address_2")
    private String address2;
    /** The ID of the member the contact belongs to ({@code member_id}). */
    @SerializedName("member_id")
    private Integer memberId;
    /** First name ({@code first_name}). */
    @SerializedName("first_name")
    private String firstName;
    /** Last name ({@code last_name}). */
    @SerializedName("last_name")
    private String lastName;
    /** The contact's email ({@code address_email}). */
    @SerializedName("address_email")
    private String addressEmail;
    /** The contact's phone number ({@code address_phone_number}). */
    @SerializedName("address_phone_number")
    private String addressPhoneNumber;
    /** The city the address is located in ({@code address_city}). */
    @SerializedName("address_city")
    private String addressCity;
    /** The state the address is located in ({@code address_state_id}). */
    @SerializedName("address_state_id")
    private String addressStateId;
    /** The country the address is located in ({@code address_country_id}). */
    @SerializedName("address_country_id")
    private String addressCountryId;
    /** The ZIP code the address is located in ({@code address_zip}). */
    @SerializedName("address_zip")
    private String addressZip;
    /** Cached latitude ({@code cached_lat}). */
    @SerializedName("cached_lat")
    private Number cachedLat;
    /** Cached longitude ({@code cached_lng}). */
    @SerializedName("cached_lng")
    private Number cachedLng;
    /** Curbside latitude ({@code curbside_lat}). */
    @SerializedName("curbside_lat")
    private Number curbsideLat;
    /** Curbside longitude ({@code curbside_lng}). */
    @SerializedName("curbside_lng")
    private Number curbsideLng;
    /** An array of the contact's custom field-value pairs ({@code address_custom_data}). */
    @SerializedName("address_custom_data")
    private Map<String, Object> addressCustomData;

    /** Schedule ({@code schedule}). */
    @SerializedName("schedule")
    private List<Object> schedule;
    /** Schedule black list ({@code schedule_blacklist}). */
    @SerializedName("schedule_blacklist")
    private List<Object> scheduleBlacklist;
    /** In how many routes is included address ({@code in_route_count}). */
    @SerializedName("in_route_count")
    private Integer inRouteCount;
    /** How many times the contact was visited ({@code visited_count}). */
    @SerializedName("visited_count")
    private Integer visitedCount;
    /** When the last time an address was visited ({@code last_visited_timestamp}). */
    @SerializedName("last_visited_timestamp")
    private Double lastVisitedTimestamp;
    /** When last time an address was included in a route ({@code last_routed_timestamp}). */
    @SerializedName("last_routed_timestamp")
    private Double lastRoutedTimestamp;
    /** Start a local time window ({@code local_time_window_start}). */
    @SerializedName("local_time_window_start")
    private Double localTimeWindowStart;
    /** End a local time window ({@code local_time_window_end}). */
    @SerializedName("local_time_window_end")
    private Double localTimeWindowEnd;
    /** Start a local time window 2 ({@code local_time_window_start_2}). */
    @SerializedName("local_time_window_start_2")
    private Double localTimeWindowStart2;
    /** End a local time window 2 ({@code local_time_window_end_2}). */
    @SerializedName("local_time_window_end_2")
    private Double localTimeWindowEnd2;
    /** Consumed service time at an address ({@code service_time}). */
    @SerializedName("service_time")
    private Double serviceTime;
    /** Local timezone ({@code local_timezone_string}). */
    @SerializedName("local_timezone_string")
    private String localTimezoneString;
    /** The icon color of the contact on a map ({@code color}). */
    @SerializedName("color")
    private String color;
    /** The contact's icon on the map ({@code address_icon}). */
    @SerializedName("address_icon")
    private String addressIcon;
    /**
     * The type of stop that this is (DELIVERY,PICKUP,BREAK,MEETUP,SERVICE,VISIT,DRIVEBY) ({@code address_stop_type}).
     */
    @SerializedName("address_stop_type")
    private String addressStopType;
    /** The cubic volume of the contact's cargo ({@code address_cube}). */
    @SerializedName("address_cube")
    private String addressCube;
    /**
     * The number of pieces/pallets that this destination/order/line-item consumes/contains on a vehicle ({@code
     * address_pieces}).
     */
    @SerializedName("address_pieces")
    private Integer addressPieces;
    /** The reference number of the address ({@code address_reference_no}). */
    @SerializedName("address_reference_no")
    private String addressReferenceNo;
    /** The revenue from the contact ({@code address_revenue}). */
    @SerializedName("address_revenue")
    private String addressRevenue;
    /** The weight of the contact's cargo ({@code address_weight}). */
    @SerializedName("address_weight")
    private String addressWeight;
    /** The priority of the contact in a route: 1 is the highest, 100000 the lowest ({@code address_priority}). */
    @SerializedName("address_priority")
    private Integer addressPriority;
    /** The customer purchase order of the contact ({@code address_customer_po}). */
    @SerializedName("address_customer_po")
    private String addressCustomerPo;
    /** Assigned to the member ({@code assigned_to}). */
    @SerializedName("assigned_to")
    private Integer assignedTo;
    /** Whether the contact can be a pickup ({@code eligible_pickup}). */
    @SerializedName("eligible_pickup")
    private Boolean eligiblePickup;
    /** Whether the contact can be a depot ({@code eligible_depot}). */
    @SerializedName("eligible_depot")
    private Boolean eligibleDepot;

}
