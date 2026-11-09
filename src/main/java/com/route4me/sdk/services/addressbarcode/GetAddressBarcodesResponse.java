package com.route4me.sdk.services.addressbarcode;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * A page of scanned barcodes, the answer of
 * {@link AddressBarcodeManager#getAddressBarcodes(String, Long, Integer, String)}.
 */
@Data
public class GetAddressBarcodesResponse {
    /** The status of the request ({@code status}). */
    @SerializedName("status")
    private String status;

    /**
     * The cursor of the next page, to pass as {@code cursor}; {@code null} on the last page ({@code next_page_cursor}).
     */
    @SerializedName("next_page_cursor")
    private String nextPageCursor;

    /** The barcodes of this page ({@code data}). */
    @SerializedName("data")
    private List<BarcodeDataResponse> data;
}
