package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookContactsResponse;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;
import com.route4me.sdk.services.addressbook.BatchUpdateContactsRequest;

import java.util.Arrays;

/** Sets the same group and service time on several contacts with {@link AddressBookManagerV5#batchUpdateContacts}. */
public class BatchUpdateContactsV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 manager = new AddressBookManagerV5(apiKey);
        try {
            BatchUpdateContactsRequest request = new BatchUpdateContactsRequest();
            request.setAddressIds(Arrays.asList(12345L, 12346L));
            request.setAddressGroup("Midtown");
            request.setServiceTime(300L);
            AddressBookContactsResponse response = manager.batchUpdateContacts(request);
            System.out.println("Updated: " + (response.getResults() == null ? 0 : response.getResults().size()));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
