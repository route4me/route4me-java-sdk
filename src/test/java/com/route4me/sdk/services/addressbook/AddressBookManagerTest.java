package com.route4me.sdk.services.addressbook;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link AddressBookManager}: the V4 {@code address_book.php} contacts and
 * the V5 depot list.
 */
public class AddressBookManagerTest {

    private static final String HOST = "api.route4me.com";
    private static final String ADDRESS_BOOK_EP = "/api.v4/address_book.php";

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private AddressBookManager manager() {
        return http.install(new AddressBookManager(MockHttp.API_KEY));
    }

    /** Builds a sample address book contact with fake data. */
    private static Contact contact() {
        Contact contact = new Contact();
        contact.setAddressAlias("Example Bakery");
        contact.setAddress1("100 Example St, Springfield");
        contact.setCachedLat(40.1);
        contact.setCachedLng(-74.1);
        contact.setFirstName("Alex");
        return contact;
    }

    @Test
    @SuppressWarnings("deprecation") // the V4 contact methods are deprecated (obsolete in C#) but stay tested
    public void createContactPostsTheContact() throws Exception {
        AddressBookManager manager = manager();
        http.respondWithFixture(200, "contact.json");

        Contact created = manager.createContact(contact());

        http.request().assertEndpoint("POST", HOST, ADDRESS_BOOK_EP).assertApiKeyInQuery()
                .assertJson("address_alias", "Example Bakery")
                .assertJson("address_1", "100 Example St, Springfield")
                .assertJson("cached_lat", 40.1)
                .assertJson("first_name", "Alex");
        assertEquals(12345, created.getAddressId().intValue());
        assertEquals("A1", created.getAddressCustomData().get("gate_code"));
    }

    @Test
    @SuppressWarnings("deprecation") // the V4 contact methods are deprecated (obsolete in C#) but stay tested
    public void updateContactPutsTheContact() throws Exception {
        AddressBookManager manager = manager();
        http.respondWithFixture(200, "contact.json");
        Contact change = contact();
        change.setAddressId(12345);

        Contact updated = manager.updateContact(change);

        http.request().assertEndpoint("PUT", HOST, ADDRESS_BOOK_EP).assertJson("address_id", 12345);
        assertEquals("Springfield", updated.getAddressCity());
    }

    @Test
    public void getContactSendsTheIdAndReturnsTheFirstResult() throws Exception {
        AddressBookManager manager = manager();
        http.respondWithFixture(200, "contacts.json");

        Contact contact = manager.getContact(12345);

        http.request().assertEndpoint("GET", HOST, ADDRESS_BOOK_EP).assertNoBody();
        assertTrue(http.request().query("address_id").contains("12345"));
        assertEquals("Example Bakery", contact.getAddressAlias());
    }

    @Test
    public void getContactSendsThePlainId() throws Exception {
        AddressBookManager manager = manager();
        http.respondWithFixture(200, "contacts.json");

        manager.getContact(12345);

        http.request().assertQuery("address_id", "12345");
    }

    @Test
    @SuppressWarnings("deprecation") // the V4 contact methods are deprecated (obsolete in C#) but stay tested
    public void emptyResponsesDoNotThrow() throws Exception {
        AddressBookManager manager = manager();
        http.respondWithoutBody(200).respondWithoutBody(200);

        assertNull(manager.getContacts(new ContactsRequest()));
        assertFalse(manager.deleteContact(Arrays.<Number>asList(12345)));
    }

    @Test
    public void getContactReturnsNullWhenNothingMatches() throws Exception {
        AddressBookManager manager = manager();
        http.respond(200, json("{'results':[],'total':0}")).respond(200, "{}").respondWithoutBody(200);

        assertNull(manager.getContact(99999));
        assertNull(manager.getContact(99999));
        assertNull(manager.getContact(99999));
    }

