package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookManager;
import com.route4me.sdk.services.addressbook.Contact;
import com.route4me.sdk.services.addressbook.ContactsRequest;

import java.util.List;

/**
 * @author juan
 */
public class GetAddressBookContact {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        AddressBookManager addressBookManager = new AddressBookManager(apiKey);
        try {
            List<Contact> contacts = addressBookManager.getContacts(new ContactsRequest().setLimit(10));
            Number addressID = contacts.get(0).getAddressId();
            System.out.println(addressID);
            Contact contact = addressBookManager.getContact(addressID);
            System.out.println(contact.toString());
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }

}
