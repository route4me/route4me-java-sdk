package com.route4me.sdk.integration;

import com.route4me.sdk.services.addressbook.AddressBookManager;
import com.route4me.sdk.services.addressbook.Contact;
import com.route4me.sdk.services.addressbook.ContactsRequest;
import com.route4me.sdk.exception.APIException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

public class AddressBookManagerIntegrationTest extends BaseIntegrationTest {
    private AddressBookManager manager;
    private List<Number> createdIds = new ArrayList<>();

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new AddressBookManager(API_KEY);
    }

    @After
    public void tearDown() {
        if (!createdIds.isEmpty()) {
            try { manager.deleteContact(createdIds); } catch (Exception e) { }
        }
    }

    private Contact buildContact() {
        Contact contact = new Contact();
        contact.setAddress1("1600 Amphitheatre Pkwy, Mountain View, CA 94043");
        contact.setAddressAlias("SDK V4 Contact " + System.currentTimeMillis());
        contact.setCachedLat(37.4220);
        contact.setCachedLng(-122.0841);
        contact.setFirstName("SDK");
        contact.setLastName("Test");
        return contact;
    }

    @Test
    public void testCreateAndGetContact() throws APIException {
        Contact created = manager.createContact(buildContact());
        assertNotNull(created);
        assertNotNull(created.getAddressId());
        createdIds.add(created.getAddressId());

        Contact fetched = manager.getContact(created.getAddressId());
        assertNotNull(fetched);
    }

    @Test
    public void testGetContacts() throws APIException {
        Contact created = manager.createContact(buildContact());
        createdIds.add(created.getAddressId());

        ContactsRequest request = new ContactsRequest();
        request.setLimit(10);
        request.setOffset(0);
        List<Contact> contacts = manager.getContacts(request);
        assertNotNull(contacts);
    }

    @Test
    public void testUpdateContact() throws APIException {
        Contact created = manager.createContact(buildContact());
        createdIds.add(created.getAddressId());

        created.setAddressAlias("SDK Updated Contact V4");
        Contact updated = manager.updateContact(created);
        assertNotNull(updated);
    }

    @Test
    public void testDeleteContact() throws APIException {
        Contact created = manager.createContact(buildContact());
        assertNotNull(created.getAddressId());
        boolean deleted = manager.deleteContact(Collections.singletonList(created.getAddressId()));
        assertTrue(deleted);
    }
}
