package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * The body of {@link AddressBookManagerV5#getContactsPaginated(AddressBookContactsBodyPaginatedRequest)}: a search
 * text, filters, a sort order, the fields to return and a page. Fields left {@code null} are not sent.
 */
@Data
public class AddressBookContactsBodyPaginatedRequest {
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
     * The sort order, as {@code [field, direction]} pairs, such as {@code [["address_alias", "asc"]]}; the fields
     * are those of the {@code order_by} of {@link AddressBookContactsBodyRequest} ({@code order_by}).
     */
    @SerializedName("order_by")
    private List<String[]> orderBy;

    /** The page number, from 1 ({@code page}). */
    @SerializedName("page")
    private Integer page;

    /** The number of contacts per page ({@code per_page}). */
    @SerializedName("per_page")
    private Integer perPage;
}
