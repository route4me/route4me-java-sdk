package com.route4me.sdk.services.customers;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A customer as {@link CustomerManager#getCustomerById(String)} returns it: a {@link CustomerResource} with its
 * location count and the name of its accountable person.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerShowResource extends CustomerResource {
    /** The first name of the accountable person ({@code accountable_person_first_name}). */
    @SerializedName("accountable_person_first_name")
    private String accountablePersonFirstName;

    /** The last name of the accountable person ({@code accountable_person_last_name}). */
    @SerializedName("accountable_person_last_name")
    private String accountablePersonLastName;

    /** The number of locations of the customer ({@code customer_location_count}). */
    @SerializedName("customer_location_count")
    private Integer customerLocationCount;
}
