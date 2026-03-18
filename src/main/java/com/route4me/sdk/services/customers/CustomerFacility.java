package com.route4me.sdk.services.customers;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class CustomerFacility {
    @SerializedName("facility_id")
    private String facilityId;

    @SerializedName("facility_name")
    private String facilityName;
}
