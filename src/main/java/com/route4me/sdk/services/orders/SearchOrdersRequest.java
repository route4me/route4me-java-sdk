package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The body of {@link OrderManagerV5#searchOrders(SearchOrdersRequest)}; fields left {@code null} are not sent. */
@Data
public class SearchOrdersRequest {
    /** Only these orders, by their 32-character hex IDs ({@code order_ids}). */
    @SerializedName("order_ids")
    private List<String> orderIds;

    /** Only these orders, by their UUIDs ({@code order_uuids}). */
    @SerializedName("order_uuids")
    private List<String> orderUuids;

    /** Whether to return the requested fields as a map ({@code return_provided_fields_as_map}). */
    @SerializedName("return_provided_fields_as_map")
    private Boolean returnProvidedFieldsAsMap;

    /**
     * The sort order: pairs of a field name, such as {@code address_alias} or {@code day_added}, and a direction,
     * {@code asc} or {@code desc} ({@code order_by}).
     */
    @SerializedName("order_by")
    private List<String[]> orderBy;

    /** The number of orders per page ({@code limit}). */
    @SerializedName("limit")
    private Integer limit;

    /** The number of orders to skip ({@code offset}). */
    @SerializedName("offset")
    private Integer offset;

    /** The fields of the orders to return, such as {@code order_uuid} or {@code address_1} ({@code fields}). */
    @SerializedName("fields")
    private List<String> fields;

    /** Additional fields to return: {@code territory_ids}, {@code aggregation_ids} ({@code addition}). */
    @SerializedName("addition")
    private List<String> addition;

    /** The text search ({@code search}). */
    @SerializedName("search")
    private OrderSearch search;

    /** The filters ({@code filters}). */
    @SerializedName("filters")
    private OrderFilters filters;
}
