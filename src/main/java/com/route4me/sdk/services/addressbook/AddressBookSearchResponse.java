package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * The answer of the V4 address book searches of {@link AddressBookManager}. When the request names the fields to
 * return, the API answers each contact as a row of values in the order of {@code fields} (a {@link List}); otherwise
 * as an object (a {@link java.util.Map} of JSON names to values). JSON numbers are read as {@link Double}.
 */
@Data
public class AddressBookSearchResponse {
    /** The contacts, as rows of values or as objects ({@code results}). */
    @SerializedName("results")
    private List<Object> results;

    /** The total number of contacts that match ({@code total}). */
    @SerializedName("total")
    private Integer total;

    /** The names of the values in each row ({@code fields}). */
    @SerializedName("fields")
    private List<String> fields;

    /** The search the API ran ({@code index_query}). */
    @SerializedName("index_query")
    private String indexQuery;
}
