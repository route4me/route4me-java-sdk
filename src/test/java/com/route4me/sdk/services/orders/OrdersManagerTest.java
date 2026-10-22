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
    private static final String CUSTOM_FIELDS_EP = "/api.v4/order_custom_user_fields.php";
    private static final String CUSTOM_FIELD = json("{'order_custom_field_id':42,'order_custom_field_name':'gate',"
            + "'order_custom_field_label':'Gate code','order_custom_field_type':'dropdown','root_owner_member_id':1001,"
            + "'order_custom_field_type_info':{'short_label':'Gate','allowed_values':['A1','B2']}}");

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

    /** Builds a sample V4 order custom field with fake data. */
    private static OrderCustomUserField customField() {
        OrderCustomFieldTypeInfo info = new OrderCustomFieldTypeInfo();
        info.setShortLabel("Gate");
        info.setAllowedValues(Arrays.asList("A1", "B2"));
        OrderCustomUserField field = new OrderCustomUserField();
        field.setOrderCustomFieldName("gate");
        field.setOrderCustomFieldLabel("Gate code");
        field.setOrderCustomFieldType("dropdown");
        field.setOrderCustomFieldTypeInfo(info);
        return field;
    }

    @Test
    public void getOrderCustomUserFieldsReadsTheDataList() throws Exception {
        OrdersManager manager = manager();
        http.respond(200, "{\"data\":[" + CUSTOM_FIELD + "]}");

        List<OrderCustomUserField> fields = manager.getOrderCustomUserFields();

        http.request().assertEndpoint("GET", HOST, CUSTOM_FIELDS_EP).assertApiKeyInQuery().assertNoBody();
        assertEquals(Long.valueOf(42L), fields.get(0).getOrderCustomFieldId());
        assertEquals(Long.valueOf(1001L), fields.get(0).getRootOwnerMemberId());
        assertEquals("B2", fields.get(0).getOrderCustomFieldTypeInfo().getAllowedValues().get(1));
    }

    @Test
    public void getOrderCustomUserFieldsAlsoReadsABareArray() throws Exception {
        OrdersManager manager = manager();
        http.respond(200, "[" + CUSTOM_FIELD + "]").respond(200, json("{'result':'OK'}")).respondWithoutBody(200);

        List<OrderCustomUserField> fields = manager.getOrderCustomUserFields();

        assertEquals("gate", fields.get(0).getOrderCustomFieldName());
        assertNull(manager.getOrderCustomUserFields());
        assertNull(manager.getOrderCustomUserFields());
    }

    @Test
    public void getOrderCustomUserFieldsRejectsAListOfTheWrongShape() {
        final OrdersManager manager = manager();
        http.respond(200, "[1, 2]");

        http.expectApiException(manager::getOrderCustomUserFields);
    }

    @Test
    public void createOrderCustomUserFieldPostsTheField() throws Exception {
        OrdersManager manager = manager();
        http.respond(200, "{\"result\":\"OK\",\"affected\":1,\"data\":" + CUSTOM_FIELD + "}");

        OrderCustomUserFieldResponse response = manager.createOrderCustomUserField(customField());

        http.request().assertEndpoint("POST", HOST, CUSTOM_FIELDS_EP).assertApiKeyInQuery()
                .assertContentType("application/json")
                .assertJson("order_custom_field_name", "gate")
                .assertJson("order_custom_field_label", "Gate code")
                .assertJson("order_custom_field_type", "dropdown")
                .assertJson("order_custom_field_type_info.short_label", "Gate")
                .assertJson("order_custom_field_type_info.allowed_values[0]", "A1")
                .assertJsonLacks("order_custom_field_id", "root_owner_member_id", "order_custom_field_value");
        assertEquals("OK", response.getResult());
        assertEquals(Integer.valueOf(1), response.getAffected());
        assertEquals(Long.valueOf(42L), response.getData().getOrderCustomFieldId());
    }

    @Test
    public void updateOrderCustomUserFieldPutsTheField() throws Exception {
        OrdersManager manager = manager();
        http.respond(200, "{\"result\":\"OK\",\"affected\":1,\"data\":" + CUSTOM_FIELD + "}");
        OrderCustomUserField field = customField();
        field.setOrderCustomFieldId(42L);
        field.setOrderCustomFieldValue("A1");

        manager.updateOrderCustomUserField(field);

        http.request().assertEndpoint("PUT", HOST, CUSTOM_FIELDS_EP).assertApiKeyInQuery()
                .assertJson("order_custom_field_id", 42)
                .assertJson("order_custom_field_value", "A1")
                .assertJson("order_custom_field_label", "Gate code");
    }

    @Test
    public void removeOrderCustomUserFieldSendsOnlyTheId() throws Exception {
        OrdersManager manager = manager();
        http.respond(200, json("{'result':'OK','affected':1}"));

        OrderCustomUserFieldResponse response = manager.removeOrderCustomUserField(42L);

        http.request().assertEndpoint("DELETE", HOST, CUSTOM_FIELDS_EP).assertApiKeyInQuery()
                .assertJson("order_custom_field_id", 42);
        assertEquals(Collections.singleton("order_custom_field_id"), http.request().jsonKeys());
        assertEquals(Integer.valueOf(1), response.getAffected());
    }

    @Test
    public void getOrdersUpdatesSendsTheLastKnownTimestamp() throws Exception {
        OrdersManager manager = manager();
        http.respond(200, json("{'data':[{'order_id':7001,'updated_timestamp':'1761040000',"
                + "'EXT_FIELD_custom_data':{'gate':'A1'}}]}"));

        OrdersUpdatesResponse updates = manager.getOrdersUpdates(1761000000L);

        http.request().assertEndpoint("GET", HOST, "/api.v4/orders/changes.php").assertApiKeyInQuery()
                .assertQuery("last_known_ts", "1761000000")
                .assertNoBody();
        assertEquals(Long.valueOf(7001L), updates.getData().get(0).getOrderId());
        assertEquals("1761040000", updates.getData().get(0).getUpdatedTimestamp());
        assertEquals("A1", updates.getData().get(0).getCustomData().get("gate"));
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
