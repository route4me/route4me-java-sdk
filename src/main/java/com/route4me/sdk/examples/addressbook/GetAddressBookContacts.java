package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.managers.AddressBookManager;
import com.route4me.sdk.model.Contact;

/**
 *
 * @author juan
 */
public class GetAddressBookContacts {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        AddressBookManager addressBookManager = route4me.getAddressBookManager();
        Contact responseObject = addressBookManager.getContacts(10, 5);
        for (Contact contact : responseObject.getResults()) {
            System.out.println("Address ID: " + contact.getAddress_id());
            System.out.println("First Name: " + contact.getFirst_name());
            System.out.println("Last Name: " + contact.getLast_name());
            System.out.println("Address 1: " + contact.getAddress_1());
            System.out.println("Address 2: " + contact.getAddress_2());
        }
    }

}
