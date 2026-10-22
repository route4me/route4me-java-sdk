package com.route4me.sdk.services.addressbook;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link AddressBookManagerV5}: address-book contacts, batch and area operations,
 * clustering, exports, job tracking and groups on the V5 API.
 */
public class AddressBookManagerV5Test {

    private static final String HOST = "wh.route4me.com";
    private static final String BOOK_EP = "/modules/api/v5.0/address-book";
    private static final String ADDRESSES_EP = BOOK_EP + "/addresses";
    private static final String CONTACT = json("{'address_id':12345,'member_id':1001,'address_1':'100 Example St',"
            + "'address_alias':'Example Bakery','first_name':'Alex','cached_lat':40.1,'cached_lng':-74.1,"
            + "'service_time':300,'address_stop_type':'DELIVERY','tags':['vip'],'in_route_count':2}");
    private static final String CONTACTS = "{\"results\":[" + CONTACT + "],\"total\":1}";
    private static final String STATUS_TRUE = json("{'status':true}");

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private AddressBookManagerV5 manager() {
        return http.install(new AddressBookManagerV5(MockHttp.API_KEY));
    }

    /** Builds a sample address book contact with fake data. */
    private static AddressBookContactV5 contact() {
        AddressBookContactV5 contact = new AddressBookContactV5();
        contact.setAddress1("100 Example St");
        contact.setAddressAlias("Example Bakery");
        contact.setFirstName("Alex");
        contact.setCachedLat(40.1);
        contact.setCachedLng(-74.1);
        return contact;
    }

    /** Builds sample filters with fake data. */
    private static AddressBookContactsFilter filter() {
        Map<String, Object> circle = new LinkedHashMap<>();
        circle.put("center", Arrays.asList(40.1, -74.1));
        circle.put("distance", 500);
        SelectedArea area = new SelectedArea();
        area.setType("circle");
        area.setValue(circle);
        AddressBookContactsFilter filter = new AddressBookContactsFilter();
        filter.setQuery("Bakery");
        filter.setSelectedAreas(Collections.singletonList(area));
        filter.setBoundingBox(box());
        filter.setDisplayList(Collections.singletonList("routed"));
        filter.setIsAssigned(true);
        return filter;
    }

    /** Builds a sample bounding box. */
    private static BoundingBox box() {
        BoundingBox box = new BoundingBox();
        box.setTop(41.0);
        box.setLeft(-75.0);
        box.setBottom(40.0);
        box.setRight(-74.0);
        return box;
    }

    @Test
    public void getContactsSendsTheQueryInBrackets() throws Exception {
        AddressBookManagerV5 manager = manager();
        http.respondToAll(200, CONTACTS);
        AddressBookContactsQuery query = new AddressBookContactsQuery();
        query.setFields(Arrays.asList("address_id", "address_1"));
        query.setDisplay("routed");
        query.setDisplayList(Collections.singletonList("unrouted"));
        query.setQuery("Bakery");
        query.setLimit(50);
        query.setOffset(100);

        AddressBookContactsResponse all = manager.getContacts(query);
        manager.getContacts((AddressBookContactsQuery) null);

        http.request(0).assertEndpoint("GET", HOST, ADDRESSES_EP + "/index/all").assertApiKeyAsBearer().assertNoBody()
                .assertQueryValues("fields[]", "address_id", "address_1")
                .assertQuery("display", "routed")
                .assertQueryValues("display_list[]", "unrouted")
                .assertQuery("query", "Bakery")
                .assertQuery("limit", "50")
                .assertQuery("offset", "100")
                .assertNoQuery("fields");
        assertTrue(http.request(1).getQueryParameters().isEmpty());
        assertEquals(Integer.valueOf(1), all.getTotal());
        assertEquals("DELIVERY", all.getResults().get(0).getAddressStopType());
        assertEquals(Long.valueOf(300), all.getResults().get(0).getServiceTime());
    }

