package com.route4me.sdk.services.orders;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.route4me.sdk.responses.StatusResponse;
import com.route4me.sdk.testsupport.Fixtures;
import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link OrdersManager}: the V4 {@code order.php} endpoint.
 */
public class OrdersManagerTest {

    private static final String HOST = "api.route4me.com";
    private static final String ORDERS_EP = "/api.v4/order.php";

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private OrdersManager manager() {
        return http.install(new OrdersManager(MockHttp.API_KEY));
    }

    /** Returns an {@code order.php} search response with one order. */
    private static String ordersPage() {
        return "{\"results\":[" + Fixtures.read("order.json") + "],\"total\":1}";
    }

    /** Builds a sample order with fake data. */
    private static Order order() {
        Order order = new Order();
        order.setAddress1("100 Example St, Springfield");
        order.setAddressAlias("Example Bakery");
        order.setCachedLatitude(40.1);
        order.setCachedLongitude(-74.1);
        order.setFirstName("Alex");
        order.setDateScheduled("2026-10-22");
        return order;
    }

    @Test
    public void addOrderPostsTheOrderAsJson() throws Exception {
        OrdersManager manager = manager();
        http.respondWithFixture(200, "order.json");

        Order created = manager.addOrder(order());

        http.request().assertEndpoint("POST", HOST, ORDERS_EP).assertApiKeyInQuery()
                .assertContentType("application/json")
                .assertJson("address_1", "100 Example St, Springfield")
                .assertJson("cached_lat", 40.1)
                .assertJson("EXT_FIELD_first_name", "Alex")
                .assertJson("day_scheduled_for_YYMMDD", "2026-10-22");
        assertEquals(Long.valueOf(7001), created.getId());
        assertEquals("Sample", created.getLastName());
        assertEquals(12.5, created.getWeight(), 0.0);
    }

    @Test
    public void getOrderSendsTheId() throws Exception {
        OrdersManager manager = manager();
        http.respondWithFixture(200, "order.json");

        Order order = manager.getOrder(7001);

        http.request().assertEndpoint("GET", HOST, ORDERS_EP).assertQuery("order_id", "7001").assertNoBody();
        assertEquals("2026-10-21", order.getDateAdded());
    }

    @Test
    public void getOrdersSendsTheRequestAsQuery() throws Exception {
        OrdersManager manager = manager();
        http.respond(200, ordersPage());
        OrderRequest request = new OrderRequest().setLimit(10).setOffset(5).setDisplay(OrderRequest.DisplayType.ROUTED)
                .setDateInserted("2026-10-21").setDateScheduled("2026-10-22").setQuery("Bakery")
                .setFields("order_id").setIds("7001,7002");

        List<Order> orders = manager.getOrders(request);

        http.request().assertEndpoint("GET", HOST, ORDERS_EP)
                .assertQuery("limit", "10")
                .assertQuery("offset", "5")
                .assertQuery("display", "routed")
                .assertQuery("day_added_YYMMDD", "2026-10-21")
                .assertQuery("scheduled_for_YYMMDD", "2026-10-22")
                .assertQuery("query", "Bakery")
                .assertQuery("fields", "order_id")
                .assertQuery("order_ids", "7001,7002")
                .assertNoBody();
        assertEquals(1, orders.size());
        assertEquals("Example Bakery", orders.get(0).getAddressAlias());
    }

    @Test
    public void getOrdersDoesNotSendJavaToStringAsTheFilter() throws Exception {
        OrdersManager manager = manager();
        http.respond(200, ordersPage());
        OrderFilter filter = new OrderFilter();
        filter.setDisplay("all");
        OrderRequest request = new OrderRequest().setFilter(filter);

        manager.getOrders(request);

        String sent = http.request().query("filter");
        assertFalse("filter=" + sent, sent != null && sent.startsWith("OrderFilter("));
    }

    @Test
    public void getOrdersWithAFilterPostsItAsJson() throws Exception {
        OrdersManager manager = manager();
        http.respond(200, ordersPage());
        OrderFilter filter = new OrderFilter();
        filter.setTrackingNumbers(Collections.singletonList("TRK-1"));
        OrderRequest request = new OrderRequest().setFilter(filter).setLimit(5)
                .setDisplay(OrderRequest.DisplayType.UNROUTED);

        List<Order> orders = manager.getOrders(request);

        http.request().assertEndpoint("POST", HOST, ORDERS_EP)
                .assertNoQuery("filter").assertNoQuery("limit")
                .assertContentType("application/json")
                .assertJson("filter.tracking_numbers[0]", "TRK-1")
                .assertJson("limit", 5)
                .assertJson("display", "unrouted");
        assertEquals(1, orders.size());
    }

