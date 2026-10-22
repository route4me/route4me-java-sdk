package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A group of orders, from {@link OrderManagerV5#getOrdersGroup}, to build an optimization from. */
@Data
public class OrdersGroup {
    /** The ID of the group ({@code group_id}). */
    @SerializedName("group_id")
    private String groupId;

    /** The optimization profile of the group ({@code optimization_profile_id}). */
    @SerializedName("optimization_profile_id")
    private String optimizationProfileId;

    /** The order field the orders are grouped by ({@code group_column}). */
    @SerializedName("group_column")
    private String groupColumn;

    /** The value of that field for this group ({@code group_value}). */
    @SerializedName("group_value")
    private String groupValue;

    /** The number of orders in the group ({@code orders_count}). */
    @SerializedName("orders_count")
    private Integer ordersCount;
}
