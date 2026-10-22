package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The body of {@link OrderManagerV5#batchDeleteOrders(BatchDeleteOrdersRequest)}. */
@Data
public class BatchDeleteOrdersRequest {
    /** The IDs of the orders to delete, as 32-character hex strings ({@code order_ids}). */
    @SerializedName("order_ids")
    private List<String> orderUuids;
}
