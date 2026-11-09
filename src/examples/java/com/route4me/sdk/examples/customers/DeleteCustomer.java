package com.route4me.sdk.examples.customers;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.customers.CustomerManager;

/**
 * Deletes a customer with {@link CustomerManager#deleteCustomer}. Set {@code CUSTOMER_ID} to a customer of a test
 * account: the example deletes it.
 */
public class DeleteCustomer {
    public static void main(String[] args) {
        String customerId = System.getenv("CUSTOMER_ID");
        if (customerId == null || customerId.isEmpty()) {
            System.err.println("Set CUSTOMER_ID to the ID of a customer of a test account; this example deletes it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        CustomerManager manager = new CustomerManager(apiKey);
        try {
            manager.deleteCustomer(customerId);
            System.out.println("Customer deleted.");
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
