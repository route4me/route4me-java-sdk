package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.Map;

@Data
public class BatchUpdateFilterOrderRequest {
    @SerializedName("filters")
    private OrderFilters filters;

    @SerializedName("data")
    private Map<String, Object> data;
}
