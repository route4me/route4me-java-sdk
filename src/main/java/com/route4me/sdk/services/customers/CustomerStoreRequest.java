package com.route4me.sdk.services.customers;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class CustomerStoreRequest {
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

    @SerializedName("facility_ids")
    private List<String> facilityIds;
}
