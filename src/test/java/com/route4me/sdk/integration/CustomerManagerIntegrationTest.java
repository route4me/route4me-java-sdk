package com.route4me.sdk.integration;

import com.route4me.sdk.services.customers.*;
import com.route4me.sdk.exception.APIException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class CustomerManagerIntegrationTest extends BaseIntegrationTest {
    private CustomerManager manager;
    private List<String> createdIds = new ArrayList<>();

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new CustomerManager(API_KEY);
    }

    @After
    public void tearDown() {
        for (String id : createdIds) {
            try { manager.deleteCustomer(id); } catch (Exception e) { }
        }
    }

    private CustomerStoreRequest buildRequest(String name) {
        CustomerStoreRequest request = new CustomerStoreRequest();
        request.setName(name);
        request.setExternalId("java-sdk-test-" + System.currentTimeMillis());
        request.setCurrency("USD");
        request.setStatus(1);
        return request;
    }

    @Test
    public void testCreateAndGetCustomer() throws APIException {
        CustomerResource created = manager.createCustomer(buildRequest("SDK Test Customer"));
        assertNotNull(created);
        assertNotNull(created.getCustomerId());
        createdIds.add(created.getCustomerId());

        CustomerShowResource fetched = manager.getCustomerById(created.getCustomerId());
        assertNotNull(fetched);
        assertEquals(created.getCustomerId(), fetched.getCustomerId());
        assertEquals("SDK Test Customer", fetched.getName());
    }

    @Test
    public void testGetCustomersList() throws APIException {
        CustomerResource created = manager.createCustomer(buildRequest("SDK List Test"));
        createdIds.add(created.getCustomerId());

        CustomerListParameters params = new CustomerListParameters();
        params.setPage(1);
        params.setPerPage(10);
        CustomerListResponse response = manager.getCustomersList(params);
        assertNotNull(response);
    }

    @Test
    public void testUpdateCustomer() throws APIException {
        CustomerResource created = manager.createCustomer(buildRequest("SDK Update Test"));
        createdIds.add(created.getCustomerId());

        CustomerStoreRequest update = new CustomerStoreRequest();
        update.setName("SDK Updated Name");
        CustomerResource updated = manager.updateCustomer(created.getCustomerId(), update);
        assertNotNull(updated);
        assertEquals("SDK Updated Name", updated.getName());
    }

    @Test
    public void testDeleteCustomer() throws APIException {
        CustomerResource created = manager.createCustomer(buildRequest("SDK Delete Test"));
        assertNotNull(created.getCustomerId());
        manager.deleteCustomer(created.getCustomerId());
        // If no exception, delete succeeded
    }
}
