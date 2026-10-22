package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookManager;
import com.route4me.sdk.services.addressbook.SaveGeocodedAddressesResponse;

/**
 * Saves the geocoded addresses of a temporary optimization to the address book with
 * {@link AddressBookManager#saveGeocodedAddressesToDatabase}.
 */
public class SaveGeocodedAddresses {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManager manager = new AddressBookManager(apiKey);
        try {
            SaveGeocodedAddressesResponse response = manager.saveGeocodedAddressesToDatabase(
                    "TEMPORARY_OPTIMIZATION_PROBLEM_ID_HERE");
            System.out.println("Saved: " + response.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
