package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookManager;
import com.route4me.sdk.services.addressbook.Contact;
import com.route4me.sdk.services.addressbook.ContactsRequest;

import java.util.List;

/**
 * Takes a contact from {@link AddressBookManager#getContacts} and changes it with
 * {@link AddressBookManager#updateContact}.
 */
public class UpdateAddressBookContact {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManager addressBookManager = new AddressBookManager(apiKey);
        try {
            List<Contact> contacts = addressBookManager.getContacts(new ContactsRequest().setLimit(10));
            contacts.get(0).setFirstName("Johny");
            Contact contact = addressBookManager.updateContact(contacts.get(0));
            System.out.println(contact);
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
