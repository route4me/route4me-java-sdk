package com.route4me.sdk.integration;

import com.route4me.sdk.services.orders.*;
import com.route4me.sdk.exception.APIException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class OrderManagerV5IntegrationTest extends BaseIntegrationTest {
    private OrderManagerV5 manager;
    private List<String> createdUuids = new ArrayList<>();

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new OrderManagerV5(API_KEY);
    }

    @After
    public void tearDown() {
        for (String uuid : createdUuids) {
            try { manager.deleteOrder(uuid); } catch (Exception e) { }
        }
    }

    private OrderV5 buildOrder() {
        OrderV5 order = new OrderV5();
        order.setAddress1("1600 Amphitheatre Pkwy, Mountain View, CA 94043");
        order.setAddressAlias("SDK Test Order " + System.currentTimeMillis());
        order.setFirstName("Test");
        order.setLastName("User");
        return order;
    }

    @Test
    public void testCreateAndGetOrder() throws APIException {
        OrderV5 created = manager.createOrder(buildOrder());
        assertNotNull(created);
        assertNotNull(created.getOrderUuid());
        createdUuids.add(created.getOrderUuid());

        OrderV5 fetched = manager.getOrder(created.getOrderUuid());
        assertNotNull(fetched);
        assertEquals(created.getOrderUuid(), fetched.getOrderUuid());
    }

    @Test
    public void testUpdateOrder() throws APIException {
        OrderV5 created = manager.createOrder(buildOrder());
        createdUuids.add(created.getOrderUuid());

        OrderV5 update = new OrderV5();
        update.setAddressAlias("SDK Updated Order");
        OrderV5 updated = manager.updateOrder(created.getOrderUuid(), update);
        assertNotNull(updated);
    }

    @Test
    public void testDeleteOrder() throws APIException {
        OrderV5 created = manager.createOrder(buildOrder());
        assertNotNull(created.getOrderUuid());
        StatusResponse response = manager.deleteOrder(created.getOrderUuid());
        assertNotNull(response);
    }

    @Test
    public void testSearchOrders() throws APIException {
        OrderV5 created = manager.createOrder(buildOrder());
        createdUuids.add(created.getOrderUuid());

        SearchOrdersRequest request = new SearchOrdersRequest();
        request.setPage(1);
        request.setPerPage(10);
        SearchOrdersResponse response = manager.searchOrders(request);
        assertNotNull(response);
    }

    @Test
    public void testGetCustomUserFields() throws APIException {
        CustomUserFieldsResponse response = manager.getCustomUserFields();
        assertNotNull(response);
    }
}
