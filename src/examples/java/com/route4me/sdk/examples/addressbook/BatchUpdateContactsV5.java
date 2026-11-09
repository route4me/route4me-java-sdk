package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookContactsResponse;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;
import com.route4me.sdk.services.addressbook.BatchUpdateContactsRequest;

import java.util.List;

/**
 * Sets the same group and service time on several contacts with {@link AddressBookManagerV5#batchUpdateContacts}.
 * Set {@code CONTACT_IDS} to the comma-separated IDs of contacts of a test account: the example changes them.
 */
public class BatchUpdateContactsV5 {
    public static void main(String[] args) {
        List<Long> contactIds = ExampleInput.toLongs("CONTACT_IDS", System.getenv("CONTACT_IDS"));
        if (contactIds == null) {
            return;
        }
        if (contactIds.isEmpty()) {
            System.err.println("Set CONTACT_IDS to the comma-separated IDs of contacts of a test account;"
                    + " this example changes them.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 manager = new AddressBookManagerV5(apiKey);
        try {
            BatchUpdateContactsRequest request = new BatchUpdateContactsRequest();
            request.setAddressIds(contactIds);
            request.setAddressGroup("Midtown");
            request.setServiceTime(300L);
            AddressBookContactsResponse response = manager.batchUpdateContacts(request);
            int updated = response == null || response.getResults() == null ? 0 : response.getResults().size();
            System.out.println("Updated: " + updated);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
