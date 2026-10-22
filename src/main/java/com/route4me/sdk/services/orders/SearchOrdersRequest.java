package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The body of {@link OrderManagerV5#searchOrders(SearchOrdersRequest)}. */
@Data
public class SearchOrdersRequest {
    /** Routes filters ({@code filters}). */
    @SerializedName("filters")
    private OrderFilters filters;

    /**
     * Array of fields name to be ordered by (ASC by default, of DESC the field name should have "-" prefix) ({@code
     * order_by}).
     */
    @SerializedName("order_by")
    private List<String[]> orderBy;

    /** Pagination page number ({@code page}). */
    @SerializedName("page")
    private Integer page;

    /** Items per page ({@code per_page}). */
    @SerializedName("per_page")
    private Integer perPage;

    /**
     * Response fields to return. Supports one level of nesting with a wildcard, e.g. "route_data.is_depot" or
     * "route_data.*". Empty/omitted returns all fields; unknown fields are ignored ({@code fields}).
     */
    @SerializedName("fields")
    private List<String> fields;
}
