package com.route4me.sdk.services.customers;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The answer of {@link CustomerManager#getCustomersList(CustomerListParameters)}: a page of customers. */
@Data
public class CustomerListResponse {
    /** List of customers ({@code items}). */
    @SerializedName("items")
    private List<CustomerListResource> items;
}
