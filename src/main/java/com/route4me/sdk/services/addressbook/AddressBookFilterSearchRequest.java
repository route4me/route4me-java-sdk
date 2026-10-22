package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * The body of {@link AddressBookManager#searchAddressBookContactsByFilter(AddressBookFilterSearchRequest, Integer,
 * Integer)}: the search filter and the fields to return. Fields left {@code null} are not sent.
 */
@Data
public class AddressBookFilterSearchRequest {
    /** The fields to return for each contact, such as {@code address_id} and {@code address_1} ({@code fields}). */
    @SerializedName("fields")
    private List<String> fields;

    /** The search; it tells this request apart from creating a contact ({@code filter}). */
    @SerializedName("filter")
    private AddressBookSearchFilter filter;
}
