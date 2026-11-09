package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookContactV5;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;

/**
 * Creates an address book contact with {@link AddressBookManagerV5#addContact}, which replaces the deprecated
 * {@code AddressBookManager.createContact} of the V4 {@code address_book.php} endpoint.
 */
public class CreateAddressBookContact {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 addressBookManager = new AddressBookManagerV5(apiKey);
        AddressBookContactV5 contact = new AddressBookContactV5();
        contact.setFirstName("Juan");
        contact.setLastName("Pimentel");
        contact.setAddress1("17205 RICHMOND TNPK, MILFORD, VA, 22514");
        contact.setCachedLat(38.024654);
        contact.setCachedLng(-77.338814);
        try {
            AddressBookContactV5 created = addressBookManager.addContact(contact);
            System.out.println(created);
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
