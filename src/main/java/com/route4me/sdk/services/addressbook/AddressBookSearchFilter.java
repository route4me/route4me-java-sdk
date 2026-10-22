package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The {@code filter} of an {@link AddressBookFilterSearchRequest}; filters left {@code null} are not sent. */
@Data
public class AddressBookSearchFilter {
    /** Text to search for in the contacts ({@code query}). */
    @SerializedName("query")
    private String query;

    /** Which contacts to return: {@code all}, {@code routed} or {@code unrouted} ({@code display}). */
    @SerializedName("display")
    private String display;
}
