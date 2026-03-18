package com.route4me.sdk.services.addressbarcode;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class SaveAddressBarcodesRequest {
    @SerializedName("route_id")
    private String routeId;

    @SerializedName("route_destination_id")
    private long routeDestinationId;

    @SerializedName("barcodes")
    private List<BarcodeDataRequest> barcodes;
}