    /** Regression: the body sent {@code filters}; the spec reads {@code query}, {@code filter} and a range. */
    @Test
    public void getContactsPostsTheSpecBody() throws Exception {
        AddressBookManagerV5 manager = manager();
        http.respond(200, CONTACTS);
        AddressBookContactsBodyRequest request = new AddressBookContactsBodyRequest();
        request.setQuery("Main");
        request.setFields(Arrays.asList("address_id", "address_1"));
        request.setFilter(filter());
        request.setOrderBy(Collections.singletonList(new String[] {"address_alias", "asc"}));
        request.setLimit(10);
        request.setOffset(20);

        AddressBookContactsResponse all = manager.getContacts(request);

        http.request().assertEndpoint("POST", HOST, ADDRESSES_EP + "/index/all").assertApiKeyAsBearer()
                .assertJson("query", "Main")
                .assertJson("fields[1]", "address_1")
                .assertJson("filter.query", "Bakery")
                .assertJson("filter.selected_areas[0].type", "circle")
                .assertJson("filter.selected_areas[0].value.distance", 500)
                .assertJson("filter.bounding_box.top", 41.0)
                .assertJson("filter.display_list[0]", "routed")
                .assertJson("filter.is_assigned", true)
                .assertJson("order_by[0][0]", "address_alias")
                .assertJson("order_by[0][1]", "asc")
                .assertJson("limit", 10)
                .assertJson("offset", 20)
                .assertJsonLacks("filters", "page");
        assertEquals("Example Bakery", all.getResults().get(0).getAddressAlias());
    }

    @Test
    public void getContactsPaginatedSendsThePage() throws Exception {
        AddressBookManagerV5 manager = manager();
        http.respondToAll(200, CONTACTS);
        AddressBookContactsPaginatedQuery query = new AddressBookContactsPaginatedQuery();
        query.setPage(2);
        query.setPerPage(30);
        query.setDisplayList(Arrays.asList("routed", "unrouted"));
        AddressBookContactsBodyPaginatedRequest body = new AddressBookContactsBodyPaginatedRequest();
        body.setFilter(filter());
        body.setPage(3);
        body.setPerPage(20);

        manager.getContactsPaginated(query);
        AddressBookContactsResponse page = manager.getContactsPaginated(body);

        http.request(0).assertEndpoint("GET", HOST, ADDRESSES_EP + "/index/pagination").assertNoBody()
                .assertQuery("page", "2")
                .assertQuery("per_page", "30")
                .assertQueryValues("display_list[]", "routed", "unrouted");
        http.request(1).assertEndpoint("POST", HOST, ADDRESSES_EP + "/index/pagination")
                .assertJson("page", 3)
                .assertJson("per_page", 20)
                .assertJson("filter.query", "Bakery")
                .assertJsonLacks("limit", "offset");
        assertEquals(1, page.getResults().size());
    }

    @Test
    public void clusteringByQueryAndByBody() throws Exception {
        AddressBookManagerV5 manager = manager();
        http.respond(200, json("{'clusters':[{'geohash':'dr5ru','lat':40.1,'lng':-74.1,'bbox':[[40.0,-74.2],"
                + "[40.2,-74.0]],'address_count':7}],'total':7}"))
                .respond(200, json("{'clusters':[{'cluster':{'geohash':'dr5','lat':40.5,'lng':-74.5},"
                        + "'address_count':3}],'total':3}"));
        AddressBookClusteringQuery query = new AddressBookClusteringQuery();
        query.setDisplay("unrouted");
        query.setQuery("Bakery");
        AddressBookClustering clustering = new AddressBookClustering();
        clustering.setPrecision(6);
        AddressBookClusteringRequest body = new AddressBookClusteringRequest();
        body.setQuery("Main");
        body.setClustering(clustering);
        body.setFilter(filter());

        AddressBookClusteringResponse flat = manager.getAddressBookContactsClustering(query);
        AddressBookClusteringResponse nested = manager.getAddressBookContactsClustering(body);

        http.request(0).assertEndpoint("GET", HOST, ADDRESSES_EP + "/index/clustering").assertNoBody()
                .assertQuery("display", "unrouted")
                .assertQuery("query", "Bakery");
        http.request(1).assertEndpoint("POST", HOST, ADDRESSES_EP + "/index/clustering")
                .assertJson("query", "Main")
                .assertJson("clustering.precision", 6)
                .assertJson("filter.bounding_box.right", -74.0);
        assertEquals("dr5ru", flat.getClusters().get(0).getGeohash());
        assertEquals(Integer.valueOf(7), flat.getClusters().get(0).getAddressCount());
        assertEquals(-74.0, flat.getClusters().get(0).getBbox().get(1).get(1), 0.0);
        assertEquals("dr5", nested.getClusters().get(0).getCluster().getGeohash());
        assertEquals(Integer.valueOf(3), nested.getTotal());
    }

