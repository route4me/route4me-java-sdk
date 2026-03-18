package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class SearchOrdersRequest {
    @SerializedName("filters")
    private OrderFilters filters;

    @SerializedName("order_by")
    private List<String[]> orderBy;

    @SerializedName("page")
    private Integer page;

    @SerializedName("per_page")
    private Integer perPage;

    @SerializedName("fields")
    private List<String> fields;
}
