package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.responses.StatusResponse;
import com.route4me.sdk.services.addressbook.AddressBookManager;

/**
 * Deletes an address book group with {@link AddressBookManager#removeAddressBookGroup}. Set {@code GROUP_ID} to an
 * address book group of a test account: the example deletes it.
 */
public class RemoveAddressBookGroup {
    public static void main(String[] args) {
        String groupId = System.getenv("GROUP_ID");
        if (groupId == null || groupId.isEmpty()) {
            System.err.println("Set GROUP_ID to the ID of an address book group of a test account; this example"
                    + " deletes it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManager manager = new AddressBookManager(apiKey);
        try {
            StatusResponse response = manager.removeAddressBookGroup(groupId);
            System.out.println("Deleted: " + response.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
