package com.route4me.sdk.services.customers;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** An address of a customer, in a {@link CustomerResource}. */
@Data
public class CustomerAddress {
    /** The kind of address: {@code billing}, {@code shipping} or {@code delivery} ({@code type}). */
    @SerializedName("type")
    private String type;

    /** The address ({@code address}). */
    @SerializedName("address")
    private String address;
}
