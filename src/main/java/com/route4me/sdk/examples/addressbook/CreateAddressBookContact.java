package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.services.addressbook.AddressBookManager;
import com.route4me.sdk.services.addressbook.Contact;

/**
 *
 * @author juan
 */
public class CreateAddressBookContact {
    
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        AddressBookManager addressBookManager = route4me.getAddressBookManager();
        Contact contact = new Contact();
        contact.setFirst_name("Juan");
        contact.setLast_name("Pimentel");
        contact.setAddress_1("17205 RICHMOND TNPK, MILFORD, VA, 22514");
        contact.setCached_lat(38.024654);
        contact.setCached_lng(-77.338814);
        Contact responseObject = addressBookManager.createContact(contact);
        System.out.println("Address ID: " + responseObject.getAddress_id());
        System.out.println("First Name: " + responseObject.getFirst_name());
        System.out.println("Last Name: " + responseObject.getLast_name());
        System.out.println("Address 1: " + responseObject.getAddress_1());
        System.out.println("Address 2: " + responseObject.getAddress_2());

        
        
    }    
    
    
}
