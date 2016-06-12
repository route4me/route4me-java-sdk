package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.services.addressbook.AddressBookManager;
import com.route4me.sdk.services.addressbook.Contact;
import com.route4me.sdk.model.Response;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juan
 */
public class DeleteAddressBookContact {

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
        Number addressID = responseObject.getAddress_id();
        List<Number> addressIDs = new ArrayList<>();
        addressIDs.add(addressID);
        System.err.println("Deleting Address ID: " + addressID);
        Response response = addressBookManager.deleteContact(addressIDs);
        System.out.println(response.getResponseBody());
    }

}
