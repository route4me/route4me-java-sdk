package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.Map;

@Data
public class OrderV5 {
    @SerializedName("order_id")
    private String orderId;

    @SerializedName("order_uuid")
    private String orderUuid;

    @SerializedName("member_id")
    private Long memberId;

    @SerializedName("root_member_id")
    private Long rootMemberId;

    @SerializedName("organization_id")
    private Integer organizationId;

    @SerializedName("address_alias")
    private String addressAlias;

    @SerializedName("address_1")
    private String address1;

    @SerializedName("address_2")
    private String address2;

    @SerializedName("address_city")
    private String addressCity;

    @SerializedName("address_state_id")
    private String addressStateId;

    @SerializedName("address_country_id")
    private String addressCountryId;

    @SerializedName("address_zip")
    private String addressZip;

    @SerializedName("address_geo")
    private GeoPoint addressGeo;

    @SerializedName("curbside_geo")
    private GeoPoint curbsideGeo;

    @SerializedName("custom_data")
    private Map<String, Object> customData;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("weight")
    private Double weight;

    @SerializedName("cost")
    private Double cost;

    @SerializedName("revenue")
    private Double revenue;

    @SerializedName("cube")
    private Double cube;

    @SerializedName("pieces")
    private Double pieces;

    @SerializedName("visited_count")
    private Integer visitedCount;

    @SerializedName("day_added_YYMMDD")
    private String dayAdded;

    @SerializedName("day_scheduled_for_YYYYMMDD")
    private String dayScheduledFor;

    @SerializedName("created_timestamp")
    private Long createdTimestamp;

    @SerializedName("updated_timestamp")
    private Long updatedTimestamp;
}
