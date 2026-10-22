package com.route4me.sdk.examples.addressbarcode;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbarcode.AddressBarcodeManager;
import com.route4me.sdk.services.addressbarcode.BarcodeDataRequest;
import com.route4me.sdk.services.addressbarcode.SaveAddressBarcodesRequest;
import com.route4me.sdk.services.addressbarcode.StatusResponse;

import java.util.ArrayList;
import java.util.List;

public class SaveAddressBarcodes {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBarcodeManager manager = new AddressBarcodeManager(apiKey);
        try {
            SaveAddressBarcodesRequest request = new SaveAddressBarcodesRequest();
            request.setRouteId("ROUTE_ID_HERE");
            request.setRouteDestinationId(123456L);

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
