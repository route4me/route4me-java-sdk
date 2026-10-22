package com.route4me.sdk.services.orders;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

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
    private static final String ORDER_ID = "C0FFEE0000000000000000000000000A";
    private static final String ORDER_ID_2 = "C0FFEE0000000000000000000000000B";

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
        filters.setOrderIds(Collections.singletonList(ORDER_ID));
        filters.setTrackingNumbers(Collections.singletonList("TRK-1"));
        filters.setOnlyGeocoded(true);
        filters.setLastStatuses(Arrays.asList(1, 2));
        filters.setDisplay("unrouted");
        Map<String, Object> range = new LinkedHashMap<String, Object>();
        range.put("start", "2026-10-01");
        range.put("end", "2026-10-22");
        filters.setScheduledFor(range);
        return filters;
    }

    /** Builds a sample text search with fake data. */
    private static OrderSearch search() {
        OrderSearch search = new OrderSearch();
        search.setMatches(Collections.<String, Object>singletonMap("first_name", "Alex"));
        search.setTerms(Collections.<String, Object>singletonMap("email", "alex@example.com"));
        return search;
    }

    @Test
    public void searchOrdersPostsTheSpecBody() throws Exception {
        OrderManagerV5 manager = manager();
        http.respond(200, "{\"results\":[" + ORDER + "],\"total\":1,\"fields\":[\"order_uuid\"]}");
        SearchOrdersRequest request = new SearchOrdersRequest();
        request.setOrderIds(Collections.singletonList(ORDER_ID));
        request.setReturnProvidedFieldsAsMap(false);
        request.setOrderBy(Collections.singletonList(new String[] {"day_added", "desc"}));
        request.setLimit(20);
        request.setOffset(40);
        request.setFields(Collections.singletonList("order_uuid"));
        request.setAddition(Collections.singletonList("territory_ids"));
        request.setSearch(search());
        request.setFilters(filters());

        SearchOrdersResponse response = manager.searchOrders(request);

        http.request().assertEndpoint("POST", HOST, PLATFORM_EP).assertApiKeyAsBearer()
                .assertJson("order_ids[0]", ORDER_ID)
                .assertJson("return_provided_fields_as_map", false)
                .assertJson("order_by[0][0]", "day_added")
                .assertJson("order_by[0][1]", "desc")
                .assertJson("limit", 20)
                .assertJson("offset", 40)
                .assertJson("fields[0]", "order_uuid")
                .assertJson("addition[0]", "territory_ids")
                .assertJson("search.matches.first_name", "Alex")
                .assertJson("search.terms.email", "alex@example.com")
                .assertJson("filters.order_ids[0]", ORDER_ID)
                .assertJson("filters.tracking_numbers[0]", "TRK-1")
                .assertJson("filters.only_geocoded", true)
                .assertJson("filters.last_statuses[1]", 2)
                .assertJson("filters.display", "unrouted")
                .assertJson("filters.scheduled_for.start", "2026-10-01")
                .assertJson("filters.scheduled_for.end", "2026-10-22")
                .assertJsonLacks("page", "per_page", "order_uuids", "filters.order_uuids", "search.query");
        assertEquals(Integer.valueOf(1), response.getTotal());
        assertEquals("order_uuid", response.getFields().get(0));
        assertEquals("ORDER-UUID-1", response.getResults().get(0).getOrderUuid());
        assertEquals(40.1, response.getResults().get(0).getAddressGeo().getLat(), 0.0);
    }

    @Test
    public void batchOperationsPostTheirPayloads() throws Exception {
        OrderManagerV5 manager = manager();
        http.respond(200, ASYNC).respond(200, "[" + ORDER + "]").respond(200, json("{'status':true}"))
                .respond(200, ASYNC);
        BatchCreateOrdersRequest create = new BatchCreateOrdersRequest();
        create.setData(Collections.singletonList(order()));
        BatchUpdateOrdersRequest update = new BatchUpdateOrdersRequest();
        update.setOrderIds(Arrays.asList(ORDER_ID, ORDER_ID_2));
        update.setData(order());
        BatchDeleteOrdersRequest delete = new BatchDeleteOrdersRequest();
        delete.setOrderUuids(Arrays.asList(ORDER_ID, ORDER_ID_2));
        BatchUpdateFilterOrderRequest byFilter = new BatchUpdateFilterOrderRequest();
        byFilter.setFilters(filters());
        byFilter.setSearch(search());
        byFilter.setData(Collections.<String, Object>singletonMap("first_name", "Sam"));

        AsyncStatusResponse created = manager.batchCreateOrders(create);
        OrderV5[] updated = manager.batchUpdateOrders(update);
        StatusResponse deleted = manager.batchDeleteOrders(delete);
        manager.batchUpdateFilter(byFilter);

        http.request(0).assertEndpoint("POST", HOST, PLATFORM_EP + "/batch-create")
                .assertJson("data[0].address_1", "100 Example St");
        http.request(1).assertEndpoint("POST", HOST, PLATFORM_EP + "/batch-update")
                .assertJson("order_ids[1]", ORDER_ID_2)
                .assertJson("data.address_geo.lat", 40.1)
                .assertJson("data.first_name", "Alex");
        http.request(2).assertEndpoint("POST", HOST, PLATFORM_EP + "/batch-delete")
                .assertJson("order_ids[0]", ORDER_ID)
                .assertJson("order_ids[1]", ORDER_ID_2)
                .assertJsonLacks("order_uuids");
        http.request(3).assertEndpoint("PUT", HOST, PLATFORM_EP + "/batch-update/filter")
                .assertJson("filters.tracking_numbers[0]", "TRK-1")
                .assertJson("search.matches.first_name", "Alex")
                .assertJson("data.first_name", "Sam");
        assertTrue(created.getAsync());
        assertEquals("Alex", updated[0].getFirstName());
        assertTrue(deleted.getStatus());
    }

    @Test
    public void archiveOrdersPostsTheCursorPageAndFilters() throws Exception {
        OrderManagerV5 manager = manager();
        http.respond(200, json("{'items':[{'order_id':7001,'address_1':'100 Example St',"
                + "'EXT_FIELD_first_name':'Alex','day_added_YYMMDD':'2026-10-01'}],'next_page_cursor':'CURSOR-2'}"));
        ArchiveOrdersFilters filters = new ArchiveOrdersFilters();
        filters.setDayAdded(Arrays.asList("2026-10-01", "2026-10-02"));
        ArchiveOrdersRequest archive = new ArchiveOrdersRequest();
        archive.setCursor("CURSOR-1");
        archive.setPerPage(50);
        archive.setFilters(filters);

        ArchiveOrdersResponse archived = manager.archiveOrders(archive);

        http.request().assertEndpoint("POST", HOST, "/modules/api/v5.0/orders/archive").assertApiKeyAsBearer()
                .assertJson("cursor", "CURSOR-1")
                .assertJson("per_page", 50)
                .assertJson("filters.day_added[1]", "2026-10-02")
                .assertJsonLacks("order_ids");
        assertEquals("CURSOR-2", archived.getNextPageCursor());
        assertEquals("Alex", archived.getItems().get(0).getFirstName());
        assertEquals("100 Example St", archived.getItems().get(0).getAddress1());
    }

    @Test
    public void getOrderHistorySendsOnlyTheGivenIdentifiers() throws Exception {
        OrderManagerV5 manager = manager();
        http.respondToAll(200, json("{'order_barcode':'BAR-1','results':[{'created_timestamp':1761040000,"
                + "'order_id':'7001','event_type':2,'member_id':1001,'order_diffs':[{'field':'member_id',"
                + "'new_value':1002}],'order_model':{'order_uuid':'ORDER-UUID-1','first_name':'Alex'}}]}"));

        OrderHistoryResponse history = manager.getOrderHistory(7001L, "ORDER-UUID-1", "TRK-1");
        manager.getOrderHistory("ORDER-UUID-1", null);
        manager.getOrderHistory(null, null);

        http.request(0).assertEndpoint("GET", HOST, "/modules/api/v5.0/orders/history").assertApiKeyAsBearer()
                .assertQuery("order_id", "7001")
                .assertQuery("order_uuid", "ORDER-UUID-1")
                .assertQuery("tracking_number", "TRK-1")
                .assertNoBody();
        http.request(1).assertQuery("order_uuid", "ORDER-UUID-1").assertNoQuery("order_id")
                .assertNoQuery("tracking_number");
        http.request(2).assertNoQuery("order_uuid").assertNoQuery("tracking_number").assertNoQuery("order_id");
        assertEquals("BAR-1", history.getOrderBarcode());
        OrderHistoryEntry entry = history.getResults().get(0);
        assertEquals(Long.valueOf(1761040000L), entry.getCreatedTimestamp());
        assertEquals(Integer.valueOf(2), entry.getEventType());
        assertEquals("member_id", entry.getOrderDiffs().get(0).getField());
        assertEquals(1002.0, ((Number) entry.getOrderDiffs().get(0).getNewValue()).doubleValue(), 0.0);
        assertEquals("Alex", entry.getOrderModel().getFirstName());
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
