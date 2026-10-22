package com.route4me.sdk.services.customers;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class CustomerListResponse {
    @SerializedName("items")
    private List<CustomerListResource> items;
}
