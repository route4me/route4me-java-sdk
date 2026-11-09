package com.route4me.sdk.services.customers;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A contact person of a customer, as the API returns it in a {@link CustomerListResource}. */
@Data
public class CustomerContactResource {
    /** Contact type ({@code type}). */
    @SerializedName("type")
    private String type;

    /** Contact name ({@code name}). */
    @SerializedName("name")
    private String name;

    /** Contact email ({@code email}). */
    @SerializedName("email")
    private String email;

    /** Contact phone ({@code phone}). */
    @SerializedName("phone")
    private String phone;
}
