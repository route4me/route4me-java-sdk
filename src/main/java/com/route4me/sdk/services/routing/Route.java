package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;

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
    @SerializedName("route_distance_sec")
    private Integer routeDurationSec;
}
