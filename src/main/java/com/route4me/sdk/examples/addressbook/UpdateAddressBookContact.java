package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.services.addressbook.AddressBookManager;
import com.route4me.sdk.services.addressbook.Contact;

/**
 *
 * @author juan
 */
public class UpdateAddressBookContact {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        AddressBookManager addressBookManager = route4me.getAddressBookManager();
        Contact responseObject = addressBookManager.getContacts(10, 5);
        Contact contact = responseObject.getResults().get(0);
        System.out.println("Updating Address ID: " + contact.getAddress_id());
        contact.setFirst_name(contact.getFirst_name() + " Updated");
        responseObject = addressBookManager.updateContact(contact);
        System.out.println("Address ID: " + responseObject.getAddress_id());
        System.out.println("First Name: " + responseObject.getFirst_name());
        System.out.println("Last Name: " + responseObject.getLast_name());
        System.out.println("Address 1: " + responseObject.getAddress_1());
        System.out.println("Address 2: " + responseObject.getAddress_2());
    }

}
