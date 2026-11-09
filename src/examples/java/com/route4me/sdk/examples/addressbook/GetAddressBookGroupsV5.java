package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookGroup;
import com.route4me.sdk.services.addressbook.AddressBookGroupsResponse;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;

/** Lists the address book groups with {@link AddressBookManagerV5#getAddressBookGroups()}. */
public class GetAddressBookGroupsV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 manager = new AddressBookManagerV5(apiKey);
        try {
            AddressBookGroupsResponse response = manager.getAddressBookGroups();
            if (response.getResults() != null) {
                for (AddressBookGroup group : response.getResults()) {
                    System.out.println("Group: " + group.getGroupName() + " (ID: " + group.getGroupId() + ")");
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
