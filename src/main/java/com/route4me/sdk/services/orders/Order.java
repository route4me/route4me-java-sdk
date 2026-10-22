package com.route4me.sdk.services.orders;


import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

/** An order, as the V4 order API of {@link OrdersManager} takes and returns it. */
@Data
public class Order {
    /** Timestamp of an order creation ({@code created_timestamp}). */
    @SerializedName("created_timestamp")
    private Long created;
    /** Timestamp of an order creation ({@code updated_timestamp}). */
    @SerializedName("updated_timestamp")
    private Long updatedTimestamp;
    /** An unique internal ID of an order ({@code order_id}). */
    @SerializedName("order_id")
    private Long id;
    /** The status code of the order; see {@link OrderStatus} ({@code order_status_id}). */
    @SerializedName("order_status_id")
    private Integer statusID;
    /** When the order inserted ({@code day_added_YYMMDD}). */
    @SerializedName("day_added_YYMMDD")
    private String dateAdded;
    /** A date the order was scheduled for ({@code day_scheduled_for_YYMMDD}). */
    @SerializedName("day_scheduled_for_YYMMDD")
    private String dateScheduled;
    /** Address Alias. Required ({@code address_alias}). */
    @SerializedName("address_alias")
    private String addressAlias;
    /** Address 1 field. Required ({@code address_1}). */
    @SerializedName("address_1")
    private String address1;
    /** Address 2 field ({@code address_2}). */
    @SerializedName("address_2")
    private String address2;
    /** The id of the member inside the route4me system ({@code member_id}). */
    @SerializedName("member_id")
    private Long memberID;
    /** The first name of the receiving address ({@code EXT_FIELD_first_name}). */
    @SerializedName("EXT_FIELD_first_name")
    private String firstName;
    /** The last name of the receiving address ({@code EXT_FIELD_last_name}). */
    @SerializedName("EXT_FIELD_last_name")
    private String lastName;
    /** Address email ({@code EXT_FIELD_email}). */
    @SerializedName("EXT_FIELD_email")
    private String email;
    /** The phone number for the address ({@code EXT_FIELD_phone}). */
    @SerializedName("EXT_FIELD_phone")
    private String phone;
    /** The weight of the order's cargo (2 digits precision) ({@code EXT_FIELD_weight}). */
    @SerializedName("EXT_FIELD_weight")
    private Double weight;
    /** Order cost (2 digits precision) ({@code EXT_FIELD_cost}). */
    @SerializedName("EXT_FIELD_cost")
    private Double cost;
    /** Order revenue (2 digits precision) ({@code EXT_FIELD_revenue}). */
    @SerializedName("EXT_FIELD_revenue")
    private Double revenue;
    /**
     * The cubic volume that this order consumes/contains on a vehicle (2 digits precision) ({@code EXT_FIELD_cube}).
     */
    @SerializedName("EXT_FIELD_cube")
    private Double cube;
    /**
     * The number of pieces/pallets that this order consumes/contains on a vehicle (2 digits precision) ({@code
     * EXT_FIELD_pieces}).
     */
    @SerializedName("EXT_FIELD_pieces")
    private Integer pieces;
    /** A custom data of the order ({@code EXT_FIELD_custom_data}). */
    @SerializedName("EXT_FIELD_custom_data")
    private Object customData;
    /** The city the address is located in ({@code address_city}). */
    @SerializedName("address_city")
    private String city;
    /** The state the address is located in ({@code address_state_id}). */
    @SerializedName("address_state_id")
    private String stateID;
    /** The country the address is located in ({@code address_country_id}). */
    @SerializedName("address_country_id")
    private String addressCountryID;
    /** The zip code the address is located in ({@code address_zip}). */
    @SerializedName("address_zip")
    private String addressZip;
    /** Geo latitude ({@code cached_lat}). */
    @SerializedName("cached_lat")
    private Double cachedLatitude;
    /** Geo longitude ({@code cached_lng}). */
    @SerializedName("cached_lng")
    private Double cachedLongitude;
    /** Generate optimal routes and driving directions to this curbside latitude ({@code curbside_lat}). */
    @SerializedName("curbside_lat")
    private Double curbsideLatitude;
    /** Generate optimal routes and driving directions to the curbside langitude ({@code curbside_lng}). */
    @SerializedName("curbside_lng")
    private Double curbsideLongitude;
    /** In how many routes is included address ({@code in_route_count}). */
    @SerializedName("in_route_count")
    private Integer inRouteCount;
    /** When the last time an address was visited ({@code last_visited_timestamp}). */
    @SerializedName("last_visited_timestamp")
    private Long lastVisited;
    /** When last time an address was included in a route ({@code last_routed_timestamp}). */
    @SerializedName("last_routed_timestamp")
    private Long lastRouted;
    /** Start a local time window ({@code local_time_window_start}). */
    @SerializedName("local_time_window_start")
    private Long localTimeWindowStart;
    /** End a local time window ({@code local_time_window_end}). */
    @SerializedName("local_time_window_end")
    private Long localTimeWindowEnd;
    /** Second Local time window start ({@code local_time_window_start_2}). */
    @SerializedName("local_time_window_start_2")
    private Long localTimeWindowStart2;
    /** Second local time window end ({@code local_time_window_end_2}). */
    @SerializedName("local_time_window_end_2")
    private Long localTimeWindowEnd2;
    /** Consumed service time at an address ({@code service_time}). */
    @SerializedName("service_time")
    private Integer serviceTime;
    /** Local timezone string ({@code local_timezone_string}). */
    @SerializedName("local_timezone_string")
    private String timezoneString;
    /** Order icon hexadecimal color on a map ({@code color}). */
    @SerializedName("color")
    private String color;
    /** URL to an order icon file ({@code order_icon}). */
    @SerializedName("order_icon")
    private String orderIcon;
    /** If true, the order is validated ({@code is_validated}). */
    @SerializedName("is_validated")
    private Boolean validated;
    /** If true, the order is pending ({@code is_pending}). */
    @SerializedName("is_pending")
    private Boolean pending;
    /** If true, the order is accepted ({@code is_accepted}). */
    @SerializedName("is_accepted")
    private Boolean accepted;
    /** If true, the order is started ({@code is_started}). */
    @SerializedName("is_started")
    private Boolean started;
    /** If true, the order is completed ({@code is_completed}). */
    @SerializedName("is_completed")
    private Boolean completed;
    /** Last status of the order ({@code last_status}). */
    @SerializedName("last_status")
    private Integer lastStatus;
    /**
     * System-wide unique code, which permits end-users (recipients) to track the status of their order ({@code
     * tracking_number}).
     */
    @SerializedName("tracking_number")
    private String trackingNumber;
    /** How many times the order visited ({@code visited_count}). */
    @SerializedName("visited_count")
    private Integer visitedCount;
    /**
     * The stop type of the order: {@code DELIVERY}, {@code PICKUP}, {@code BREAK}, {@code MEETUP}, {@code SERVICE},
     * {@code VISIT} or {@code DRIVEBY} ({@code address_stop_type}).
     */
    @SerializedName("address_stop_type")
    private String addressStopType;
    /** An order sorted on date ({@code sorted_on_date}). */
    @SerializedName("sorted_on_date")
    private String sortedOnDate;
    /** Root member ID ({@code root_member_id}). */
    @SerializedName("root_member_id")
    private Integer rootMemberID;
    /** A unique ID of a group the organizations or partners ({@code organization_id}). */
    @SerializedName("organization_id")
    private Integer organizationID;
    /** A day ID, when the order was completed ({@code done_day_id}). */
    @SerializedName("done_day_id")
    private Integer doneDayID;
    /** Custom user fields ({@code custom_user_fields}). */
    @SerializedName("custom_user_fields")
    private List<OrderCustomUserField> customUserFields;
    /** Group ({@code group}). */
    @SerializedName("group")
    private String group;

}
