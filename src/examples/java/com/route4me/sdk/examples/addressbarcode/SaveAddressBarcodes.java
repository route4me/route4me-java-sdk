package com.route4me.sdk.examples.addressbarcode;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.responses.StatusResponse;
import com.route4me.sdk.services.addressbarcode.AddressBarcodeManager;
import com.route4me.sdk.services.addressbarcode.BarcodeDataRequest;
import com.route4me.sdk.services.addressbarcode.SaveAddressBarcodesRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Saves a scanned barcode for a route destination with {@link AddressBarcodeManager#saveAddressBarcodes}. Set
 * {@code ROUTE_ID} and {@code ROUTE_DESTINATION_ID} to a route of a test account and one of its stops: the example
 * adds the barcode to the stop.
 */
public class SaveAddressBarcodes {
    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        String routeDestinationId = System.getenv("ROUTE_DESTINATION_ID");
        if (routeId == null || routeId.isEmpty() || routeDestinationId == null || routeDestinationId.isEmpty()) {
            System.err.println("Set ROUTE_ID and ROUTE_DESTINATION_ID to a route of a test account and one of its"
                    + " stops; this example adds a barcode to the stop.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBarcodeManager manager = new AddressBarcodeManager(apiKey);
        try {
            SaveAddressBarcodesRequest request = new SaveAddressBarcodesRequest();
            request.setRouteId(routeId);
            request.setRouteDestinationId(Long.valueOf(routeDestinationId));

            List<BarcodeDataRequest> barcodes = new ArrayList<BarcodeDataRequest>();
            BarcodeDataRequest barcode = new BarcodeDataRequest();
            barcode.setBarcode("BARCODE123");
            barcode.setScanType("QR");
            barcode.setLatitude(40.7128);
            barcode.setLongitude(-74.0060);
            barcode.setTimestampDate(System.currentTimeMillis() / 1000);
            barcode.setTimestampUtc(System.currentTimeMillis() / 1000);
            barcodes.add(barcode);

            request.setBarcodes(barcodes);
            StatusResponse response = manager.saveAddressBarcodes(request);
            System.out.println("Status: " + response.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
