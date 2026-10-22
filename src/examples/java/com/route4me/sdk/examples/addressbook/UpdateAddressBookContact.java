package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookContactV5;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;

/**
 * Reads a contact with {@link AddressBookManagerV5#getContactById} and changes its first name with
 * {@link AddressBookManagerV5#updateContact}, which replaces the deprecated {@code AddressBookManager.updateContact}.
 * Set {@code CONTACT_ID} to the address ID of a contact of a test account: the example changes it.
 */
public class UpdateAddressBookContact {

    public static void main(String[] args) {
        String contactId = System.getenv("CONTACT_ID");
        if (contactId == null || contactId.isEmpty()) {
            System.err.println("Set CONTACT_ID to the address ID of a contact of a test account; this example"
                    + " changes it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 addressBookManager = new AddressBookManagerV5(apiKey);
        try {
            long addressId = Long.parseLong(contactId);
            AddressBookContactV5 contact = addressBookManager.getContactById(addressId);
            if (contact == null) {
                System.err.println("No contact has the address ID " + contactId);
                return;
            }
            // The read contact keeps the address_1, cached_lat and cached_lng that the update requires.
            contact.setFirstName("Example");
            AddressBookContactV5 updated = addressBookManager.updateContact(addressId, contact);
            System.out.println(updated);
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
