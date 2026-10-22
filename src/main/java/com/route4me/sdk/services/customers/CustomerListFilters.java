package com.route4me.sdk.services.customers;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/**
 * The filters of {@link CustomerManager#getCustomersList(CustomerListParameters)}; filters left {@code null} are not
 * sent.
 */
@Data
public class CustomerListFilters {
    /** Text to search for in the customers ({@code search_query}). */
    @SerializedName("search_query")
    private String searchQuery;

    /** The view the list is shown in ({@code view_mode}). */
    @SerializedName("view_mode")
    private String viewMode;

    /** Only these customer IDs ({@code customer_id}). */
    @SerializedName("customer_id")
    private List<String> customerId;

    /** Only customers with these names ({@code name}). */
    @SerializedName("name")
    private List<String> name;

    /** Only customers with these external IDs ({@code external_id}). */
    @SerializedName("external_id")
    private List<String> externalId;

    /** Only customers with these statuses: {@code 1} active, {@code 0} inactive ({@code status}). */
    @SerializedName("status")
    private List<Integer> status;

    /** Only customers with these currency codes ({@code currency}). */
    @SerializedName("currency")
    private List<String> currency;

    /** Only customers linked to these facility IDs ({@code facility_ids}). */
    @SerializedName("facility_ids")
    private List<String> facilityIds;
}
