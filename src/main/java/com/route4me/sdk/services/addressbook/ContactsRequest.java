package com.route4me.sdk.services.addressbook;

import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/** The query of {@link AddressBookManager#getContacts(ContactsRequest)}. */
@Data
@Accessors(chain = true)
public class ContactsRequest {
    /** The IDs of the contacts to get, sent as {@code address_id}, separated by commas. */
    private List<String> addressIds = new ArrayList<>();
    /** Query string for filtering of the address book contacts ({@code query}). */
    @QueryParameter("query")
    private String query;
    /**
     * An array of the fields. If specified, the response will contain only the values of the listed fields ({@code
     * fields}).
     */
    @QueryParameter("fields")
    private String fields;
    /** Limit the number of records in response ({@code limit}). */
    @QueryParameter("limit")
    private Integer limit;
    /** Only records from that offset will be considered ({@code offset}). */
    @QueryParameter("offset")
    private Integer offset;
    /** The start of the time filter, as a Unix timestamp ({@code start}). */
    @QueryParameter("start")
    private Integer start;
    /** The end of the time filter, as a Unix timestamp ({@code end}). */
    @QueryParameter("end")
    private Integer end;
    /**
     * Specifies which address book contacts to display. Available values are: 'all', 'routed', 'unrouted' ({@code
     * display}).
     */
    @QueryParameter("display")
    private String display;
}
