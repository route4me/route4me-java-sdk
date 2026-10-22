package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The body of {@link OrderManagerV5#archiveOrders(ArchiveOrdersRequest)}. */
@Data
public class ArchiveOrdersRequest {
    /** The IDs of the orders to archive ({@code order_ids}). */
    @SerializedName("order_ids")
    private List<String> orderIds;
}
