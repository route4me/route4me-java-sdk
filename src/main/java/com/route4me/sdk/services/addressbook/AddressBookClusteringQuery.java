package com.route4me.sdk.services.addressbook;

import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;

import java.util.List;

/**
 * The query of {@link AddressBookManagerV5#getAddressBookContactsClustering(AddressBookClusteringQuery)}. Fields
 * left {@code null} are not sent; lists are sent as {@code name[]=value}.
 */
@Data
public class AddressBookClusteringQuery {
    /** Which contacts to cluster: {@code all} (the default), {@code routed} or {@code unrouted} ({@code display}). */
    @QueryParameter("display")
    private String display;

    /** Which contacts to cluster: {@code routed}, {@code unrouted} or both ({@code display_list}). */
    @QueryParameter("display_list")
    private List<String> displayList;

    /** Text to search for in the contacts ({@code query}). */
    @QueryParameter("query")
    private String query;
}
