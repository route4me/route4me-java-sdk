package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The answer of {@link OrdersManager#getOrdersUpdates(long)}. */
@Data
public class OrdersUpdatesResponse {
    /** The orders changed since the given time ({@code data}). */
    @SerializedName("data")
    private List<OrderUpdate> data;
}
