package com.route4me.sdk.examples.customers;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.customers.CustomerListParameters;
import com.route4me.sdk.services.customers.CustomerListResource;
import com.route4me.sdk.services.customers.CustomerListResponse;
import com.route4me.sdk.services.customers.CustomerManager;

/** Lists customers with {@link CustomerManager#getCustomersList}. */
public class GetCustomers {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        CustomerManager manager = new CustomerManager(apiKey);
        try {
            CustomerListParameters params = new CustomerListParameters();
            params.setPage(1);
            params.setPerPage(10);
            CustomerListResponse response = manager.getCustomersList(params);
            if (response.getItems() != null) {
                for (CustomerListResource customer : response.getItems()) {
                    System.out.println("Customer: " + customer.getName() + " (ID: " + customer.getCustomerId() + ")");
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
