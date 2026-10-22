package com.route4me.sdk.services.addressbook;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
/** The answer of the contact list methods of {@link AddressBookManagerV5}. */
@Data
public class AddressBookContactsResponse {
    /** The contacts ({@code results}). */
    @SerializedName("results") private List<AddressBookContactV5> results;
    /** Total number of the returned address book contacts ({@code total}). */
    @SerializedName("total") private Integer total;
}
