package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of {@link AddressBookManager#saveGeocodedAddressesToDatabase(String)}. */
@Data
public class SaveGeocodedAddressesResponse {
    /** Whether the addresses were saved ({@code status}). */
    @SerializedName("status")
    private Boolean status;

    /** The ID of the temporary optimization that held the addresses ({@code optimization_problem_id}). */
    @SerializedName("optimization_problem_id")
    private String optimizationProblemId;

    /** The ID of the temporary storage of the addresses ({@code temporary_addresses_storage_id}). */
    @SerializedName("temporary_addresses_storage_id")
    private String temporaryAddressesStorageId;

    /** The number of addresses ({@code address_count}). */
    @SerializedName("address_count")
    private Integer addressCount;
}
