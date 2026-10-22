package com.route4me.sdk.examples.addressbook;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookContactV5;
import com.route4me.sdk.services.addressbook.AddressBookContactsBodyRequest;
import com.route4me.sdk.services.addressbook.AddressBookContactsResponse;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;
/** Lists address book contacts one page at a time with {@link AddressBookManagerV5#getContactsPaginated}. */
public class GetContactsV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 manager = new AddressBookManagerV5(apiKey);
        try {
            AddressBookContactsBodyRequest request = new AddressBookContactsBodyRequest();
            request.setPage(1);
            request.setPerPage(10);
            AddressBookContactsResponse response = manager.getContactsPaginated(request);
            if (response.getResults() != null) {
                for (AddressBookContactV5 c : response.getResults()) {
                    System.out.println("Contact: " + c.getAddressAlias());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
