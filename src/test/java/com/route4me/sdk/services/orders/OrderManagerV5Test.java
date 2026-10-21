package com.route4me.sdk.services.orders;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link OrderManagerV5}: the V5 orders platform, archive, history and
 * order custom fields.
 */
public class OrderManagerV5Test {

    private static final String HOST = "wh.route4me.com";
    private static final String PLATFORM_EP = "/modules/api/v5.0/orders-platform";
    private static final String FIELDS_EP = PLATFORM_EP + "/order-custom-user-fields";
    private static final String ORDER = json("{'order_uuid':'ORDER-UUID-1','order_id':'7001','member_id':1001,"
            + "'address_1':'100 Example St','address_geo':{'lat':40.1,'lng':-74.1},'first_name':'Alex',"
            + "'weight':12.5,'day_scheduled_for_YYYYMMDD':'2026-10-22','custom_data':{'gate':'A1'}}");
    private static final String FIELD = json("{'data':{'order_custom_field_uuid':'FIELD-1',"
            + "'order_custom_field_name':'gate','order_custom_field_label':'Gate','order_custom_field_type':'text'}}");
    private static final String ASYNC = json("{'status':true,'async':true}");

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private OrderManagerV5 manager() {
        return http.install(new OrderManagerV5(MockHttp.API_KEY));
    }

    /** Builds a sample order with fake data. */
    private static OrderV5 order() {
        GeoPoint geo = new GeoPoint();
        geo.setLat(40.1);
        geo.setLng(-74.1);
        OrderV5 order = new OrderV5();
        order.setAddress1("100 Example St");
        order.setAddressGeo(geo);
        order.setFirstName("Alex");
        order.setDayScheduledFor("2026-10-22");
        order.setCustomData(Collections.<String, Object>singletonMap("gate", "A1"));
        return order;
    }

    /** Builds a sample set of order filters with fake data. */
    private static OrderFilters filters() {
        OrderFilters filters = new OrderFilters();
        filters.setSearchQuery("Example");
        filters.setTrackingNumber(Collections.singletonList("TRK-1"));
        filters.setDayScheduledFor(Collections.singletonList("2026-10-22"));
        return filters;
    }

    @Test
    public void searchOrdersPostsTheFilters() throws Exception {
        OrderManagerV5 manager = manager();
        http.respond(200, "{\"items\":[" + ORDER + "],\"total\":1}");
        SearchOrdersRequest request = new SearchOrdersRequest();
        request.setFilters(filters());
        request.setPage(1);
        request.setPerPage(20);
        request.setOrderBy(Collections.singletonList(new String[] {"created_timestamp", "desc"}));

        SearchOrdersResponse response = manager.searchOrders(request);

        http.request().assertEndpoint("POST", HOST, PLATFORM_EP).assertApiKeyInQuery()
                .assertJson("filters.search_query", "Example")
                .assertJson("filters.tracking_number[0]", "TRK-1")
                .assertJson("filters.day_scheduled_for_YYYYMMDD[0]", "2026-10-22")
                .assertJson("page", 1)
                .assertJson("order_by[0][1]", "desc");
        assertEquals(Integer.valueOf(1), response.getTotal());
        assertEquals("ORDER-UUID-1", response.getItems().get(0).getOrderUuid());
        assertEquals(40.1, response.getItems().get(0).getAddressGeo().getLat(), 0.0);
    }

    @Test
    public void batchOperationsPostTheirPayloads() throws Exception {
        OrderManagerV5 manager = manager();
        http.respond(200, ASYNC).respond(200, "[" + ORDER + "]").respond(200, json("{'status':true}"))
                .respond(200, ASYNC);
        BatchCreateOrdersRequest create = new BatchCreateOrdersRequest();
        create.setData(Collections.singletonList(order()));
        BatchUpdateOrdersRequest update = new BatchUpdateOrdersRequest();
        update.setData(Collections.singletonList(order()));
        BatchDeleteOrdersRequest delete = new BatchDeleteOrdersRequest();
        delete.setOrderUuids(Arrays.asList("ORDER-UUID-1", "ORDER-UUID-2"));
        BatchUpdateFilterOrderRequest byFilter = new BatchUpdateFilterOrderRequest();
        byFilter.setFilters(filters());
        byFilter.setData(Collections.<String, Object>singletonMap("first_name", "Sam"));

        AsyncStatusResponse created = manager.batchCreateOrders(create);
        OrderV5[] updated = manager.batchUpdateOrders(update);
        StatusResponse deleted = manager.batchDeleteOrders(delete);
        manager.batchUpdateFilter(byFilter);

        http.request(0).assertEndpoint("POST", HOST, PLATFORM_EP + "/batch-create")
                .assertJson("data[0].address_1", "100 Example St");
        http.request(1).assertEndpoint("POST", HOST, PLATFORM_EP + "/batch-update")
                .assertJson("data[0].address_geo.lat", 40.1);
        http.request(2).assertEndpoint("POST", HOST, PLATFORM_EP + "/batch-delete")
                .assertJson("order_uuids[1]", "ORDER-UUID-2");
        http.request(3).assertEndpoint("PUT", HOST, PLATFORM_EP + "/batch-update/filter")
                .assertJson("filters.search_query", "Example")
                .assertJson("data.first_name", "Sam");
        assertTrue(created.getAsync());
        assertEquals("Alex", updated[0].getFirstName());
        assertTrue(deleted.getStatus());
    }

