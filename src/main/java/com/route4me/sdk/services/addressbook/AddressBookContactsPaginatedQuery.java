package com.route4me.sdk.services.addressbook;

import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;

import java.util.List;

/**
 * The query of {@link AddressBookManagerV5#getContactsPaginated(AddressBookContactsPaginatedQuery)}. Fields left
 * {@code null} are not sent; lists are sent as {@code name[]=value}, which the API reads as an array.
 */
@Data
public class AddressBookContactsPaginatedQuery {
    /** The fields to return for each contact, such as {@code address_id} and {@code address_1} ({@code fields}). */
    @QueryParameter("fields")
    private List<String> fields;

    /** Which contacts to return: {@code all} (the default), {@code routed} or {@code unrouted} ({@code display}). */
    @QueryParameter("display")
    private String display;

    /** Which contacts to return: {@code routed}, {@code unrouted} or both ({@code display_list}). */
    @QueryParameter("display_list")
    private List<String> displayList;

    /** Text to search for in the contacts ({@code query}). */
    @QueryParameter("query")
    private String query;

    /** The page number, from 1, the default ({@code page}). */
    @QueryParameter("page")
    private Integer page;

    /** The number of contacts per page; the API defaults to 30 ({@code per_page}). */
    @QueryParameter("per_page")
    private Integer perPage;
}
