package com.route4me.sdk.examples.addressbarcode;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbarcode.AddressBarcodeManager;
import com.route4me.sdk.services.addressbarcode.BarcodeDataResponse;
import com.route4me.sdk.services.addressbarcode.GetAddressBarcodesResponse;

/** Lists the barcodes scanned at a route destination with {@link AddressBarcodeManager#getAddressBarcodes}. */
public class GetAddressBarcodes {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBarcodeManager manager = new AddressBarcodeManager(apiKey);
        try {
            GetAddressBarcodesResponse response = manager.getAddressBarcodes("ROUTE_ID_HERE", null, 10, null);
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
