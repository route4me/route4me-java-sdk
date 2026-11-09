package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookGroup;
import com.route4me.sdk.services.addressbook.AddressBookManager;

/** Lists the first address book groups with {@link AddressBookManager#getAddressBookGroups}. */
public class GetAddressBookGroups {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManager manager = new AddressBookManager(apiKey);
        try {
            AddressBookGroup[] groups = manager.getAddressBookGroups(0, 20);
            for (AddressBookGroup group : groups) {
                System.out.println("Group: " + group.getGroupName() + " (ID: " + group.getGroupId() + ")");
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
