package com.route4me.sdk.services.customers;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** One customer of {@link CustomerManager#getCustomersList(CustomerListParameters)}. */
@Data
public class CustomerListResource {
    /** Root member ID ({@code root_member_id}). */
    @SerializedName("root_member_id")
    private Integer rootMemberId;

    /** Customer ID ({@code customer_id}). */
    @SerializedName("customer_id")
    private String customerId;

    /** Customer name ({@code name}). */
    @SerializedName("name")
    private String name;

    /** The ID of the customer in your own system ({@code external_id}). */
    @SerializedName("external_id")
    private String externalId;

    /** Accountable person first name ({@code accountable_person_first_name}). */
    @SerializedName("accountable_person_first_name")
    private String accountablePersonFirstName;

    /** Accountable person last name ({@code accountable_person_last_name}). */
    @SerializedName("accountable_person_last_name")
    private String accountablePersonLastName;

    /** Number of customer locations ({@code customer_location_count}). */
    @SerializedName("customer_location_count")
    private Integer customerLocationCount;

    /** The status: {@code 1} active, {@code 0} inactive ({@code status}). */
    @SerializedName("status")
    private Integer status;

    /** Currency ({@code currency}). */
    @SerializedName("currency")
    private String currency;

    /** Creation date (UTC string) ({@code creation_date}). */
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
    private Integer createdByMemberId;

    /** Updated timestamp ({@code updated_timestamp}). */
    @SerializedName("updated_timestamp")
    private Long updatedTimestamp;

    /** Facility IDs ({@code facility_ids}). */
    @SerializedName("facility_ids")
    private List<String> facilityIds;

    /** Contacts ({@code contacts}). */
    @SerializedName("contacts")
    private List<CustomerContactResource> contacts;
}
