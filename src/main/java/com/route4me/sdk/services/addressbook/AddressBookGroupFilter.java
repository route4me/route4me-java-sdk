package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The rules a contact must match to be in an {@link AddressBookGroup}. */
@Data
public class AddressBookGroupFilter {
    /** How the rules combine: {@code AND} or {@code OR} ({@code condition}). */
    @SerializedName("condition")
    private String condition;

    /** The rules ({@code rules}). */
    @SerializedName("rules")
    private List<AddressBookGroupRule> rules;
}