    @Test
    @SuppressWarnings("deprecation") // the V4 contact methods are deprecated (obsolete in C#) but stay tested
    public void getContactsSendsIdsAndQueryParameters() throws Exception {
        AddressBookManager manager = manager();
        http.respondWithFixture(200, "contacts.json").respondWithFixture(200, "contacts.json");
        ContactsRequest byIds = new ContactsRequest().setAddressIds(Arrays.asList("12345", "12346"));
        ContactsRequest search = new ContactsRequest().setQuery("Bakery").setLimit(10).setOffset(20)
                .setFields("address_id,address_1").setDisplay("all").setStart(0).setEnd(5);

        List<Contact> contacts = manager.getContacts(byIds);
        manager.getContacts(search);

        http.request(0).assertEndpoint("GET", HOST, ADDRESS_BOOK_EP).assertApiKeyInQuery().assertNoBody()
                .assertQuery("address_id", "12345,12346");
        http.request(1).assertNoBody().assertNoQuery("address_id")
                .assertQuery("query", "Bakery")
                .assertQuery("limit", "10")
                .assertQuery("offset", "20")
                .assertQuery("fields", "address_id,address_1")
                .assertQuery("display", "all")
                .assertQuery("start", "0")
                .assertQuery("end", "5");
        assertEquals(2, contacts.size());
        assertTrue(contacts.get(1).getEligibleDepot());
    }

    @Test
    @SuppressWarnings("deprecation") // the V4 contact methods are deprecated (obsolete in C#) but stay tested
    public void deleteContactSendsTheIdsInTheBody() throws Exception {
        AddressBookManager manager = manager();
        http.respond(200, json("{'status':true}"));

        assertTrue(manager.deleteContact(Arrays.<Number>asList(12345, 12346)));

        http.request().assertEndpoint("DELETE", HOST, ADDRESS_BOOK_EP)
                .assertJson("address_ids[0]", 12345)
                .assertJson("address_ids[1]", 12346);
    }

    @Test
    public void getDepotsFromAddressBookUsesTheV5Endpoint() throws Exception {
        AddressBookManager manager = manager();
        http.respond(200, json("[{'address_id':12346,'address_alias':'Sample Depot','eligible_depot':true}]"));

        List<Contact> depots = manager.getDepotsFromAddressBook();

        http.request().assertEndpoint("GET", "wh.route4me.com", "/modules/api/v5.0/address-book/addresses/depots");
        assertEquals("Sample Depot", depots.get(0).getAddressAlias());
    }

    @Test
    @SuppressWarnings("deprecation") // the V4 contact methods are deprecated (obsolete in C#) but stay tested
    public void createContactSendsTheMemberId() throws Exception {
        AddressBookManager manager = manager();
        http.respondWithFixture(200, "contact.json");
        Contact contact = contact();
        contact.setMemberId(1001);

        manager.createContact(contact);

        http.request().assertEndpoint("POST", HOST, ADDRESS_BOOK_EP).assertJson("member_id", 1001);
    }

    @Test
    public void searchAddressBookLocationReadsRowsOfFields() throws Exception {
        AddressBookManager manager = manager();
        http.respond(200, json("{'results':[[12345,'100 Example St'],[12346,'200 Example Ave']],'total':2,"
                + "'fields':['address_id','address_1']}"))
                .respond(200, json("{'results':[{'address_id':12345,'address_1':'100 Example St'}],'total':1}"));
        ContactsRequest search = new ContactsRequest().setQuery("Example").setFields("address_id,address_1")
                .setOffset(0).setLimit(20).setDisplay("routed");

        AddressBookSearchResponse rows = manager.searchAddressBookLocation(search);
        AddressBookSearchResponse objects = manager.searchAddressBookLocation(new ContactsRequest()
                .setAddressIds(Arrays.asList("12345")));

        http.request(0).assertEndpoint("GET", HOST, ADDRESS_BOOK_EP).assertApiKeyInQuery().assertNoBody()
                .assertQuery("query", "Example")
                .assertQuery("fields", "address_id,address_1")
                .assertQuery("offset", "0")
                .assertQuery("limit", "20")
                .assertQuery("display", "routed");
        http.request(1).assertQuery("address_id", "12345");
        assertEquals(Integer.valueOf(2), rows.getTotal());
        assertEquals(Arrays.asList("address_id", "address_1"), rows.getFields());
        assertEquals("200 Example Ave", ((List<?>) rows.getResults().get(1)).get(1));
        assertEquals(12345.0, ((List<?>) rows.getResults().get(0)).get(0));
        assertEquals("100 Example St", ((Map<?, ?>) objects.getResults().get(0)).get("address_1"));
    }

