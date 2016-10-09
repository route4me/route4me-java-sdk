package com.route4me.sdk.services.addressbook;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.responses.DeleteResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.utils.URIBuilder;

import java.util.List;

public class AddressBookManager extends Manager {
    public static final String ADDRESS_BOOK_EP = "/api.v4/address_book.php";


    public AddressBookManager(String apiKey) {
        super(apiKey, new Gson());
    }

    public Contact createContact(Contact contact) throws APIException {
        return this.makeRequest(RequestMethod.POST,
                Manager.defaultBuilder(ADDRESS_BOOK_EP),
                this.gson.toJson(contact),
                Contact.class);
    }

    public Contact updateContact(Contact contact) throws APIException {
        return this.makeRequest(RequestMethod.PUT,
                Manager.defaultBuilder(ADDRESS_BOOK_EP),
                this.gson.toJson(contact),
                Contact.class);
    }

    public Contact getContact(Number addressID) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ADDRESS_BOOK_EP);
        builder.setParameter("address_id", "'" + addressID.toString() + "'");
        return this.makeRequest(RequestMethod.GET, builder, "", ContactsResponse.class).getResults().get(0);
    }

    public List<Contact> getContacts(ContactsRequest request) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ADDRESS_BOOK_EP);
        if (request.getAddressIds() != null && !request.getAddressIds().isEmpty()) {
            StringBuilder addresses = new StringBuilder();
            for (String address : request.getAddressIds()) {
                addresses.append(address).append(',');
            }
            builder.setParameter("address_id", addresses.substring(0, addresses.length() - 1));
        }
        if (request.getLimit() != null) {
            builder.setParameter("limit", Integer.toString(request.getLimit()));
        }
        if (request.getOffset() != null) {
            builder.setParameter("offset", Integer.toString(request.getOffset()));
        }
        if (request.getStart() != null) {
            builder.setParameter("start", Integer.toString(request.getStart()));
        }
        if (request.getQuery() != null) {
            builder.setParameter("query", request.getQuery());
        }
        if (request.getFields() != null) {
            builder.setParameter("fields", request.getFields());
        }
        if (request.getDisplay() != null) {
            builder.setParameter("display", request.getDisplay());
        }
        ContactsResponse cr = this.makeRequest(RequestMethod.GET, builder, "", ContactsResponse.class);
        return cr.getResults();
    }

    public boolean deleteContact(List<Number> addressIds) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ADDRESS_BOOK_EP);
        return this.makeRequest(RequestMethod.DELETE, builder, this.gson.toJson(new AddressIds(addressIds)), DeleteResponse.class).isDeleted();
    }

    @Getter
    static class ContactsResponse {

        private List<Contact> results;
        private int total;
    }

    @Getter
    @RequiredArgsConstructor
    static class AddressIds {

        @SerializedName("address_ids")
        private final List<Number> addressIds;
    }

}
