package com.route4me.sdk.examples.addressbook;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookContactV5;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;
import com.route4me.sdk.services.addressbook.BatchCreateContactsRequest;
import com.route4me.sdk.services.addressbook.StatusResponse;
import java.util.ArrayList;
import java.util.List;
public class BatchCreateContactsV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 manager = new AddressBookManagerV5(apiKey);
        try {
            BatchCreateContactsRequest request = new BatchCreateContactsRequest();
            List<AddressBookContactV5> contacts = new ArrayList<AddressBookContactV5>();
            AddressBookContactV5 c = new AddressBookContactV5();
            c.setAddress1("123 Main St, New York, NY");
            c.setAddressAlias("Contact 1");
            c.setCachedLat(40.7128);
            c.setCachedLng(-74.0060);
            contacts.add(c);
            request.setData(contacts);
            StatusResponse response = manager.batchCreateContacts(request);
            System.out.println("Status: " + response.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
