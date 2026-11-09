package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * The body of {@link AddressBookManagerV5#updateAddressBookContactByAreas(UpdateContactsByAreasRequest)}: which
 * contacts to update, and the contact fields to set on all of them.
 */
@Data
public class UpdateContactsByAreasRequest {
    /** The contacts to update ({@code filter}). */
    @SerializedName("filter")
    private AddressBookAreasFilter filter;

    /** The fields to set; fields left {@code null} are not changed ({@code data}). */
    @SerializedName("data")
    private AddressBookContactV5 data;
}
