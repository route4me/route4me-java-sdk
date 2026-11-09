package com.route4me.sdk.services.addressbarcode;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * The body of {@link AddressBarcodeManager#saveAddressBarcodes(SaveAddressBarcodesRequest)}: the barcodes
 * scanned at one route destination. All three fields are required.
 */
@Data
public class SaveAddressBarcodesRequest {
    /** The ID of the route ({@code route_id}). */
    @SerializedName("route_id")
    private String routeId;

    /** The ID of the route destination the barcodes belong to ({@code route_destination_id}). */
    @SerializedName("route_destination_id")
    private Long routeDestinationId;

    /** The barcodes to save for the destination ({@code barcodes}). */
    @SerializedName("barcodes")
    private List<BarcodeDataRequest> barcodes;
}
