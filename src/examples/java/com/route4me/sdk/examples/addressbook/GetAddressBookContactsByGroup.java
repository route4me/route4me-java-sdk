package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookManager;
import com.route4me.sdk.services.addressbook.AddressBookSearchResponse;

import java.util.Arrays;

/**
 * Lists the IDs and addresses of the contacts of an address book group with
 * {@link AddressBookManager#getAddressBookContactsByGroup}.
 */
public class GetAddressBookContactsByGroup {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManager manager = new AddressBookManager(apiKey);
        try {
            AddressBookSearchResponse response = manager.getAddressBookContactsByGroup("GROUP_ID_HERE",
                    Arrays.asList("address_id", "address_1"));
            System.out.println("Fields: " + response.getFields());
            if (response.getResults() != null) {
                for (Object row : response.getResults()) {
                    System.out.println(row);
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