    @Test
    public void searchesAndNewGroupsRejectMissingInputBeforeSending() {
        final AddressBookManager manager = manager();

        http.expectThrows(IllegalArgumentException.class, () -> manager.searchAddressBookContactsByFilter(null, 0, 10));
        http.expectThrows(IllegalArgumentException.class,
                () -> manager.searchAddressBookContactsByFilter(new AddressBookFilterSearchRequest(), 0, 10));
        http.expectThrows(IllegalArgumentException.class, () -> manager.searchAddressBookLocation(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.addAddressBookGroup(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.addAddressBookGroup(new AddressBookGroup()));
        http.assertNoRequests();
    }

    @Test
    public void searchAddressBookContactsByFilterPostsTheFilter() throws Exception {
        AddressBookManager manager = manager();
        http.respondToAll(200, json("{'results':[[12345,'100 Example St']],'total':1,"
                + "'fields':['address_id','address_1']}"));
        AddressBookSearchFilter filter = new AddressBookSearchFilter();
        filter.setQuery("Springfield");
        filter.setDisplay("all");
        AddressBookFilterSearchRequest request = new AddressBookFilterSearchRequest();
        request.setFields(Arrays.asList("address_id", "address_1"));
        request.setFilter(filter);

        AddressBookSearchResponse found = manager.searchAddressBookContactsByFilter(request, 10, 25);
        manager.searchAddressBookContactsByFilter(request, null, null);

        http.request(0).assertEndpoint("POST", HOST, ADDRESS_BOOK_EP).assertApiKeyInQuery()
                .assertQuery("offset", "10")
                .assertQuery("limit", "25")
                .assertJson("fields[1]", "address_1")
                .assertJson("filter.query", "Springfield")
                .assertJson("filter.display", "all");
        http.request(1).assertNoQuery("offset").assertNoQuery("limit");
        assertEquals(1, found.getResults().size());
    }

    @Test
    public void getAddressBookContactsByGroupSendsTheGroupInTheQuery() throws Exception {
        AddressBookManager manager = manager();
        http.respondToAll(200, json("{'results':[[12345]],'total':1,'fields':['address_id']}"));

        AddressBookSearchResponse contacts = manager.getAddressBookContactsByGroup("C0FFEE0000000000000000000000000B",
                Collections.singletonList("address_id"));
        manager.getAddressBookContactsByGroup("C0FFEE0000000000000000000000000B", null);

        http.request(0).assertEndpoint("POST", HOST, "/api/address_book/get_search_group_addresses.php")
                .assertApiKeyInQuery()
                .assertQuery("group_id", "C0FFEE0000000000000000000000000B")
                .assertJson("fields[0]", "address_id");
        http.request(1).assertJsonLacks("fields", "group_id");
        assertEquals(Integer.valueOf(1), contacts.getTotal());
    }

    @Test
    public void addressBookGroupsCrud() throws Exception {
        AddressBookManager manager = manager();
        String group = json("{'group_id':'C0FFEE0000000000000000000000000B','group_name':'North',"
                + "'group_color':'92e1c0','member_id':'1001','filter':{'condition':'AND','rules':[{'id':'address_1',"
                + "'field':'address_1','operator':'contains','value':'Main'}]}}");
        http.respond(200, group).respond(200, group).respond(200, "[" + group + "]").respond(200, "[]")
                .respond(200, group).respond(200, json("{'status':true}"));
        AddressBookGroupRule rule = new AddressBookGroupRule();
        rule.setId("address_1");
        rule.setField("address_1");
        rule.setOperator("contains");
        rule.setValue("Main");
        AddressBookGroupFilter filter = new AddressBookGroupFilter();
        filter.setCondition("AND");
        filter.setRules(Collections.singletonList(rule));
        AddressBookGroup create = new AddressBookGroup();
        create.setGroupName("North");
        create.setGroupColor("92e1c0");
        create.setFilter(filter);
        AddressBookGroup change = new AddressBookGroup();
        change.setGroupId("C0FFEE0000000000000000000000000B");
        change.setGroupColor("ff0000");

        AddressBookGroup created = manager.addAddressBookGroup(create);
        AddressBookGroup one = manager.getAddressBookGroup("C0FFEE0000000000000000000000000B");
        AddressBookGroup[] page = manager.getAddressBookGroups(0, 10);
        manager.getAddressBookGroups(null, null);
        manager.updateAddressBookGroup(change);
        StatusResponse removed = manager.removeAddressBookGroup("C0FFEE0000000000000000000000000B");

        String groupEp = "/api.v4/address_book_group.php";
        http.request(0).assertEndpoint("POST", HOST, groupEp).assertApiKeyInQuery()
                .assertJson("group_name", "North")
                .assertJson("group_color", "92e1c0")
                .assertJson("filter.condition", "AND")
                .assertJson("filter.rules[0].operator", "contains")
                .assertJsonLacks("group_id");
        http.request(1).assertEndpoint("GET", HOST, groupEp).assertNoBody()
                .assertQuery("group_id", "C0FFEE0000000000000000000000000B");
        http.request(2).assertEndpoint("GET", HOST, groupEp).assertQuery("offset", "0").assertQuery("limit", "10")
                .assertNoQuery("group_id");
        http.request(3).assertNoQuery("offset").assertNoQuery("limit");
        http.request(4).assertEndpoint("PUT", HOST, groupEp)
                .assertJson("group_id", "C0FFEE0000000000000000000000000B")
                .assertJson("group_color", "ff0000");
        http.request(5).assertEndpoint("DELETE", HOST, groupEp)
                .assertJson("group_id", "C0FFEE0000000000000000000000000B");
        assertEquals("C0FFEE0000000000000000000000000B", created.getGroupId());
        assertEquals(Long.valueOf(1001), one.getMemberId());
        assertEquals("Main", one.getFilter().getRules().get(0).getValue());
        assertEquals(1, page.length);
        assertTrue(removed.getStatus());
    }

    @Test
    public void saveGeocodedAddressesSendsTheIdAsText() throws Exception {
        AddressBookManager manager = manager();
        http.respond(200, json("{'status':true,'address_count':3}"));

        SaveGeocodedAddressesResponse saved = manager.saveGeocodedAddressesToDatabase(
                "C0FFEE0000000000000000000000000C");

        http.request().assertEndpoint("POST", HOST, "/api/address_book/save_geocoded.php").assertApiKeyInQuery()
                .assertJson("optimization_problem_id", "C0FFEE0000000000000000000000000C");
        assertTrue(http.request().getBody().contains("\"C0FFEE0000000000000000000000000C\""));
        assertTrue(saved.getStatus());
        assertEquals(Integer.valueOf(3), saved.getAddressCount());
    }

    @Test
    public void requiredIdsAreCheckedBeforeSending() {
        final AddressBookManager manager = manager();

        http.expectThrows(IllegalArgumentException.class, () -> manager.getAddressBookGroup(""));
        http.expectThrows(IllegalArgumentException.class, () -> manager.removeAddressBookGroup(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.updateAddressBookGroup(new AddressBookGroup()));
        http.expectThrows(IllegalArgumentException.class, () -> manager.updateAddressBookGroup(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.getAddressBookContactsByGroup(null, null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.saveGeocodedAddressesToDatabase(""));
        http.assertNoRequests();
    }

    @Test
    @SuppressWarnings("deprecation") // the V4 contact methods are deprecated (obsolete in C#) but stay tested
    public void errorBecomesApiException() {
        final AddressBookManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.createContact(contact()));
    }
}
