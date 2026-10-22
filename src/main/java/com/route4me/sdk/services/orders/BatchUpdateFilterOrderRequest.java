package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.Map;

/** The body of {@link OrderManagerV5#batchUpdateFilter(BatchUpdateFilterOrderRequest)}. */
@Data
public class BatchUpdateFilterOrderRequest {
    /** Filters ({@code filters}). */
    @SerializedName("filters")
    private OrderFilters filters;

    /** Data ({@code data}). */
    @SerializedName("data")
    private Map<String, Object> data;
}
