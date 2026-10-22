package com.route4me.sdk.services.addressbook;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
/** The body of {@link AddressBookManagerV5#batchCreateContacts(BatchCreateContactsRequest)}. */
@Data
public class BatchCreateContactsRequest {
    /** The contacts to create ({@code data}). */
    @SerializedName("data") private List<AddressBookContactV5> data;
}
