package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookManager;
import com.route4me.sdk.services.addressbook.StatusResponse;

/** Deletes an address book group with {@link AddressBookManager#removeAddressBookGroup}. */
public class RemoveAddressBookGroup {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManager manager = new AddressBookManager(apiKey);
        try {
            StatusResponse response = manager.removeAddressBookGroup("GROUP_ID_HERE");
            System.out.println("Deleted: " + response.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
