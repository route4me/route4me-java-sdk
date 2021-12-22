package com.route4me.sdk.services.orders;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Order {
    @SerializedName("created_timestamp")
    private Long created;
    @SerializedName("order_id")
    private Long id;
    @SerializedName("order_status_id")
    private Integer statusID;
    @SerializedName("day_added_YYMMDD")
    private String dateAdded;
    @SerializedName("day_scheduled_for_YYMMDD")
    private String dateScheduled;
    @SerializedName("address_alias")
    private String addressAlias;
    @SerializedName("address_1")
    private String address1;
    @SerializedName("address_2")
    private String address2;
    @SerializedName("member_id")
    private Long memberID;
    @SerializedName("EXT_FIELD_first_name")
    private String firstName;
    @SerializedName("EXT_FIELD_last_name")
    private String lastName;
    @SerializedName("EXT_FIELD_email")
    private String email;
    @SerializedName("EXT_FIELD_phone")
    private String phone;
    @SerializedName("EXT_FIELD_weight")
    private Double weight;
    @SerializedName("EXT_FIELD_cost")
    private Double cost;
    @SerializedName("EXT_FIELD_revenue")
    private Double revenue;
    @SerializedName("EXT_FIELD_cube")
    private Double cube;
    @SerializedName("EXT_FIELD_pieces")
    private Integer pieces;
    @SerializedName("EXT_FIELD_custom_data")
    private Object CustomData;
    @SerializedName("address_city")
    private String city;
    @SerializedName("address_state_id")
    private String stateID;
    @SerializedName("cached_lat")
    private Double cachedLatitude;
    @SerializedName("cached_lng")
    private Double cachedLongitude;
    @SerializedName("curbside_lat")
    private Double curbsideLatitude;
    @SerializedName("curbside_lng")
    private Double curbsideLongitude;
    @SerializedName("in_route_count")
    private Integer inRouteCount;
    @SerializedName("last_visited_timestamp")
    private Long lastVisited;
    @SerializedName("last_routed_timestamp")
    private Long lastRouted;

    @SerializedName("local_time_window_start")
    private Long localTimeWindowStart;
    @SerializedName("local_time_window_end")
    private Long localTimeWindowEnd;
    @SerializedName("local_time_window_start_2")
    private Long localTimeWindowStart2;
    @SerializedName("local_time_window_end_2")
    private Long localTimeWindowEnd2;
    @SerializedName("service_time")
    private Integer serviceTime;
    @SerializedName("local_timezone_string")
    private String timezoneString;
    @SerializedName("color")
    private String color;
    @SerializedName("order_icon")
    private String orderIcon;

    @SerializedName("is_validated")
    private Boolean validated;
    @SerializedName("is_pending")
    private Boolean pending;
    @SerializedName("is_accepted")
    private Boolean accepted;
    @SerializedName("is_started")
    private Boolean started;
    @SerializedName("is_completed")
    private Boolean completed;
    @SerializedName("last_status")
    private Integer lastStatus;
    @SerializedName("tracking_number")
    private String trackingNumber;
}
