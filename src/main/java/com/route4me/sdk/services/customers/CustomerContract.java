package com.route4me.sdk.services.customers;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class CustomerContract {
    @SerializedName("start_date")
    private String startDate;

    @SerializedName("expiration")
    private String expiration;
}
