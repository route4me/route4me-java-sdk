package com.route4me.sdk.integration;

import com.route4me.sdk.services.addressbook.*;
import com.route4me.sdk.exception.APIException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class AddressBookManagerV5IntegrationTest extends BaseIntegrationTest {
    private AddressBookManagerV5 manager;
    private List<Long> createdIds = new ArrayList<>();

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new AddressBookManagerV5(API_KEY);
    }

    @After
    public void tearDown() {
        if (!createdIds.isEmpty()) {
            try {
                long[] ids = createdIds.stream().mapToLong(Long::longValue).toArray();
                manager.deleteContacts(ids);
            } catch (Exception e) { }
        }
    }

    private AddressBookContactV5 buildContact() {
        AddressBookContactV5 contact = new AddressBookContactV5();
        contact.setAddress1("1600 Amphitheatre Pkwy, Mountain View, CA 94043");
        contact.setAddressAlias("SDK V5 Contact " + System.currentTimeMillis());
        contact.setCachedLat(37.4220);
        contact.setCachedLng(-122.0841);
        contact.setFirstName("SDK");
        contact.setLastName("Test");
        return contact;
    }

    @Test
    public void testAddAndGetContact() throws APIException {
        AddressBookContactV5 created = manager.addContact(buildContact());
        assertNotNull(created);
        assertNotNull(created.getAddressId());
        createdIds.add(created.getAddressId());

        AddressBookContactV5 fetched = manager.getContactById(created.getAddressId());
        assertNotNull(fetched);
    }

    @Test
    public void testGetContacts() throws APIException {
        AddressBookContactV5 created = manager.addContact(buildContact());
        createdIds.add(created.getAddressId());

        AddressBookContactsBodyRequest request = new AddressBookContactsBodyRequest();
        request.setPage(1);
        request.setPerPage(10);
        AddressBookContactsResponse response = manager.getContacts(request);
        assertNotNull(response);
    }

    @Test
    public void testUpdateContact() throws APIException {
        AddressBookContactV5 created = manager.addContact(buildContact());
        createdIds.add(created.getAddressId());

        AddressBookContactV5 update = new AddressBookContactV5();
        update.setAddressAlias("SDK Updated Contact");
        AddressBookContactV5 updated = manager.updateContact(created.getAddressId(), update);
        assertNotNull(updated);
    }

    @Test
    public void testDeleteContacts() throws APIException {
        AddressBookContactV5 created = manager.addContact(buildContact());
        assertNotNull(created.getAddressId());
        boolean deleted = manager.deleteContacts(new long[]{created.getAddressId()});
        assertTrue(deleted);
    }

    @Test
    public void testGetCustomFields() throws APIException {
        CustomFieldsResponse response = manager.getCustomFields();
        assertNotNull(response);
    }

    @Test
    public void testGetDepots() throws APIException {
        AddressBookContactV5[] depots = manager.getDepots();
        assertNotNull(depots);
    }
}
