package com.route4me.sdk.services.addressbook;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link AddressBookManagerV5}: address-book contacts, batch operations,
 * exports and job tracking on the V5 API.
 */
public class AddressBookManagerV5Test {

    private static final String HOST = "wh.route4me.com";
    private static final String BOOK_EP = "/modules/api/v5.0/address-book";
    private static final String ADDRESSES_EP = BOOK_EP + "/addresses";
    private static final String CONTACT = json("{'address_id':12345,'member_id':1001,'address_1':'100 Example St',"
            + "'address_alias':'Example Bakery','first_name':'Alex','cached_lat':40.1,'cached_lng':-74.1}");
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

    /** Builds a sample filtered contacts query with fake data. */
    private static AddressBookContactsBodyRequest query() {
        AddressBookFilters filters = new AddressBookFilters();
        filters.setSearchQuery("Bakery");
        filters.setAddressId(Collections.singletonList(12345L));
        AddressBookContactsBodyRequest request = new AddressBookContactsBodyRequest();
        request.setFilters(filters);
        request.setPage(1);
        request.setPerPage(20);
        request.setFields(Arrays.asList("address_id", "address_1"));
        return request;
    }

    @Test
    public void getContactsAndPaginatedPostTheFilters() throws Exception {
        AddressBookManagerV5 manager = manager();
        http.respondToAll(200, CONTACTS);

        AddressBookContactsResponse all = manager.getContacts(query());
        manager.getContactsPaginated(query());

        http.request(0).assertEndpoint("POST", HOST, ADDRESSES_EP + "/index/all").assertApiKeyAsBearer()
                .assertJson("filters.search_query", "Bakery")
                .assertJson("filters.address_id[0]", 12345)
                .assertJson("page", 1)
                .assertJson("fields[1]", "address_1");
        http.request(1).assertEndpoint("POST", HOST, ADDRESSES_EP + "/index/pagination").assertJson("per_page", 20);
        assertEquals(Integer.valueOf(1), all.getTotal());
        assertEquals("Example Bakery", all.getResults().get(0).getAddressAlias());
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

        AddressBookContactV5 created = manager.addContact(contact());
        manager.updateContact(12345L, contact());

        http.request(0).assertEndpoint("POST", HOST, ADDRESSES_EP)
                .assertJson("address_1", "100 Example St")
                .assertJson("address_alias", "Example Bakery")
                .assertJson("first_name", "Alex")
                .assertJson("cached_lat", 40.1);
        http.request(1).assertEndpoint("PUT", HOST, ADDRESSES_EP + "/12345").assertJson("cached_lng", -74.1);
        assertEquals(Long.valueOf(1001), created.getMemberId());
    }

    @Test
    public void batchCreateUpdateAndDelete() throws Exception {
        AddressBookManagerV5 manager = manager();
        http.respond(200, "[" + CONTACT + "]").respond(200, STATUS_TRUE).respond(200, STATUS_TRUE);
        BatchUpdateContactsRequest update = new BatchUpdateContactsRequest();
        update.setData(Collections.singletonList(contact()));
        BatchCreateContactsRequest create = new BatchCreateContactsRequest();
        create.setData(Collections.singletonList(contact()));

        AddressBookContactV5[] updated = manager.batchUpdateContacts(update);
        StatusResponse created = manager.batchCreateContacts(create);
        boolean deleted = manager.deleteContacts(new long[] {12345L});

        http.request(0).assertEndpoint("PUT", HOST, ADDRESSES_EP + "/batch-update")
                .assertJson("data[0].address_1", "100 Example St");
        http.request(1).assertEndpoint("POST", HOST, ADDRESSES_EP + "/batch-create")
                .assertJson("data[0].first_name", "Alex");
        http.request(2).assertEndpoint("DELETE", HOST, ADDRESSES_EP + "/delete").assertJson("address_ids[0]", 12345);
        assertEquals(1, updated.length);
        assertTrue(created.getStatus());
        assertTrue(deleted);
    }

    @Test
    public void customFieldsDepotsAndExport() throws Exception {
        AddressBookManagerV5 manager = manager();
        http.respond(200, json("{'data':['gate_code','floor']}")).respond(200, "[" + CONTACT + "]")
                .respond(200, STATUS_TRUE);
        AddressExportRequest export = new AddressExportRequest();
        export.setFilters(query().getFilters());
        export.setColumns(Arrays.asList("address_1", "address_alias"));
        export.setFilename("contacts.csv");
        export.setFormat("csv");

        CustomFieldsResponse fields = manager.getCustomFields();
        AddressBookContactV5[] depots = manager.getDepots();
        StatusResponse exported = manager.exportAddresses(export);

        http.request(0).assertEndpoint("GET", HOST, ADDRESSES_EP + "/custom-fields");
        http.request(1).assertEndpoint("GET", HOST, ADDRESSES_EP + "/depots");
        http.request(2).assertEndpoint("POST", HOST, ADDRESSES_EP + "/export")
                .assertJson("filename", "contacts.csv")
                .assertJson("format", "csv")
                .assertJson("columns[0]", "address_1")
                .assertJson("filters.search_query", "Bakery");
        assertEquals(Arrays.asList("gate_code", "floor"), fields.getData());
        assertEquals(1, depots.length);
        assertTrue(exported.getStatus());
    }

    @Test
    public void jobStatusAndResultPutTheJobIdInThePath() throws Exception {
        AddressBookManagerV5 manager = manager();
        http.respondToAll(200, STATUS_TRUE);

        manager.getJobStatus("JOB-1");
        StatusResponse result = manager.getJobResult("JOB-1");

        http.request(0).assertEndpoint("GET", HOST, BOOK_EP + "/job-tracker/status/JOB-1");
        http.request(1).assertEndpoint("GET", HOST, BOOK_EP + "/job-tracker/result/JOB-1");
        assertTrue(result.getStatus());
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

        http.expectApiException(() -> manager.getContacts(query()));
    }
}
