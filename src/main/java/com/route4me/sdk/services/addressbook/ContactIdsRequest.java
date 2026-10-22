package com.route4me.sdk.services.addressbook;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
/** The body of the methods of {@link AddressBookManagerV5} that take contact IDs. */
@Data
public class ContactIdsRequest {
    /** The contact IDs ({@code address_ids}). */
    @SerializedName("address_ids") private long[] addressIds;
}
