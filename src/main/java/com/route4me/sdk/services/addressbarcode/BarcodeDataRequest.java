package com.route4me.sdk.services.addressbarcode;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** One scanned barcode to save, in a {@link SaveAddressBarcodesRequest}. */
@Data
public class BarcodeDataRequest {
    /** The scanned barcode ({@code barcode}). */
    @SerializedName("barcode")
    private String barcode;

    /** The kind of scan, such as {@code DELIVERY} ({@code scan_type}). */
    @SerializedName("scan_type")
    private String scanType;

    /** The latitude where the barcode was scanned ({@code lat}). */
    @SerializedName("lat")
    private double latitude;

    /** The longitude where the barcode was scanned. The SDK sends it as {@code lan}. */
    @SerializedName("lan")
    private double longitude;

    /** The time of the scan on the device, as a Unix timestamp in seconds ({@code timestamp_date}). */
    @SerializedName("timestamp_date")
    private long timestampDate;

    /** The time of the scan in UTC, as a Unix timestamp in seconds ({@code timestamp_utc}). */
    @SerializedName("timestamp_utc")
    private long timestampUtc;

    /** The date and time of the scan, such as {@code 2021-10-23 22:10:16} ({@code scanned_at}). */
    @SerializedName("scanned_at")
    private String scannedAt;
}
