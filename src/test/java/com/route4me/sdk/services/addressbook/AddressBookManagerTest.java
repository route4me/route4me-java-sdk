package com.route4me.sdk.services.addressbook;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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
    public void getContactsSendsIdsAndQueryParameters() throws Exception {
        AddressBookManager manager = manager();
        http.respondWithFixture(200, "contacts.json").respondWithFixture(200, "contacts.json");
        ContactsRequest byIds = new ContactsRequest().setAddressIds(Arrays.asList("12345", "12346"));
        ContactsRequest search = new ContactsRequest().setQuery("Bakery").setLimit(10).setOffset(20)
                .setFields("address_id,address_1").setDisplay("all").setStart(0).setEnd(5);

        List<Contact> contacts = manager.getContacts(byIds);
        manager.getContacts(search);

        http.request(0).assertEndpoint("GET", HOST, ADDRESS_BOOK_EP).assertQuery("address_id", "12345,12346");
        http.request(1).assertNoQuery("address_id")
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
    public void errorBecomesApiException() {
        final AddressBookManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.createContact(contact()));
    }
}
