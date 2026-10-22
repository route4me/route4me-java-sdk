package com.route4me.sdk.services.addressbook;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
/** The answer of {@link AddressBookManagerV5#getCustomFields()}. */
@Data
public class CustomFieldsResponse {
    /** The custom fields of the address book ({@code data}). */
    @SerializedName("data") private List<String> data;
}
