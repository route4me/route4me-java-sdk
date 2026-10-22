package com.route4me.sdk.services.addressbarcode;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of {@link AddressBarcodeManager#saveAddressBarcodes(SaveAddressBarcodesRequest)}. */
@Data
public class StatusResponse {
    /** Whether the request succeeded ({@code status}). */
    @SerializedName("status")
    private Boolean status;
}
