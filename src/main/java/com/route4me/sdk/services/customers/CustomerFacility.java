package com.route4me.sdk.services.customers;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A facility linked to a customer, in a {@link CustomerResource}. */
@Data
public class CustomerFacility {
    /** Facility ID ({@code facility_id}). */
    @SerializedName("facility_id")
    private String facilityId;

    /** Facility name ({@code facility_name}). */
    @SerializedName("facility_name")
    private String facilityName;
}
