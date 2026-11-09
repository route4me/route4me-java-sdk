package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookFilterSearchRequest;
import com.route4me.sdk.services.addressbook.AddressBookManager;
import com.route4me.sdk.services.addressbook.AddressBookSearchFilter;
import com.route4me.sdk.services.addressbook.AddressBookSearchResponse;

import java.util.Arrays;

/**
 * Searches the address book for a text, returning some fields of the first 10 contacts, with
 * {@link AddressBookManager#searchAddressBookContactsByFilter}.
 */
public class SearchAddressBookContactsByFilter {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManager manager = new AddressBookManager(apiKey);
        try {
            AddressBookSearchFilter filter = new AddressBookSearchFilter();
            filter.setQuery("Louisville");
            filter.setDisplay("all");
            AddressBookFilterSearchRequest request = new AddressBookFilterSearchRequest();
            request.setFields(Arrays.asList("address_id", "address_1", "address_group"));
            request.setFilter(filter);
            AddressBookSearchResponse response = manager.searchAddressBookContactsByFilter(request, 0, 10);
            System.out.println("Found " + response.getTotal() + " contacts");
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
