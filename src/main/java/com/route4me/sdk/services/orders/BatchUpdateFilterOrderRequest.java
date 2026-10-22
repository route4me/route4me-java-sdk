package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.Map;

/** The body of {@link OrderManagerV5#batchUpdateFilter(BatchUpdateFilterOrderRequest)}. */
@Data
public class BatchUpdateFilterOrderRequest {
    /** The filters that select the orders to update ({@code filters}). */
    @SerializedName("filters")
    private OrderFilters filters;

    /** The text search that selects the orders to update ({@code search}). */
    @SerializedName("search")
    private OrderSearch search;

    /** The new values, by their API names, such as {@code first_name} ({@code data}). */
    @SerializedName("data")
    private Map<String, Object> data;
}
