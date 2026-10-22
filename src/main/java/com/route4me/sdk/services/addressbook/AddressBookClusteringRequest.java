package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * The body of {@link AddressBookManagerV5#getAddressBookContactsClustering(AddressBookClusteringRequest)}. Fields
 * left {@code null} are not sent.
 */
@Data
public class AddressBookClusteringRequest {
    /** Text to search for in the contacts ({@code query}). */
    @SerializedName("query")
    private String query;

    /** The clustering options ({@code clustering}). */
    @SerializedName("clustering")
    private AddressBookClustering clustering;

    /** The filters ({@code filter}). */
    @SerializedName("filter")
    private AddressBookContactsFilter filter;
}
