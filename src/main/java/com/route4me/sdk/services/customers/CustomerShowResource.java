package com.route4me.sdk.services.customers;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerShowResource extends CustomerResource {
    @SerializedName("accountable_person_first_name")
    private String accountablePersonFirstName;

    @SerializedName("accountable_person_last_name")
    private String accountablePersonLastName;

    @SerializedName("customer_location_count")
    private Integer customerLocationCount;
}
