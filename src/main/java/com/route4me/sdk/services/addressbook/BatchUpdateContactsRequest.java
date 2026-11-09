package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * The body of {@link AddressBookManagerV5#batchUpdateContacts(BatchUpdateContactsRequest)}: the IDs of the contacts
 * to update, and the contact fields to set on all of them. Fields left {@code null} are not sent.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BatchUpdateContactsRequest extends AddressBookContactV5 {
    /** The IDs of the contacts to update ({@code address_ids}). */
    @SerializedName("address_ids")
    private List<Long> addressIds;

    /** The ID of the city of the contacts ({@code city_id}). */
    @SerializedName("city_id")
    private Long cityId;

    /** Whether the contacts are marked as deleted ({@code is_deleted}). */
    @SerializedName("is_deleted")
    private Boolean isDeleted;
}
