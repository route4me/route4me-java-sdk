package com.route4me.sdk.services.customers;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class CustomerListFilters {
    @SerializedName("search_query")
    private String searchQuery;

    @SerializedName("view_mode")
    private String viewMode;

    @SerializedName("customer_id")
    private List<String> customerId;

    @SerializedName("name")
    private List<String> name;

    @SerializedName("external_id")
    private List<String> externalId;

    @SerializedName("status")
    private List<Integer> status;

    @SerializedName("currency")
    private List<String> currency;

    @SerializedName("facility_ids")
    private List<String> facilityIds;
}
