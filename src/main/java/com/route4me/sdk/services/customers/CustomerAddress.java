package com.route4me.sdk.services.customers;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class CustomerAddress {
    @SerializedName("type")
    private String type;

    @SerializedName("address")
    private String address;
}
