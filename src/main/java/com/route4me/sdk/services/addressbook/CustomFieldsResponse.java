package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The answer of {@link AddressBookManagerV5#getCustomFields()}. */
@Data
public class CustomFieldsResponse {
    /** The fields of the contacts, custom data fields included ({@code custom_fields}). */
    @SerializedName("custom_fields")
    private List<CustomField> customFields;
}
