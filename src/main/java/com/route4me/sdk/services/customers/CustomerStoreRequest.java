package com.route4me.sdk.services.customers;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/**
 * The body of {@link CustomerManager#createCustomer(CustomerStoreRequest)} and
 * {@link CustomerManager#updateCustomer(String, CustomerStoreRequest)}; fields left {@code null} are not sent.
 */
@Data
public class CustomerStoreRequest {
    /** The name of the customer ({@code name}). */
    @SerializedName("name")
    private String name;

    /** The ID of the customer in your own system ({@code external_id}). */
    @SerializedName("external_id")
    private String externalId;

    /** The contacts of the customer ({@code contacts}). */
    @SerializedName("contacts")
    private List<CustomerContact> contacts;

    /** The member ID of the person accountable for the customer ({@code accountable_person_id}). */
    @SerializedName("accountable_person_id")
    private Long accountablePersonId;

    /** The addresses of the customer ({@code addresses}). */
    @SerializedName("addresses")
    private List<CustomerAddress> addresses;

    /** The currency code, such as {@code USD} ({@code currency}). */
    @SerializedName("currency")
    private String currency;

    /** The tax ID of the customer ({@code tax_id}). */
    @SerializedName("tax_id")
    private String taxId;

    /** The status: {@code 1} active, {@code 0} inactive ({@code status}). */
    @SerializedName("status")
    private Integer status;

    /** The contracts of the customer ({@code contracts}). */
    @SerializedName("contracts")
    private List<CustomerContract> contracts;

    /** The IDs of the facilities linked to the customer ({@code facility_ids}). */
    @SerializedName("facility_ids")
    private List<String> facilityIds;
}
