package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookManager;
import com.route4me.sdk.services.addressbook.Contact;
import com.route4me.sdk.services.addressbook.ContactsRequest;

import java.util.List;

/**
 * Lists address book contacts with the deprecated {@link AddressBookManager#getContacts}. New code uses
 * {@code AddressBookManagerV5.getContacts}; see {@link GetContactsByQueryV5}.
 */
public class GetAddressBookContacts {

    // Shows a deprecated V4 address_book.php method (obsolete in the C# SDK) until it is removed.
    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManager addressBookManager = new AddressBookManager(apiKey);
        try {
            List<Contact> contacts = addressBookManager.getContacts(new ContactsRequest().setLimit(10));
            if (contacts == null) {
                return;
            }
            for (Contact c : contacts) {
                System.out.println(c);
            }
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }

}
