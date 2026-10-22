package com.route4me.sdk.examples.geocoding;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.geocoding.GeocoderResponse;
import com.route4me.sdk.services.geocoding.GeocodingManager;
import com.route4me.sdk.services.routing.Address;
import java.util.Arrays;
import java.util.List;

/**
 * Uploads two addresses as JSON to the temporary storage of the bulk geocoder with
 * {@link GeocodingManager#jsonUploadGeocoder}, and prints the storage ID that the websocket geocoder or an automatic
 * territories job takes.
 */
public class JsonUploadGeocoder {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        GeocodingManager manager = new GeocodingManager(apiKey);
        List<Address> addresses = Arrays.asList(new Address("151 Arbor Way Milledgeville GA 31061"),
                new Address("230 Arbor Way Milledgeville GA 31061"));
        try {
            GeocoderResponse response = manager.jsonUploadGeocoder(addresses);
            System.out.println("Storage ID: " + response.getTemporaryAddressesStorageID() + ", addresses: "
                    + response.getAddressCount());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
