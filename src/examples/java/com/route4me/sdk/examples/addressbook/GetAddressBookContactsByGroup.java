package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookManager;
import com.route4me.sdk.services.addressbook.AddressBookSearchResponse;

import java.util.Arrays;

/**
 * Lists the IDs and addresses of the contacts of an address book group with
 * {@link AddressBookManager#getAddressBookContactsByGroup}. Set {@code GROUP_ID} to one of your address book groups.
 */
public class GetAddressBookContactsByGroup {
    public static void main(String[] args) {
        String groupId = System.getenv("GROUP_ID");
        if (groupId == null || groupId.isEmpty()) {
            System.err.println("Set GROUP_ID to the ID of one of your address book groups; this example lists its"
                    + " contacts.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManager manager = new AddressBookManager(apiKey);
        try {
            AddressBookSearchResponse response = manager.getAddressBookContactsByGroup(groupId,
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
