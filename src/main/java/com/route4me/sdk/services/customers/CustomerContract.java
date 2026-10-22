package com.route4me.sdk.services.customers;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A contract of a customer. */
@Data
public class CustomerContract {
    /** The start date of the contract ({@code start_date}). */
    @SerializedName("start_date")
    private String startDate;

    /** The expiration date of the contract ({@code expiration}). */
    @SerializedName("expiration")
    private String expiration;
}
