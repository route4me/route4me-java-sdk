package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookManager;
import com.route4me.sdk.services.addressbook.Contact;

public class CreateAddressBookContact {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        AddressBookManager addressBookManager = new AddressBookManager(apiKey);
        Contact contact = new Contact();
        contact.setFirstName("Juan");
        contact.setLastName("Pimentel");
        contact.setAddress1("17205 RICHMOND TNPK, MILFORD, VA, 22514");
        contact.setCachedLat(38.024654);
        contact.setCachedLng(-77.338814);
        try {
            Contact responseObject = addressBookManager.createContact(contact);
            System.out.println(responseObject.toString());
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