    @Test
    public void getContactByIdAndByIds() throws Exception {
        AddressBookManagerV5 manager = manager();
        http.respond(200, CONTACT).respond(200, CONTACTS);

        AddressBookContactV5 contact = manager.getContactById(12345L);
        AddressBookContactsResponse contacts = manager.getContactsByIds(new long[] {12345L, 12346L});

        http.request(0).assertEndpoint("GET", HOST, ADDRESSES_EP + "/show").assertQuery("address_id", "12345")
                .assertNoBody();
        http.request(1).assertEndpoint("POST", HOST, ADDRESSES_EP + "/show")
                .assertJson("address_ids[0]", 12345)
                .assertJson("address_ids[1]", 12346);
        assertEquals(Long.valueOf(12345), contact.getAddressId());
        assertEquals(1, contacts.getResults().size());
    }

    @Test
    public void addAndUpdateContactSendTheContact() throws Exception {
        AddressBookManagerV5 manager = manager();
        http.respondToAll(200, CONTACT);
        AddressBookContactV5 change = contact();
        change.setServiceTime(600L);
        change.setTags(Collections.singletonList("vip"));

        AddressBookContactV5 created = manager.addContact(contact());
        manager.updateContact(12345L, change);

        http.request(0).assertEndpoint("POST", HOST, ADDRESSES_EP)
                .assertJson("address_1", "100 Example St")
                .assertJson("address_alias", "Example Bakery")
                .assertJson("first_name", "Alex")
                .assertJson("cached_lat", 40.1)
                .assertJsonLacks("address_id", "service_time");
        http.request(1).assertEndpoint("PUT", HOST, ADDRESSES_EP + "/12345")
                .assertJson("cached_lng", -74.1)
                .assertJson("service_time", 600)
                .assertJson("tags[0]", "vip");
        assertEquals(Long.valueOf(1001), created.getMemberId());
        assertEquals(Integer.valueOf(2), created.getInRouteCount());
    }

