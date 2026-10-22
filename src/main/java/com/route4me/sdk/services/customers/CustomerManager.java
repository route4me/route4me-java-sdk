package com.route4me.sdk.services.customers;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 * Manages the customers of the account through the V5 API ({@code https://wh.route4me.com/modules/api/v5.0/customers}).
 */
public class CustomerManager extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String CUSTOMERS_EP = V5_BASE + "/customers";
    private static final String CUSTOMERS_LIST_EP = CUSTOMERS_EP + "/list";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     */
    public CustomerManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
    public CustomerManager(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    private static URIBuilder v5Builder(String endpoint, Object... segments) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        Manager.withPathSegments(builder, endpoint, segments);
        return builder;
    }

    /**
     * Lists customers, with filters, sorting and paging: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/customers/list}, with the parameters as the JSON body.
     *
     * @param parameters the filters, sort order, page and fields
     * @return the page of customers
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public CustomerListResponse getCustomersList(CustomerListParameters parameters) throws APIException {
        URIBuilder builder = v5Builder(CUSTOMERS_LIST_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(parameters), CustomerListResponse.class);
    }

    /**
     * Gets one customer: {@code GET https://wh.route4me.com/modules/api/v5.0/customers/{customerId}}.
     *
     * @param customerId the customer ID
     * @return the customer, with its location count and accountable person
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public CustomerShowResource getCustomerById(String customerId) throws APIException {
        URIBuilder builder = v5Builder(CUSTOMERS_EP, customerId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, CustomerShowResource.class);
    }

    /**
     * Creates a customer: {@code POST https://wh.route4me.com/modules/api/v5.0/customers}, with the request as the JSON
     * body.
     *
     * @param request the customer; {@code name} and {@code accountable_person_id} are required
     * @return the created customer
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if a required field is missing; nothing is sent
     */
    public CustomerResource createCustomer(CustomerStoreRequest request) throws APIException {
        if (request == null || request.getName() == null || request.getName().isEmpty()) {
            throw new IllegalArgumentException("name is required");
        }
        if (request.getAccountablePersonId() == null) {
            throw new IllegalArgumentException("accountable_person_id is required");
        }
        URIBuilder builder = v5Builder(CUSTOMERS_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), CustomerResource.class);
    }

    /**
     * Updates a customer: {@code PUT https://wh.route4me.com/modules/api/v5.0/customers/{customerId}}, with the request
     * as the JSON body.
     *
     * @param customerId the customer ID
     * @param request the new values of the customer
     * @return the updated customer
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public CustomerResource updateCustomer(String customerId, CustomerStoreRequest request) throws APIException {
        URIBuilder builder = v5Builder(CUSTOMERS_EP, customerId);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(request), CustomerResource.class);
    }

    /**
     * Deletes a customer: {@code DELETE https://wh.route4me.com/modules/api/v5.0/customers/{customerId}}. The API
     * answers {@code 204 No Content}.
     *
     * @param customerId the customer ID
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public void deleteCustomer(String customerId) throws APIException {
        URIBuilder builder = v5Builder(CUSTOMERS_EP, customerId);
        this.makeRequest(RequestMethod.DELETE, builder, "", String.class);
    }
}
