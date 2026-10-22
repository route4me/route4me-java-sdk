package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The body of {@link OrderManagerV5#batchUpdateOrders(BatchUpdateOrdersRequest)}. */
@Data
public class BatchUpdateOrdersRequest {
    /** Data ({@code data}). */
    @SerializedName("data")
    private List<OrderV5> data;
}
