package com.route4me.sdk.services.customers;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class CustomerListResource {
    @SerializedName("root_member_id")
    private Integer rootMemberId;

    @SerializedName("customer_id")
    private String customerId;

    @SerializedName("name")
    private String name;

    @SerializedName("external_id")
    private String externalId;

    @SerializedName("accountable_person_first_name")
    private String accountablePersonFirstName;

    @SerializedName("accountable_person_last_name")
    private String accountablePersonLastName;

    @SerializedName("customer_location_count")
    private Integer customerLocationCount;

    @SerializedName("status")
    private Integer status;

    @SerializedName("currency")
    private String currency;

    @SerializedName("creation_date")
    private String creationDate;

    @SerializedName("created_timestamp")
    private Long createdTimestamp;

    @SerializedName("created_day_id")
    private Integer createdDayId;

    @SerializedName("created_by_member_id")
    private Integer createdByMemberId;

    @SerializedName("updated_timestamp")
    private Long updatedTimestamp;

    @SerializedName("facility_ids")
    private List<String> facilityIds;

    @SerializedName("contacts")
    private List<CustomerContactResource> contacts;
}
