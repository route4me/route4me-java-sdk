package com.route4me.sdk.examples.customers;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.customers.CustomerContact;
import com.route4me.sdk.services.customers.CustomerManager;
import com.route4me.sdk.services.customers.CustomerResource;
import com.route4me.sdk.services.customers.CustomerStoreRequest;
import java.util.ArrayList;
import java.util.List;

public class CreateCustomer {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        CustomerManager manager = new CustomerManager(apiKey);
        try {
            CustomerStoreRequest request = new CustomerStoreRequest();
            request.setName("Test Customer");
            request.setStatus(1);
            List<CustomerContact> contacts = new ArrayList<CustomerContact>();
            CustomerContact contact = new CustomerContact();
            contact.setType("primary");
            contact.setFirstName("John");
            contact.setLastName("Doe");
            contact.setEmail("john@example.com");
            contacts.add(contact);
            request.setContacts(contacts);
            CustomerResource result = manager.createCustomer(request);
            System.out.println("Created customer: " + result.getName());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
