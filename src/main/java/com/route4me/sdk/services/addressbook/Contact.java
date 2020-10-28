package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Contact {

    @SerializedName("created_timestamp")
    private Double createdTimestamp;
    @SerializedName("address_id")
    private Number addressId;
    @SerializedName("address_group")
    private String addressGroup;
    @SerializedName("address_alias")
    private String addressAlias;
    @SerializedName("address_1")
    private String address1;
    @SerializedName("address_2")
    private String address2;
    @SerializedName("member_id")
    private Integer memberId;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("address_email")
    private String addressEmail;
    @SerializedName("address_phone_number")
    private String addressPhoneNumber;
    @SerializedName("address_city")
    private String addressCity;
    @SerializedName("address_state_id")
    private String addressStateId;
    @SerializedName("address_country_id")
    private String addressCountryId;
    @SerializedName("address_zip")
    private String addressZip;
    @SerializedName("cached_lat")
    private Number cachedLat;
    @SerializedName("cached_lng")
    private Number cachedLng;
    @SerializedName("curbside_lat")
    private Number curbsideLat;
    @SerializedName("curbside_lng")
    private Number curbsideLng;
    @SerializedName("address_custom_data")
    private Map<String, Object> addressCustomData;

    @SerializedName("schedule")
    private List<Object> schedule;
    @SerializedName("schedule_blacklist")
    private List<Object> scheduleBlacklist;
    @SerializedName("in_route_count")
    private Integer inRouteCount;
    @SerializedName("visited_count")
    private Integer visitedCount;
    @SerializedName("last_visited_timestamp")
    private Double lastVisitedTimestamp;
    @SerializedName("last_routed_timestamp")
    private Double lastRoutedTimestamp;
    @SerializedName("local_time_window_start")
    private Double localTimeWindowStart;
    @SerializedName("local_time_window_end")
    private Double localTimeWindowEnd;
    @SerializedName("local_time_window_start_2")
    private Double localTimeWindowStart2;
    @SerializedName("local_time_window_end_2")
    private Double localTimeWindowEnd2;
    @SerializedName("service_time")
    private Double serviceTime;
    @SerializedName("local_timezone_string")
    private String localTimezoneString;
    @SerializedName("color")
    private String color;
    @SerializedName("address_icon")
    private String addressIcon;
    @SerializedName("address_stop_type")
    private String addressStopType;
    @SerializedName("address_cube")
    private String addressCube;
    @SerializedName("address_pieces")
    private Integer addressPieces;
    @SerializedName("address_reference_no")
    private String addressReferenceNo;
    @SerializedName("address_revenue")
    private String addressRevenue;
    @SerializedName("address_weight")
    private String addressWeight;
    @SerializedName("address_priority")
    private Integer addressPriority;
    @SerializedName("address_customer_po")
    private String addressCustomerPo;
    @SerializedName("assigned_to")
    private Integer assignedTo;
    @SerializedName("eligible_pickup")
    private Boolean eligiblePickup;
    @SerializedName("eligible_depot")
    private Boolean eligibleDepot;

}
