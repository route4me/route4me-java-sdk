package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookManager;
import com.route4me.sdk.services.addressbook.Contact;

import java.util.ArrayList;
import java.util.List;

public class DeleteAddressBookContact {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManager addressBookManager = new AddressBookManager(apiKey);
        Contact contact = new Contact();
        contact.setFirstName("Juan");
        contact.setLastName("Pimentel");
        contact.setAddress1("17205 RICHMOND TNPK, MILFORD, VA, 22514");
        contact.setCachedLat(38.024654);
        contact.setCachedLng(-77.338814);
        try {
            Contact responseObject = addressBookManager.createContact(contact);
            Number addressID = responseObject.getAddressId();
            List<Number> addressIDs = new ArrayList<>();
            addressIDs.add(addressID);
            System.out.println("Deleting Address ID: " + addressID);
            boolean response = addressBookManager.deleteContact(addressIDs);
            System.out.println(response);
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }

}
