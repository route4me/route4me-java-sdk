package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The answer of {@link OrderManagerV5#searchOrders(SearchOrdersRequest)}. */
@Data
public class SearchOrdersResponse {
    /** Objects of the requested type, with the requested fields ({@code items}). */
    @SerializedName("items")
    private List<OrderV5> items;

    /** The number of matching orders ({@code total}). */
    @SerializedName("total")
    private Integer total;
}
