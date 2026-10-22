package com.route4me.sdk.services.addressbarcode;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class BarcodeDataRequest {
    @SerializedName("barcode")
    private String barcode;

    @SerializedName("scan_type")
    private String scanType;

    @SerializedName("lat")
    private double latitude;

    @SerializedName("lan")
    private double longitude;

    @SerializedName("timestamp_date")
    private long timestampDate;

    @SerializedName("timestamp_utc")
    private long timestampUtc;

    @SerializedName("scanned_at")
    private String scannedAt;
}
