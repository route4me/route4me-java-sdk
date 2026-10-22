package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookManager;
import com.route4me.sdk.services.addressbook.AddressBookSearchResponse;
import com.route4me.sdk.services.addressbook.ContactsRequest;

/**
 * Searches the address book for a text and prints some fields of the first 20 contacts, with
 * {@link AddressBookManager#searchAddressBookLocation}.
 */
public class SearchAddressBookLocation {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManager manager = new AddressBookManager(apiKey);
        try {
            ContactsRequest request = new ContactsRequest().setQuery("Test")
                    .setFields("address_1,address_group,first_name,last_name").setOffset(0).setLimit(20);
            AddressBookSearchResponse response = manager.searchAddressBookLocation(request);
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
