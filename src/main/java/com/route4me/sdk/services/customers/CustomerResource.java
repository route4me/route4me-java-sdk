package com.route4me.sdk.services.customers;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class CustomerResource {
    @SerializedName("root_member_id")
    private Long rootMemberId;

    @SerializedName("customer_id")
    private String customerId;

    @SerializedName("name")
    private String name;

    @SerializedName("external_id")
    private String externalId;

    @SerializedName("contacts")
    private List<CustomerContact> contacts;

    @SerializedName("accountable_person_id")
    private Long accountablePersonId;

    @SerializedName("addresses")
    private List<CustomerAddress> addresses;

    @SerializedName("currency")
    private String currency;

    @SerializedName("tax_id")
    private String taxId;

    @SerializedName("status")
    private Integer status;

    @SerializedName("contracts")
    private List<CustomerContract> contracts;

    @SerializedName("creation_date")
    private String creationDate;

    @SerializedName("created_timestamp")
    private Long createdTimestamp;

    @SerializedName("created_day_id")
    private Integer createdDayId;

    @SerializedName("created_by_member_id")
    private Long createdByMemberId;

    @SerializedName("updated_timestamp")
    private Long updatedTimestamp;

    @SerializedName("facility_ids")
    private List<String> facilityIds;

    @SerializedName("facilities")
    private List<CustomerFacility> facilities;
}
