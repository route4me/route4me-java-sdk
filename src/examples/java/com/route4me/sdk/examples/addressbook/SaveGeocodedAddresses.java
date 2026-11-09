package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookManager;
import com.route4me.sdk.services.addressbook.SaveGeocodedAddressesResponse;

/**
 * Saves the geocoded addresses of a temporary optimization to the address book with
 * {@link AddressBookManager#saveGeocodedAddressesToDatabase}. Set {@code OPTIMIZATION_PROBLEM_ID} to an optimization
 * of a test account: the example adds its addresses to the address book.
 */
public class SaveGeocodedAddresses {
    public static void main(String[] args) {
        String optimizationProblemId = System.getenv("OPTIMIZATION_PROBLEM_ID");
        if (optimizationProblemId == null || optimizationProblemId.isEmpty()) {
            System.err.println("Set OPTIMIZATION_PROBLEM_ID to the ID of an optimization of a test account; this"
                    + " example saves its geocoded addresses to the address book.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManager manager = new AddressBookManager(apiKey);
        try {
            SaveGeocodedAddressesResponse response = manager.saveGeocodedAddressesToDatabase(optimizationProblemId);
            System.out.println("Saved: " + response.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