    @Test
    public void ordersOfAnEmptyResponseAreNull() throws Exception {
        OrdersManager manager = manager();
        http.respondWithoutBody(200).respondWithoutBody(200);

        assertNull(manager.getOrders(new OrderRequest()));
        assertNull(manager.getOrdersByStatus(Collections.singletonList(OrderStatus.NEW.getValue())));
    }

    @Test
    public void getOrdersByScheduledDatePostsAFilter() throws Exception {
        OrdersManager manager = manager();
        http.respondToAll(200, ordersPage());

        manager.getOrdersByScheduledDate(Collections.singletonList("2026-10-22"));
        manager.getOrdersByScheduledDate(Arrays.asList("2026-10-22", "2026-10-23"), 5, 10);

        http.request(0).assertEndpoint("POST", HOST, ORDERS_EP)
                .assertJson("filter.scheduled_for_YYMMDD[0]", "2026-10-22")
                .assertJson("limit", 30)
                .assertJson("offset", 0);
        http.request(1).assertJson("filter.scheduled_for_YYMMDD[1]", "2026-10-23")
                .assertJson("limit", 5)
                .assertJson("offset", 10);
    }

    @Test
    public void getOrdersByCustomFieldPostsATermsFilter() throws Exception {
        OrdersManager manager = manager();
        http.respond(200, ordersPage());

        List<Order> orders = manager.getOrdersByCustomField("tracking", "TRK-1");

        JsonObject terms = http.request().assertEndpoint("POST", HOST, ORDERS_EP)
                .assertJson("filter.display", "all")
                .json().getAsJsonObject("filter").getAsJsonObject("terms");
        assertEquals("TRK-1", terms.get("custom_data.tracking").getAsString());
        assertEquals(1, orders.size());
    }

    @Test
    public void getOrdersByStatusAndTrackingNumberPostFilters() throws Exception {
        OrdersManager manager = manager();
        http.respondToAll(200, ordersPage());

        manager.getOrdersByStatus(Arrays.asList(OrderStatus.NEW.getValue(), OrderStatus.ROUTED.getValue()));
        manager.getOrdersByTrackingNumber(Collections.singletonList("TRK-1"));
        manager.getOrdersByTrackingNumber(Collections.singletonList("TRK-2"), 1, 2);

        http.request(0).assertJson("filter.statuses[0]", 0).assertJson("filter.statuses[1]", 7);
        http.request(1).assertJson("filter.tracking_numbers[0]", "TRK-1").assertJson("limit", 30);
        http.request(2).assertJson("filter.tracking_numbers[0]", "TRK-2").assertJson("limit", 1).assertJson("offset",
                2);
    }

    @Test
    public void updateOrderPutsTheOrder() throws Exception {
        OrdersManager manager = manager();
        http.respondWithFixture(200, "order.json");
        Order change = order();
        change.setId(7001L);

        manager.updateOrder(change);

        http.request().assertEndpoint("PUT", HOST, ORDERS_EP).assertJson("order_id", 7001);
    }

    @Test
    public void deleteOrdersSendsTheIdsInTheBody() throws Exception {
        OrdersManager manager = manager();
        http.respond(200, json("{'status':true}"));

        StatusResponse status = manager.deleteOrders(7001, 7002);

        http.request().assertEndpoint("DELETE", HOST, ORDERS_EP)
                .assertJson("order_ids[0]", 7001)
                .assertJson("order_ids[1]", 7002);
        assertTrue(status.getStatus());
    }

    @Test
    public void errorBecomesApiException() {
        final OrdersManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getOrder(7001));
    }

    @Test
    public void orderStatusLooksUpByCode() {
        assertEquals(OrderStatus.DONE, OrderStatus.get(13));
        assertEquals(15, OrderStatus.SCHEDULED.getValue());
        assertNull(OrderStatus.get(99));
    }

    @Test
    public void orderRequestSerializesWithApiNames() {
        OrderRequest request = new OrderRequest().setDateInserted("2026-10-21").setDateScheduled("2026-10-22")
                .setIds("7001");

        JsonObject json = new Gson().toJsonTree(request).getAsJsonObject();

        assertEquals(json.toString(), "2026-10-21", stringField(json, "day_added_YYMMDD"));
        assertEquals(json.toString(), "2026-10-22", stringField(json, "scheduled_for_YYMMDD"));
        assertEquals(json.toString(), "7001", stringField(json, "order_ids"));
        assertFalse(json.toString(), json.has("dateInserted"));
        assertEquals("routed", new Gson().toJsonTree(new OrderRequest().setDisplay(OrderRequest.DisplayType.ROUTED))
                .getAsJsonObject().get("display").getAsString());
    }

    /** Returns a string field of {@code json}, or {@code null} when it is absent. */
    private static String stringField(JsonObject json, String name) {
        return json.has(name) ? json.get(name).getAsString() : null;
    }
}
