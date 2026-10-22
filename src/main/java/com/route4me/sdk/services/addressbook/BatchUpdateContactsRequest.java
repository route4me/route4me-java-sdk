package com.route4me.sdk.services.addressbook;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
/** The body of {@link AddressBookManagerV5#batchUpdateContacts(BatchUpdateContactsRequest)}. */
@Data
public class BatchUpdateContactsRequest {
    /** The contacts to update ({@code data}). */
    @SerializedName("data") private List<AddressBookContactV5> data;
}
