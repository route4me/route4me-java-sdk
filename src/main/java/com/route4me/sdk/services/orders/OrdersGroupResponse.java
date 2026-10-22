package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The answer of {@link OrderManagerV5#getOrdersGroup}. */
@Data
public class OrdersGroupResponse {
    /** The groups ({@code data}). */
    @SerializedName("data")
    private Groups data;

    /** The {@code data} of an {@link OrdersGroupResponse}. */
    @Data
    public static class Groups {
        /** The groups of orders ({@code items}). */
        @SerializedName("items")
        private List<OrdersGroup> items;
    }
}
