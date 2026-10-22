package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookGroup;
import com.route4me.sdk.services.addressbook.AddressBookManager;

/** Gets one address book group with {@link AddressBookManager#getAddressBookGroup}. */
public class GetAddressBookGroup {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManager manager = new AddressBookManager(apiKey);
        try {
            AddressBookGroup group = manager.getAddressBookGroup("GROUP_ID_HERE");
            System.out.println("Group: " + group.getGroupName() + ", color " + group.getGroupColor());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