    @Test
    public void archiveOrdersAndHistory() throws Exception {
        OrderManagerV5 manager = manager();
        http.respond(200, "{\"status\":true,\"data\":[" + ORDER + "]}")
                .respond(200, json("{'data':[{'event':'created'}]}"))
                .respond(200, json("{'data':[]}"));
        ArchiveOrdersRequest archive = new ArchiveOrdersRequest();
        archive.setOrderIds(Collections.singletonList("7001"));

        ArchiveOrdersResponse archived = manager.archiveOrders(archive);
        OrderHistoryResponse history = manager.getOrderHistory("ORDER-UUID-1", "TRK-1");
        manager.getOrderHistory(null, null);

        http.request(0).assertEndpoint("POST", HOST, "/modules/api/v5.0/orders/archive")
                .assertJson("order_ids[0]", "7001");
        http.request(1).assertEndpoint("GET", HOST, "/modules/api/v5.0/orders/history")
                .assertQuery("order_uuid", "ORDER-UUID-1").assertQuery("tracking_number", "TRK-1");
        http.request(2).assertNoQuery("order_uuid").assertNoQuery("tracking_number");
        assertEquals("7001", archived.getData().get(0).getOrderId());
        assertEquals(1, history.getData().size());
    }

    @Test
    public void customUserFieldsListAndCreate() throws Exception {
        OrderManagerV5 manager = manager();
        http.respond(200, json("{'data':[{'order_custom_field_uuid':'FIELD-1','order_custom_field_name':'gate'}]}"))
                .respond(200, FIELD);
        CreateCustomUserFieldRequest create = new CreateCustomUserFieldRequest();
        create.setOrderCustomFieldName("gate");
        create.setOrderCustomFieldLabel("Gate");
        create.setOrderCustomFieldType("text");
        create.setOrderCustomFieldTypeInfo(Collections.singletonMap("short_label", "G"));

        CustomUserFieldsResponse fields = manager.getCustomUserFields();
        CustomUserFieldResponse created = manager.createCustomUserField(create);

        http.request(0).assertEndpoint("GET", HOST, FIELDS_EP);
        http.request(1).assertEndpoint("POST", HOST, FIELDS_EP)
                .assertJson("order_custom_field_name", "gate")
                .assertJson("order_custom_field_type", "text")
                .assertJson("order_custom_field_type_info.short_label", "G");
        assertEquals("gate", fields.getData().get(0).getOrderCustomFieldName());
        assertEquals("FIELD-1", created.getData().getOrderCustomFieldUuid());
    }
    @Test
    public void createOrderPostsTheOrder() throws Exception {
        OrderManagerV5 manager = manager();
        http.respond(200, ORDER);

        OrderV5 created = manager.createOrder(order());

        http.request().assertEndpoint("POST", HOST, PLATFORM_EP + "/create")
                .assertJson("address_1", "100 Example St")
                .assertJson("address_geo.lng", -74.1)
                .assertJson("day_scheduled_for_YYYYMMDD", "2026-10-22")
                .assertJson("custom_data.gate", "A1");
        assertEquals("ORDER-UUID-1", created.getOrderUuid());
    }
    @Test
    public void getUpdateAndDeleteOrderUseTheUuid() throws Exception {
        OrderManagerV5 manager = manager();
        http.respond(200, ORDER).respond(200, ORDER).respond(200, json("{'status':true}"));

        OrderV5 fetched = manager.getOrder("ORDER-UUID-1");
        manager.updateOrder("ORDER-UUID-1", order());
        StatusResponse deleted = manager.deleteOrder("ORDER-UUID-1");

        http.request(0).assertEndpoint("GET", HOST, PLATFORM_EP + "/ORDER-UUID-1");
        http.request(1).assertEndpoint("PUT", HOST, PLATFORM_EP + "/ORDER-UUID-1").assertJson("first_name", "Alex");
        http.request(2).assertEndpoint("DELETE", HOST, PLATFORM_EP + "/ORDER-UUID-1").assertNoBody();
        assertEquals(12.5, fetched.getWeight(), 0.0);
        assertTrue(deleted.getStatus());
    }
    @Test
    public void updateAndDeleteCustomUserFieldUseTheUuid() throws Exception {
        OrderManagerV5 manager = manager();
        http.respondToAll(200, FIELD);
        UpdateCustomUserFieldRequest update = new UpdateCustomUserFieldRequest();
        update.setOrderCustomFieldLabel("Gate code");
        update.setOrderCustomFieldType("text");

        manager.updateCustomUserField("FIELD-1", update);
        manager.deleteCustomUserField("FIELD-1");

        http.request(0).assertEndpoint("PUT", HOST, FIELDS_EP + "/FIELD-1")
                .assertJson("order_custom_field_label", "Gate code");
        http.request(1).assertEndpoint("DELETE", HOST, FIELDS_EP + "/FIELD-1").assertNoBody();
    }

    @Test
    public void disabledRedirectsAddRedirectZero() throws Exception {
        OrderManagerV5 manager = http.install(new OrderManagerV5(MockHttp.API_KEY, true));
        http.respond(200, json("{'data':[]}"));

        manager.getCustomUserFields();

        http.request().assertQuery("redirect", "0");
    }

    @Test
    public void errorBecomesApiException() {
        final OrderManagerV5 manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.searchOrders(new SearchOrdersRequest()));
    }
}
