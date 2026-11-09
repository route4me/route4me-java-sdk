package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookContactV5;
import com.route4me.sdk.services.addressbook.AddressBookContactsQuery;
import com.route4me.sdk.services.addressbook.AddressBookContactsResponse;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;

import java.util.Arrays;

/**
 * Searches the address book with query parameters, returning only some fields, with
 * {@link AddressBookManagerV5#getContacts(AddressBookContactsQuery)}.
 */
public class GetContactsByQueryV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 manager = new AddressBookManagerV5(apiKey);
        try {
            AddressBookContactsQuery query = new AddressBookContactsQuery();
            query.setQuery("Main St");
            query.setFields(Arrays.asList("address_id", "address_alias", "address_1"));
            query.setDisplay("all");
            query.setLimit(20);
            AddressBookContactsResponse response = manager.getContacts(query);
            if (response.getResults() != null) {
                for (AddressBookContactV5 contact : response.getResults()) {
                    System.out.println("Contact " + contact.getAddressId() + ": " + contact.getAddress1());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
