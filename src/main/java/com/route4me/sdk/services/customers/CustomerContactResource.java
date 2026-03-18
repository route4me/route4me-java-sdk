package com.route4me.sdk.services.customers;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class CustomerContactResource {
    @SerializedName("type")
    private String type;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;
}
