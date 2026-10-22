package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * The body of {@link AddressBookManagerV5#getContacts(AddressBookContactsBodyRequest)}: a search text, filters,
 * a sort order, the fields to return and a range. Fields left {@code null} are not sent.
 */
@Data
public class AddressBookContactsBodyRequest {
    /** Text to search for in the contacts ({@code query}). */
    @SerializedName("query")
    private String query;

    /** The fields to return for each contact; all of them when empty or {@code null} ({@code fields}). */
    @SerializedName("fields")
    private List<String> fields;

    /** The filters ({@code filter}). */
    @SerializedName("filter")
    private AddressBookContactsFilter filter;

    /**
     * The sort order, as {@code [field, direction]} pairs, such as {@code [["address_alias", "asc"]]}. The API
     * sorts by {@code address_1}, {@code address_alias}, {@code first_name}, {@code last_name},
     * {@code address_phone_number}, {@code address_email}, {@code address_group}, {@code in_route_count},
     * {@code visited_count}, {@code last_visited_timestamp} and {@code last_routed_timestamp} ({@code order_by}).
     */
    @SerializedName("order_by")
    private List<String[]> orderBy;

    /** The number of contacts to return, up to 10000, the default ({@code limit}). */
    @SerializedName("limit")
    private Integer limit;

    /** The number of contacts to skip ({@code offset}). */
    @SerializedName("offset")
    private Integer offset;
}
