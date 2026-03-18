package com.route4me.sdk.services.addressbarcode;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class BarcodeDataResponse {
    @SerializedName("route_id")
    private String routeId;

    @SerializedName("route_destination_id")
    private Long routeDestinationId;

    @SerializedName("order_id")
    private Long orderId;

    @SerializedName("barcode")
    private String barcode;

    @SerializedName("scan_type")
    private String scanType;

    @SerializedName("lat")
    private Double latitude;

    @SerializedName("lan")
    private Double longitude;

    @SerializedName("timestamp_date")
    private Long timestampDate;

    @SerializedName("timestamp_utc")
    private Long timestampUtc;

    @SerializedName("scanned_at")
    private String scannedAt;
}
