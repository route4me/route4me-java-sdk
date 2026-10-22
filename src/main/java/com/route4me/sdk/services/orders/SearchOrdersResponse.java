package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The answer of {@link OrderManagerV5#searchOrders(SearchOrdersRequest)}. */
@Data
public class SearchOrdersResponse {
    /** The matching orders, with the requested fields ({@code results}). */
    @SerializedName("results")
    private List<OrderV5> results;

    /** The number of matching orders ({@code total}). */
    @SerializedName("total")
    private Integer total;

    /** The names of the returned fields ({@code fields}). */
    @SerializedName("fields")
    private List<String> fields;
}
