package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The answer of {@link AddressBookManagerV5#getAddressBookGroups()}. */
@Data
public class AddressBookGroupsResponse {
    /** The groups ({@code results}). */
    @SerializedName("results")
    private List<AddressBookGroup> results;
}
