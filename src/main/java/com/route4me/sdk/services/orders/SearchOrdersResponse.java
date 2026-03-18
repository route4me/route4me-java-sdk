package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class SearchOrdersResponse {
    @SerializedName("items")
    private List<OrderV5> items;

    @SerializedName("total")
    private Integer total;
}
