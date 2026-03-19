package com.route4me.sdk.integration;

import com.route4me.sdk.services.orders.Order;
import com.route4me.sdk.services.orders.OrderRequest;
import com.route4me.sdk.services.orders.OrdersManager;
import com.route4me.sdk.exception.APIException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class OrdersManagerIntegrationTest extends BaseIntegrationTest {
    private OrdersManager manager;
    private List<Long> createdIds = new ArrayList<>();

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new OrdersManager(API_KEY);
    }

    @After
    public void tearDown() {
        if (!createdIds.isEmpty()) {
            try {
                long[] ids = createdIds.stream().mapToLong(Long::longValue).toArray();
                manager.deleteOrders(ids);
            } catch (Exception e) { }
        }
    }

    private Order buildOrder() {
        Order order = new Order();
        order.setAddress1("1600 Amphitheatre Pkwy, Mountain View, CA 94043");
        order.setAddressAlias("SDK V4 Test " + System.currentTimeMillis());
        return order;
    }

    @Test
    public void testAddAndGetOrder() throws APIException {
        Order created = manager.addOrder(buildOrder());
        assertNotNull(created);
        assertTrue(created.getId() > 0);
        createdIds.add(created.getId());

        Order fetched = manager.getOrder(created.getId());
        assertNotNull(fetched);
        assertEquals(created.getId(), fetched.getId());
    }

    @Test
    public void testGetOrders() throws APIException {
        Order created = manager.addOrder(buildOrder());
        createdIds.add(created.getId());

        OrderRequest request = new OrderRequest();
        request.setLimit(10);
        request.setOffset(0);
        List<Order> orders = manager.getOrders(request);
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
    }

    @Test
    public void testUpdateOrder() throws APIException {
        Order created = manager.addOrder(buildOrder());
        createdIds.add(created.getId());

        created.setAddressAlias("SDK Updated V4 Order");
        Order updated = manager.updateOrder(created);
        assertNotNull(updated);
    }

    @Test
    public void testDeleteOrders() throws APIException {
        Order created = manager.addOrder(buildOrder());
        assertNotNull(created);
        manager.deleteOrders(created.getId());
    }
}
