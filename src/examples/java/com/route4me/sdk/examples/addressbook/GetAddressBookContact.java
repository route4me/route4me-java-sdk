package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookManager;
import com.route4me.sdk.services.addressbook.Contact;

/**
 * Reads one contact with {@link AddressBookManager#getContact}. Set {@code CONTACT_ID} to the address ID of a contact
 * of the account.
 */
public class GetAddressBookContact {

    public static void main(String[] args) {
        String contactId = System.getenv("CONTACT_ID");
        if (contactId == null || contactId.isEmpty()) {
            System.err.println("Set CONTACT_ID to the address ID of a contact of the account.");
            return;
        }
        Long id = ExampleInput.toLong("CONTACT_ID", contactId);
        if (id == null) {
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManager addressBookManager = new AddressBookManager(apiKey);
        try {
            Contact contact = addressBookManager.getContact(id);
            System.out.println(contact == null ? "No contact has the address ID " + contactId : contact.toString());
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }

}
