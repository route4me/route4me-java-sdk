package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The body of {@link OrderManagerV5#batchUpdateOrders(BatchUpdateOrdersRequest)}. */
@Data
public class BatchUpdateOrdersRequest {
    /** The IDs of the orders to update, as 32-character hex strings ({@code order_ids}). */
    @SerializedName("order_ids")
    private List<String> orderIds;

    /** The new values, set on every order of {@code order_ids} ({@code data}). */
    @SerializedName("data")
    private OrderV5 data;
}
