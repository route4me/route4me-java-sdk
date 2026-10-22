package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookGroup;
import com.route4me.sdk.services.addressbook.AddressBookManager;

/** Changes the color of an address book group with {@link AddressBookManager#updateAddressBookGroup}. */
public class UpdateAddressBookGroup {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManager manager = new AddressBookManager(apiKey);
        try {
            AddressBookGroup change = new AddressBookGroup();
            change.setGroupId("GROUP_ID_HERE");
            change.setGroupColor("ff7f50");
            AddressBookGroup updated = manager.updateAddressBookGroup(change);
            System.out.println("Group color: " + updated.getGroupColor());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
