package com.route4me.sdk.services.customers;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

public class CustomerManager extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String CUSTOMERS_EP = V5_BASE + "/customers";
    private static final String CUSTOMERS_LIST_EP = CUSTOMERS_EP + "/list";

    public CustomerManager(String apiKey) {
        super(apiKey);
    }

    public CustomerManager(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    private static URIBuilder v5Builder(String endpoint) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        builder.setPath(endpoint);
        return builder;
    }

    public CustomerListResponse getCustomersList(CustomerListParameters parameters) throws APIException {
        URIBuilder builder = v5Builder(CUSTOMERS_LIST_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(parameters), CustomerListResponse.class);
    }

    public CustomerShowResource getCustomerById(String customerId) throws APIException {
        URIBuilder builder = v5Builder(CUSTOMERS_EP + "/" + customerId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, CustomerShowResource.class);
    }

    public CustomerResource createCustomer(CustomerStoreRequest request) throws APIException {
        URIBuilder builder = v5Builder(CUSTOMERS_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), CustomerResource.class);
    }

    public CustomerResource updateCustomer(String customerId, CustomerStoreRequest request) throws APIException {
        URIBuilder builder = v5Builder(CUSTOMERS_EP + "/" + customerId);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(request), CustomerResource.class);
    }

    public void deleteCustomer(String customerId) throws APIException {
        URIBuilder builder = v5Builder(CUSTOMERS_EP + "/" + customerId);
        this.makeRequest(RequestMethod.DELETE, builder, "", String.class);
    }
}
