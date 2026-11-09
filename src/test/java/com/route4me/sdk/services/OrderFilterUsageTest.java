package com.route4me.sdk.services;

import com.route4me.sdk.services.orders.Order;
import com.route4me.sdk.services.orders.OrderFilter;
import com.route4me.sdk.services.orders.OrderRequest;
import com.route4me.sdk.services.orders.OrdersManager;
import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;

/**
 * Builds an {@link OrderFilter} from outside its package, as a caller of
 * {@link OrdersManager#getOrders(OrderRequest)} does.
 */
public class OrderFilterUsageTest {

    @Rule
    public final MockHttp http = new MockHttp();

    @Test
    public void callersCanBuildAFilterForGetOrders() throws Exception {
        OrdersManager manager = http.install(new OrdersManager(MockHttp.API_KEY));
        http.respond(200, json("{'results':[{'order_id':7001}],'total':1}"));
        OrderFilter filter = new OrderFilter()
                .setDisplay("unrouted")
                .setScheduledForYYMMDD(Arrays.asList("2026-10-21", "2026-10-22"))
                .setTerms(Collections.<String, Object>singletonMap("custom_data.priority", "high"))
                .setStatuses(Arrays.asList(0, 1))
                .setTrackingNumbers(Collections.singletonList("TRK-1"));

        List<Order> orders = manager.getOrders(new OrderRequest().setFilter(filter).setLimit(10));

        http.request().assertEndpoint("POST", "api.route4me.com", OrdersManager.ORDERS_EP)
                .assertJson("filter.display", "unrouted")
                .assertJson("filter.scheduled_for_YYMMDD[1]", "2026-10-22")
                .assertJson("filter.statuses[1]", 1)
                .assertJson("filter.tracking_numbers[0]", "TRK-1")
                .assertJson("limit", 10);
        assertEquals("high", http.request().jsonAt("filter.terms").getAsJsonObject()
                .get("custom_data.priority").getAsString());
        assertEquals(1, orders.size());
        assertEquals("2026-10-21", filter.getScheduledForYYMMDD().get(0));
    }
}
