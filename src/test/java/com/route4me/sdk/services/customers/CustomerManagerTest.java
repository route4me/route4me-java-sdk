package com.route4me.sdk.services.customers;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CustomerManager}: customers on the V5 API.
 */
public class CustomerManagerTest {

    private static final String HOST = "wh.route4me.com";
    private static final String CUSTOMERS_EP = "/modules/api/v5.0/customers";
    private static final String CUSTOMER = json("{'customer_id':'CUST-1','name':'Example Bakery',"
            + "'external_id':'EXT-1','status':1,'currency':'USD','contacts':[{'type':'billing','first_name':'Alex',"
            + "'email':'alex@example.com'}],'addresses':[{'type':'billing','address':'100 Example St'}],"
            + "'facility_ids':['FAC-1'],'accountable_person_first_name':'Sam','customer_location_count':2}");

    @Rule
    public final MockHttp http = new MockHttp();

    private CustomerManager manager() {
        return http.install(new CustomerManager(MockHttp.API_KEY));
    }

    private static CustomerStoreRequest storeRequest() {
        CustomerContact contact = new CustomerContact();
        contact.setType("billing");
        contact.setFirstName("Alex");
        contact.setEmail("alex@example.com");
        CustomerAddress address = new CustomerAddress();
        address.setType("billing");
        address.setAddress("100 Example St");
        CustomerContract contract = new CustomerContract();
        contract.setStartDate("2026-01-01");
        contract.setExpiration("2026-12-31");
        CustomerStoreRequest request = new CustomerStoreRequest();
        request.setName("Example Bakery");
        request.setExternalId("EXT-1");
        request.setCurrency("USD");
        request.setStatus(1);
        request.setContacts(Collections.singletonList(contact));
        request.setAddresses(Collections.singletonList(address));
        request.setContracts(Collections.singletonList(contract));
        request.setFacilityIds(Collections.singletonList("FAC-1"));
        return request;
    }

    @Test
    public void getCustomersListPostsTheFilters() throws Exception {
        CustomerManager manager = manager();
        http.respond(200, json("{'items':[{'customer_id':'CUST-1','name':'Example Bakery','contacts':"
                + "[{'type':'billing','name':'Alex Sample'}]}]}"));
        CustomerListFilters filters = new CustomerListFilters();
        filters.setSearchQuery("Bakery");
        filters.setStatus(Arrays.asList(1, 2));
        CustomerListParameters parameters = new CustomerListParameters();
        parameters.setFilters(filters);
        parameters.setPage(1);
        parameters.setPerPage(25);
        parameters.setOrderBy(Arrays.asList("name", "asc"));

        CustomerListResponse response = manager.getCustomersList(parameters);

        http.request().assertEndpoint("POST", HOST, CUSTOMERS_EP + "/list").assertApiKeyInQuery()
                .assertJson("filters.search_query", "Bakery")
                .assertJson("filters.status[1]", 2)
                .assertJson("page", 1)
                .assertJson("per_page", 25)
                .assertJson("order_by[0]", "name");
        assertEquals("Alex Sample", response.getItems().get(0).getContacts().get(0).getName());
    }

    @Test
    public void getCustomerByIdReadsTheDetails() throws Exception {
        CustomerManager manager = manager();
        http.respond(200, CUSTOMER);

        CustomerShowResource customer = manager.getCustomerById("CUST-1");

        http.request().assertEndpoint("GET", HOST, CUSTOMERS_EP + "/CUST-1").assertNoBody();
        assertEquals("Example Bakery", customer.getName());
        assertEquals("Sam", customer.getAccountablePersonFirstName());
        assertEquals(Integer.valueOf(2), customer.getCustomerLocationCount());
        assertEquals("100 Example St", customer.getAddresses().get(0).getAddress());
    }

    @Test
    public void createAndUpdateCustomerSendTheCustomer() throws Exception {
        CustomerManager manager = manager();
        http.respondToAll(200, CUSTOMER);

        CustomerResource created = manager.createCustomer(storeRequest());
        manager.updateCustomer("CUST-1", storeRequest());

        http.request(0).assertEndpoint("POST", HOST, CUSTOMERS_EP)
                .assertJson("name", "Example Bakery")
                .assertJson("external_id", "EXT-1")
                .assertJson("contacts[0].first_name", "Alex")
                .assertJson("addresses[0].address", "100 Example St")
                .assertJson("contracts[0].start_date", "2026-01-01")
                .assertJson("facility_ids[0]", "FAC-1");
        http.request(1).assertEndpoint("PUT", HOST, CUSTOMERS_EP + "/CUST-1").assertJson("currency", "USD");
        assertEquals("CUST-1", created.getCustomerId());
    }

    @Test
    public void deleteCustomerSendsDelete() throws Exception {
        CustomerManager manager = manager();
        http.respond(200, json("{'status':true}"));

        manager.deleteCustomer("CUST-1");

        http.request().assertEndpoint("DELETE", HOST, CUSTOMERS_EP + "/CUST-1").assertNoBody();
    }

    @Test
    public void disabledRedirectsAddRedirectZero() throws Exception {
        CustomerManager manager = http.install(new CustomerManager(MockHttp.API_KEY, true));
        http.respond(200, CUSTOMER);

        manager.getCustomerById("CUST-1");

        http.request().assertQuery("redirect", "0");
    }

    @Test
    public void errorBecomesApiException() {
        final CustomerManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.deleteCustomer("CUST-1"));
    }
}
