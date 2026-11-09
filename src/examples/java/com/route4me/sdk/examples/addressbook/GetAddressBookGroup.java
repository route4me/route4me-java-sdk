package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookGroup;
import com.route4me.sdk.services.addressbook.AddressBookManager;

/**
 * Gets one address book group with {@link AddressBookManager#getAddressBookGroup}. Set {@code GROUP_ID} to one of
 * your address book groups.
 */
public class GetAddressBookGroup {
    public static void main(String[] args) {
        String groupId = System.getenv("GROUP_ID");
        if (groupId == null || groupId.isEmpty()) {
            System.err.println("Set GROUP_ID to the ID of one of your address book groups; this example reads it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManager manager = new AddressBookManager(apiKey);
        try {
            AddressBookGroup group = manager.getAddressBookGroup(groupId);
            System.out.println("Group: " + group.getGroupName() + ", color " + group.getGroupColor());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
