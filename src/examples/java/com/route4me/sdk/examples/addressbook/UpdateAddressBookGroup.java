package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookGroup;
import com.route4me.sdk.services.addressbook.AddressBookManager;

/**
 * Changes the color of an address book group with {@link AddressBookManager#updateAddressBookGroup}. Set
 * {@code GROUP_ID} to an address book group of a test account: the example changes it.
 */
public class UpdateAddressBookGroup {
    public static void main(String[] args) {
        String groupId = System.getenv("GROUP_ID");
        if (groupId == null || groupId.isEmpty()) {
            System.err.println("Set GROUP_ID to the ID of an address book group of a test account; this example"
                    + " changes its color.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManager manager = new AddressBookManager(apiKey);
        try {
            AddressBookGroup change = new AddressBookGroup();
            change.setGroupId(groupId);
            change.setGroupColor("ff7f50");
            AddressBookGroup updated = manager.updateAddressBookGroup(change);
            System.out.println("Group color: " + updated.getGroupColor());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
