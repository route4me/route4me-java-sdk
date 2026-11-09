package com.route4me.sdk.services.addressbarcode;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * One scanned barcode of a route destination, as
 * {@link AddressBarcodeManager#getAddressBarcodes(String, Long, Integer, String)} returns it.
 */
@Data
public class BarcodeDataResponse {
    /** The ID of the route ({@code route_id}). */
    @SerializedName("route_id")
    private String routeId;

    /** The ID of the route destination the barcode belongs to ({@code route_destination_id}). */
    @SerializedName("route_destination_id")
    private Long routeDestinationId;

    /** The ID of the order of the destination, if any ({@code order_id}). */
    @SerializedName("order_id")
    private Long orderId;

    /** The scanned barcode ({@code barcode}). */
    @SerializedName("barcode")
    private String barcode;

    /** The kind of scan, such as {@code DELIVERY} ({@code scan_type}). */
    @SerializedName("scan_type")
    private String scanType;

    /** The latitude where the barcode was scanned ({@code lat}). */
    @SerializedName("lat")
    private Double latitude;

    /**
     * The longitude where the barcode was scanned ({@code lng}). An answer that names it {@code lan}, as the C# SDK
     * reads it, is read too.
     */
    @SerializedName(value = "lng", alternate = {"lan"})
    private Double longitude;

    /** The time of the scan on the device, as a Unix timestamp in seconds ({@code timestamp_date}). */
    @SerializedName("timestamp_date")
    private Long timestampDate;

    /** The time of the scan in UTC, as a Unix timestamp in seconds ({@code timestamp_utc}). */
    @SerializedName("timestamp_utc")
    private Long timestampUtc;

    /** The date and time of the scan, such as {@code 2021-10-23 22:10:16} ({@code scanned_at}). */
    @SerializedName("scanned_at")
    private String scannedAt;
}
