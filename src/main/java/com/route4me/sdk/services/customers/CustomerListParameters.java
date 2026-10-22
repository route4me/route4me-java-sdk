package com.route4me.sdk.services.customers;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/**
 * The body of {@link CustomerManager#getCustomersList(CustomerListParameters)}: filters, sort order, page and fields.
 */
@Data
public class CustomerListParameters {
    /** The filters of the list ({@code filters}). */
    @SerializedName("filters")
    private CustomerListFilters filters;

    /**
     * The sort order of the list, as {@code [field, direction]} pairs, such as {@code [["name", "asc"]]}; the
     * direction is {@code asc} or {@code desc} ({@code order_by}).
     */
    @SerializedName("order_by")
    private List<String[]> orderBy;

    /** The page number, from 1 ({@code page}). */
    @SerializedName("page")
    private Integer page;

    /** The number of customers per page ({@code per_page}). */
    @SerializedName("per_page")
    private Integer perPage;

    /** The fields to return for each customer ({@code fields}). */
    @SerializedName("fields")
    private List<String> fields;
}
