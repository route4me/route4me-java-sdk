package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.Map;

/** One changed order, from {@link OrdersManager#getOrdersUpdates(long)}. */
@Data
public class OrderUpdate {
    /** The order ID ({@code order_id}). */
    @SerializedName("order_id")
    private Long orderId;

    /** When the order last changed ({@code updated_timestamp}). */
    @SerializedName("updated_timestamp")
    private String updatedTimestamp;

    /** The custom data of the order ({@code EXT_FIELD_custom_data}). */
    @SerializedName("EXT_FIELD_custom_data")
    private Map<String, Object> customData;
}
