package com.route4me.sdk.examples.addressbarcode;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbarcode.AddressBarcodeManager;
import com.route4me.sdk.services.addressbarcode.BarcodeDataResponse;
import com.route4me.sdk.services.addressbarcode.GetAddressBarcodesResponse;

/**
 * Lists the barcodes scanned on a route with {@link AddressBarcodeManager#getAddressBarcodes}. Set {@code ROUTE_ID}
 * to one of your routes.
 */
public class GetAddressBarcodes {
    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of one of your routes; this example lists its barcodes.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBarcodeManager manager = new AddressBarcodeManager(apiKey);
        try {
            GetAddressBarcodesResponse response = manager.getAddressBarcodes(routeId, null, 10, null);
            if (response.getData() != null) {
                for (BarcodeDataResponse barcode : response.getData()) {
                    System.out.println("Barcode: " + barcode.getBarcode() + " Type: " + barcode.getScanType());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
