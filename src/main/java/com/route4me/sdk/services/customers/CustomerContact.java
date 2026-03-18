package com.route4me.sdk.services.customers;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.Map;

@Data
public class CustomerContact {
    @SerializedName("type")
    private String type;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("phone")
    private String phone;

    @SerializedName("email")
    private String email;

    @SerializedName("custom_data")
    private Map<String, Object> customData;
}
