package com.route4me.sdk.services.customers;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/**
 * A customer, as {@link CustomerManager#createCustomer(CustomerStoreRequest)} and
 * {@link CustomerManager#updateCustomer(String, CustomerStoreRequest)} return it.
 */
@Data
public class CustomerResource {
    /** Root member ID ({@code root_member_id}). */
    @SerializedName("root_member_id")
    private Long rootMemberId;

    /** Customer ID ({@code customer_id}). */
    @SerializedName("customer_id")
    private String customerId;

    /** Name ({@code name}). */
    @SerializedName("name")
    private String name;

    /** The ID of the customer in your own system ({@code external_id}). */
    @SerializedName("external_id")
    private String externalId;

    /** Contacts ({@code contacts}). */
    @SerializedName("contacts")
    private List<CustomerContact> contacts;

    /** Accountable person ID ({@code accountable_person_id}). */
    @SerializedName("accountable_person_id")
    private Long accountablePersonId;

    /** The addresses of the customer ({@code addresses}). */
    @SerializedName("addresses")
    private List<CustomerAddress> addresses;

    /** Currency ({@code currency}). */
    @SerializedName("currency")
    private String currency;

    /** Tax ID ({@code tax_id}). */
    @SerializedName("tax_id")
    private String taxId;

    /** The status: {@code 1} active, {@code 0} inactive ({@code status}). */
    @SerializedName("status")
    private Integer status;

    /** Contracts ({@code contracts}). */
    @SerializedName("contracts")
    private List<CustomerContract> contracts;

    /** Creation date (m-d-Y) ({@code creation_date}). */
    @SerializedName("creation_date")
    private String creationDate;

    /** Created timestamp ({@code created_timestamp}). */
    @SerializedName("created_timestamp")
    private Long createdTimestamp;

    /** Created day ID ({@code created_day_id}). */
    @SerializedName("created_day_id")
    private Integer createdDayId;

    /** Created by member ID ({@code created_by_member_id}). */
    @SerializedName("created_by_member_id")
    private Long createdByMemberId;

    /** Updated timestamp ({@code updated_timestamp}). */
    @SerializedName("updated_timestamp")
    private Long updatedTimestamp;

    /** Facility IDs ({@code facility_ids}). */
    @SerializedName("facility_ids")
    private List<String> facilityIds;

    /** The facilities linked to the customer ({@code facilities}). */
    @SerializedName("facilities")
    private List<CustomerFacility> facilities;
}
