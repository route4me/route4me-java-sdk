package com.route4me.sdk.services.addressbook;

import com.google.gson.Gson;
import com.route4me.sdk.responses.DeleteResponse;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.model.enums.APIEndpoints;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.utils.URIBuilder;

public class AddressBookManager extends Manager {

    public AddressBookManager(String apiKey) {
        super(apiKey, new Gson());
    }

    public Contact createContact(Contact contact) throws APIException {
        return this.makeRequest(RequestMethod.POST,
                Manager.defaultBuilder(APIEndpoints.ADDRESSBOOK),
                this.gson.toJson(contact),
                Contact.class);
    }

    public Contact updateContact(Contact contact) throws APIException {
        return this.makeRequest(RequestMethod.PUT,
                Manager.defaultBuilder(APIEndpoints.ADDRESSBOOK),
                this.gson.toJson(contact),
                Contact.class);
    }

    public Contact getContact(Number addressID) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(APIEndpoints.ADDRESSBOOK);
        builder.setParameter("address_id", "'" + addressID.toString() + "'");
        return this.makeRequest(RequestMethod.GET, builder, "", Contact.class);
    }

    @Getter
    static class ContactsResponse {

        private List<Contact> response;
        private int total;
    }

    public List<Contact> getContacts(int limit, int offset) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(APIEndpoints.ADDRESSBOOK);
        builder.setParameter("limit", Integer.toString(limit));
        builder.setParameter("offest", Integer.toString(offset));
        ContactsResponse cr = this.makeRequest(RequestMethod.GET, builder, "", ContactsResponse.class);
        return cr.getResponse();
    }

    @Getter
    @RequiredArgsConstructor
    static class AddressIds {

        private final List<Number> address_ids;
    }

    public boolean deleteContact(List<Number> addressIds) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(APIEndpoints.ADDRESSBOOK);
        return this.makeRequest(RequestMethod.DELETE, builder, this.gson.toJson(new AddressIds(addressIds)), DeleteResponse.class).isDeleted();
    }

}
