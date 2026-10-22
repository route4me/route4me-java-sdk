package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The body of {@link OrderManagerV5#batchDeleteOrders(BatchDeleteOrdersRequest)}. */
@Data
public class BatchDeleteOrdersRequest {
    /** The UUIDs of the orders to delete ({@code order_uuids}). */
    @SerializedName("order_uuids")
    private List<String> orderUuids;
}
