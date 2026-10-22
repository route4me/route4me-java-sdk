package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookManager;
import com.route4me.sdk.services.addressbook.Contact;

/**
 * Reads a contact with {@link AddressBookManager#getContact} and changes its first name with
 * {@link AddressBookManager#updateContact}. Set {@code CONTACT_ID} to the address ID of a contact of a test account:
 * the example changes it.
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
        AddressBookManager addressBookManager = new AddressBookManager(apiKey);
        try {
            Contact contact = addressBookManager.getContact(Long.valueOf(contactId));
            if (contact == null) {
                System.err.println("No contact has the address ID " + contactId);
                return;
            }
            contact.setFirstName("Example");
            Contact updated = addressBookManager.updateContact(contact);
            System.out.println(updated);
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
