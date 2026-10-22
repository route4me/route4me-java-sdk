package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookContactV5;
import com.route4me.sdk.services.addressbook.AddressBookContactsPaginatedQuery;
import com.route4me.sdk.services.addressbook.AddressBookContactsResponse;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;

/**
 * Lists the routed contacts one page at a time with query parameters, with
 * {@link AddressBookManagerV5#getContactsPaginated(AddressBookContactsPaginatedQuery)}.
 */
public class GetContactsPaginatedByQueryV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 manager = new AddressBookManagerV5(apiKey);
        try {
            AddressBookContactsPaginatedQuery query = new AddressBookContactsPaginatedQuery();
            query.setDisplay("routed");
            query.setPage(1);
            query.setPerPage(30);
            AddressBookContactsResponse response = manager.getContactsPaginated(query);
            if (response.getResults() != null) {
                for (AddressBookContactV5 contact : response.getResults()) {
                    System.out.println("Contact: " + contact.getAddressAlias());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
