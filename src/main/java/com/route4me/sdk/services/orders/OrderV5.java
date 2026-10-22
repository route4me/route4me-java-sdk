package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.Map;

/** An order, as the V5 order platform of {@link OrderManagerV5} takes and returns it. */
@Data
public class OrderV5 {
    /** An unique internal ID of an order ({@code order_id}). */
    @SerializedName("order_id")
    private String orderId;

    /** Order UUID ({@code order_uuid}). */
    @SerializedName("order_uuid")
    private String orderUuid;

    /** The id of the member inside the route4me system ({@code member_id}). */
    @SerializedName("member_id")
    private Long memberId;

    /** Root member ID ({@code root_member_id}). */
    @SerializedName("root_member_id")
    private Long rootMemberId;

    /** A unique ID of a group the organizations or partners ({@code organization_id}). */
    @SerializedName("organization_id")
    private Integer organizationId;

    /** Address Alias. Required ({@code address_alias}). */
    @SerializedName("address_alias")
    private String addressAlias;

    /** Address 1 field. Required ({@code address_1}). */
    @SerializedName("address_1")
    private String address1;

    /** Address 2 field ({@code address_2}). */
    @SerializedName("address_2")
    private String address2;

    /** The city the address is located in ({@code address_city}). */
    @SerializedName("address_city")
    private String addressCity;

    /** The state the address is located in ({@code address_state_id}). */
    @SerializedName("address_state_id")
    private String addressStateId;

    /** The country the address is located in ({@code address_country_id}). */
    @SerializedName("address_country_id")
    private String addressCountryId;

    /** The zip code the address is located in ({@code address_zip}). */
    @SerializedName("address_zip")
    private String addressZip;

    /** Address geo ({@code address_geo}). */
    @SerializedName("address_geo")
    private GeoPoint addressGeo;

    /** Curbside geo ({@code curbside_geo}). */
    @SerializedName("curbside_geo")
    private GeoPoint curbsideGeo;

    /** Custom data ({@code custom_data}). */
    @SerializedName("custom_data")
    private Map<String, Object> customData;

    /** First name ({@code first_name}). */
    @SerializedName("first_name")
    private String firstName;

    /** Last name ({@code last_name}). */
    @SerializedName("last_name")
    private String lastName;

    /** Email ({@code email}). */
    @SerializedName("email")
    private String email;

    /** Phone number ({@code phone}). */
    @SerializedName("phone")
    private String phone;

    /** The weight of the order's cargo (2 digits precision) ({@code weight}). */
    @SerializedName("weight")
    private Double weight;

    /** Order cost (2 digits precision) ({@code cost}). */
    @SerializedName("cost")
    private Double cost;

    /** Order revenue (2 digits precision) ({@code revenue}). */
    @SerializedName("revenue")
    private Double revenue;

    /** The cubic volume that this order consumes/contains on a vehicle (2 digits precision) ({@code cube}). */
    @SerializedName("cube")
    private Double cube;

    /**
     * The number of pieces/pallets that this order consumes/contains on a vehicle (2 digits precision) ({@code
     * pieces}).
     */
    @SerializedName("pieces")
    private Double pieces;

    /** How many times the order visited ({@code visited_count}). */
    @SerializedName("visited_count")
    private Integer visitedCount;

    /** When the order inserted ({@code day_added_YYMMDD}). */
    @SerializedName("day_added_YYMMDD")
    private String dayAdded;

    /** The date the order is scheduled for, as {@code YYYY-MM-DD} ({@code day_scheduled_for_YYYYMMDD}). */
    @SerializedName("day_scheduled_for_YYYYMMDD")
    private String dayScheduledFor;

    /** Timestamp of an order creation ({@code created_timestamp}). */
    @SerializedName("created_timestamp")
    private Long createdTimestamp;

    /** Timestamp of an order creation ({@code updated_timestamp}). */
    @SerializedName("updated_timestamp")
    private Long updatedTimestamp;
}