    @Test
    public void contactRequiredFieldsAreCheckedBeforeSending() {
        final AddressBookManagerV5 manager = manager();
        final AddressBookContactV5 noAddress = contact();
        noAddress.setAddress1(null);
        final AddressBookContactV5 noLat = contact();
        noLat.setCachedLat(null);
        final AddressBookContactV5 noLng = contact();
        noLng.setCachedLng(null);
        final BatchCreateContactsRequest batch = new BatchCreateContactsRequest();
        batch.setData(Arrays.asList(contact(), noLng));

        http.expectThrows(IllegalArgumentException.class, () -> manager.addContact(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.addContact(noAddress));
        http.expectThrows(IllegalArgumentException.class, () -> manager.updateContact(12345L, noLat));
        http.expectThrows(IllegalArgumentException.class, () -> manager.batchCreateContacts(batch));
        http.assertNoRequests();
    }

    /** Regression: batch-update wrapped the contacts in {@code data}; the spec reads {@code address_ids} and fields. */
    @Test
    public void batchUpdateContactsSendsIdsAndFieldsFlat() throws Exception {
        AddressBookManagerV5 manager = manager();
        http.respond(200, "[" + CONTACT + "]").respond(200, CONTACTS);
        BatchUpdateContactsRequest update = new BatchUpdateContactsRequest();
        update.setAddressIds(Arrays.asList(12345L, 12346L));
        update.setAddressGroup("North");
        update.setIsDeleted(false);

        AddressBookContactsResponse fromArray = manager.batchUpdateContacts(update);
        AddressBookContactsResponse fromObject = manager.batchUpdateContacts(update);

        http.request(0).assertEndpoint("PUT", HOST, ADDRESSES_EP + "/batch-update")
                .assertJson("address_ids[0]", 12345)
                .assertJson("address_ids[1]", 12346)
                .assertJson("address_group", "North")
                .assertJson("is_deleted", false)
                .assertJsonLacks("data", "address_1");
        assertEquals("Example Bakery", fromArray.getResults().get(0).getAddressAlias());
        assertNull(fromArray.getTotal());
        assertEquals(Integer.valueOf(1), fromObject.getTotal());
    }

    @Test
    public void batchCreateAndDelete() throws Exception {
        AddressBookManagerV5 manager = manager();
        http.respond(200, STATUS_TRUE).respond(200, STATUS_TRUE).respondWithoutBody(202);
        BatchCreateContactsRequest create = new BatchCreateContactsRequest();
        create.setData(Collections.singletonList(contact()));

        StatusResponse created = manager.batchCreateContacts(create);
        boolean deleted = manager.deleteContacts(new long[] {12345L});
        boolean queued = manager.deleteContacts(new long[] {12346L});

        http.request(0).assertEndpoint("POST", HOST, ADDRESSES_EP + "/batch-create")
                .assertJson("data[0].first_name", "Alex");
        http.request(1).assertEndpoint("DELETE", HOST, ADDRESSES_EP + "/delete").assertJson("address_ids[0]", 12345);
        http.request(2).assertJson("address_ids[0]", 12346);
        assertTrue(created.getStatus());
        assertTrue(deleted);
        assertTrue(queued);
    }

    @Test
    public void areaOperationsSendTheFilterAndAcceptAQueuedJob() throws Exception {
        AddressBookManagerV5 manager = manager();
        http.respondWithoutBody(202).respondWithoutBody(202);
        AddressBookAreasFilter areas = new AddressBookAreasFilter();
        areas.setQuery("Bakery");
        areas.setBoundingBox(box());
        AddressBookContactV5 data = new AddressBookContactV5();
        data.setAddressGroup("North");
        data.setColor("ff0000");
        UpdateContactsByAreasRequest update = new UpdateContactsByAreasRequest();
        update.setFilter(areas);
        update.setData(data);

        assertNull(manager.updateAddressBookContactByAreas(update));
        assertNull(manager.removeAddressBookContactsByAreas(areas));

        http.request(0).assertEndpoint("PUT", HOST, ADDRESSES_EP + "/update-by-areas")
                .assertJson("filter.query", "Bakery")
                .assertJson("filter.bounding_box.left", -75.0)
                .assertJson("data.address_group", "North")
                .assertJson("data.color", "ff0000");
        http.request(1).assertEndpoint("DELETE", HOST, ADDRESSES_EP + "/delete-by-areas")
                .assertJson("filter.query", "Bakery")
                .assertJson("filter.bounding_box.bottom", 40.0);
    }

    /** Regression: the answer was read as {@code data}; the spec names it {@code custom_fields}. */
    @Test
    public void customFieldsAndDepots() throws Exception {
        AddressBookManagerV5 manager = manager();
        http.respond(200, json("{'custom_fields':[{'label':'Gate code','value':'custom_data.gate_code',"
                + "'kind':'dynamic','type':'string','operators_exclude':['between']}]}"))
                .respond(200, "[" + CONTACT + "]");

        CustomFieldsResponse fields = manager.getCustomFields();
        AddressBookContactV5[] depots = manager.getDepots();

        http.request(0).assertEndpoint("GET", HOST, ADDRESSES_EP + "/custom-fields").assertNoBody();
        http.request(1).assertEndpoint("GET", HOST, ADDRESSES_EP + "/depots").assertNoBody();
        CustomField field = fields.getCustomFields().get(0);
        assertEquals("custom_data.gate_code", field.getValue());
        assertEquals("dynamic", field.getKind());
        assertEquals(Collections.singletonList("between"), field.getOperatorsExclude());
        assertEquals(1, depots.length);
    }

    /** Regression: the export body sent {@code filters}, {@code columns} and {@code format}; the spec reads these. */
    @Test
    public void exportsSendTheSpecBodiesAndAcceptAQueuedJob() throws Exception {
        AddressBookManagerV5 manager = manager();
        http.respondWithoutBody(202).respond(200, STATUS_TRUE).respondWithoutBody(202);
        AddressExportRequest byIds = new AddressExportRequest();
        byIds.setIds(Arrays.asList(12345L, 12346L));
        byIds.setFilename("contacts");
        byIds.setExtension("xlsx");
        byIds.setTimezone("America/New_York");
        AddressExportByAreasFilter byAreas = new AddressExportByAreasFilter();
        byAreas.setQuery("Bakery");
        byAreas.setBoundingBox(box());
        byAreas.setFilename("area-contacts");
        byAreas.setExtension("csv");
        AddressExportByAreaIdsRequest byAreaIds = new AddressExportByAreaIdsRequest();
        byAreaIds.setTerritoryIds(Collections.singletonList("C0FFEE0000000000000000000000000A"));
        byAreaIds.setFilename("territory-contacts");
        byAreaIds.setTimezone("UTC");

        assertNull(manager.exportAddresses(byIds));
        assertTrue(manager.exportAddressesByAreas(byAreas).getStatus());
        assertNull(manager.exportAddressesByAreaIds(byAreaIds));

        http.request(0).assertEndpoint("POST", HOST, ADDRESSES_EP + "/export")
                .assertJson("ids[1]", 12346)
                .assertJson("filename", "contacts")
                .assertJson("extension", "xlsx")
                .assertJson("tz", "America/New_York")
                .assertJsonLacks("filters", "columns", "format");
        http.request(1).assertEndpoint("POST", HOST, ADDRESSES_EP + "/export-by-areas")
                .assertJson("filter.query", "Bakery")
                .assertJson("filter.bounding_box.top", 41.0)
                .assertJson("filter.filename", "area-contacts")
                .assertJson("filter.extension", "csv")
                .assertJsonLacks("filename");
        http.request(2).assertEndpoint("POST", HOST, ADDRESSES_EP + "/export-by-area-ids")
                .assertJson("territory_ids[0]", "C0FFEE0000000000000000000000000A")
                .assertJson("filename", "territory-contacts")
                .assertJson("tz", "UTC");
    }

    @Test
    public void jobStatusAndResultPutTheJobIdInThePath() throws Exception {
        AddressBookManagerV5 manager = manager();
        http.respond(200, json("{'status':'processing'}")).respond(200, STATUS_TRUE);

        JobStatusResponse status = manager.getJobStatus("JOB-1");
        StatusResponse result = manager.getJobResult("JOB-1");

        http.request(0).assertEndpoint("GET", HOST, BOOK_EP + "/job-tracker/status/JOB-1").assertNoBody();
        http.request(1).assertEndpoint("GET", HOST, BOOK_EP + "/job-tracker/result/JOB-1").assertNoBody();
        assertEquals("processing", status.getStatus());
        assertTrue(result.getStatus());
    }

    @Test
    public void jobIdsAreRejectedWhenEmptyAndStayInOneSegment() throws Exception {
        final AddressBookManagerV5 manager = manager();
        http.expectThrows(IllegalArgumentException.class, () -> manager.getJobStatus(""));
        http.expectThrows(IllegalArgumentException.class, () -> manager.getJobResult(null));
        http.assertNoRequests();
        http.respondToAll(200, STATUS_TRUE);

        manager.getJobResult("JOB/1");

        assertEquals(BOOK_EP + "/job-tracker/result/JOB%2F1", http.request().getUri().getRawPath());
    }

    @Test
    public void groupsAndGroupRules() throws Exception {
        AddressBookManagerV5 manager = manager();
        http.respond(200, json("{'results':[{'group_id':'C0FFEE0000000000000000000000000B','group_name':'North',"
                + "'group_color':'92e1c0','member_id':1001,'filter':{'condition':'AND','rules':[{'id':'address_1',"
                + "'field':'address_1','operator':'contains','value':'Main'}]}}]}"))
                .respondToAll(200, CONTACTS);
        AddressBookGroupRule rule = new AddressBookGroupRule();
        rule.setId("address_1");
        rule.setField("address_1");
        rule.setOperator("contains");
        rule.setValue("Main");
        AddressBookGroupFilter rules = new AddressBookGroupFilter();
        rules.setCondition("AND");
        rules.setRules(Collections.singletonList(rule));
        AddressBookGroupRulesRequest request = new AddressBookGroupRulesRequest();
        request.setRules(rules);
        request.setBoundingBox(box());
        request.setFields(Arrays.asList("address_id", "address_1"));
        request.setLimit(100);
        request.setOffset(0);

        AddressBookGroupsResponse groups = manager.getAddressBookGroups();
        AddressBookContactsResponse matched = manager.filterContactsByGroupRules(request, "routed");
        manager.filterContactsByGroupRules(request, null);

        http.request(0).assertEndpoint("GET", HOST, BOOK_EP + "/groups").assertNoBody();
        http.request(1).assertEndpoint("POST", HOST, BOOK_EP + "/groups/filter/all")
                .assertQuery("display", "routed")
                .assertJson("rules.condition", "AND")
                .assertJson("rules.rules[0].field", "address_1")
                .assertJson("rules.rules[0].operator", "contains")
                .assertJson("rules.rules[0].value", "Main")
                .assertJson("bounding_box.top", 41.0)
                .assertJson("fields[0]", "address_id")
                .assertJson("limit", 100)
                .assertJson("offset", 0);
        http.request(2).assertNoQuery("display");
        AddressBookGroup group = groups.getResults().get(0);
        assertEquals("North", group.getGroupName());
        assertEquals(Long.valueOf(1001), group.getMemberId());
        assertEquals("Main", group.getFilter().getRules().get(0).getValue());
        assertEquals(1, matched.getResults().size());
    }

    @Test
    public void groupRulesAreRequired() {
        final AddressBookManagerV5 manager = manager();

        http.expectThrows(IllegalArgumentException.class, () -> manager.filterContactsByGroupRules(null, null));
        http.expectThrows(IllegalArgumentException.class,
            () -> manager.filterContactsByGroupRules(new AddressBookGroupRulesRequest(), "all"));
        http.assertNoRequests();
    }

    @Test
    public void disabledRedirectsAddRedirectZero() throws Exception {
        AddressBookManagerV5 manager = http.install(new AddressBookManagerV5(MockHttp.API_KEY, true));
        http.respond(200, CONTACT);

        manager.getContactById(12345L);

        http.request().assertQuery("redirect", "0");
    }

    @Test
    public void errorBecomesApiException() {
        final AddressBookManagerV5 manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getContacts(new AddressBookContactsBodyRequest()));
    }

    @Test
    public void unexpectedListAnswerBecomesApiException() {
        final AddressBookManagerV5 manager = manager();
        http.respond(200, "\"done\"");

        http.expectApiException(() -> manager.getContacts(new AddressBookContactsBodyRequest()));
    }
}
