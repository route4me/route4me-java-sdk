package com.route4me.sdk.examples.customers;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.customers.CustomerManager;

/** Deletes a customer with {@link CustomerManager#deleteCustomer}. */
public class DeleteCustomer {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        CustomerManager manager = new CustomerManager(apiKey);
        try {
            String customerId = "CUSTOMER_ID_HERE";
            manager.deleteCustomer(customerId);
            System.out.println("Customer deleted.");
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
