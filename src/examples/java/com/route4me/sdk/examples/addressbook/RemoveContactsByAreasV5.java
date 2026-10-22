package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookAreasFilter;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;

/**
 * Deletes the contacts that match a search text, in a background job, with
 * {@link AddressBookManagerV5#removeAddressBookContactsByAreas}.
 */
public class RemoveContactsByAreasV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 manager = new AddressBookManagerV5(apiKey);
        try {
            AddressBookAreasFilter filter = new AddressBookAreasFilter();
            filter.setQuery("TEST CONTACT");
            manager.removeAddressBookContactsByAreas(filter);
            System.out.println("The deletion was queued");
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
