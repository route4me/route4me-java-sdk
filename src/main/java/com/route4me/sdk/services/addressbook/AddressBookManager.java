package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.responses.DeleteResponse;
import java.util.ArrayList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.utils.URIBuilder;

import java.util.List;
import org.apache.http.HttpEntity;

public class AddressBookManager extends Manager {
    private static final String ADDRESS_BOOK_EP = "/api.v4/address_book.php";
    private static final String ADDRESS_BOOK_DEPOTS = "/modules/api/v5.0/address-book/addresses/depots"; 


    public AddressBookManager(String apiKey) {
        super(apiKey);
    }

    private URIBuilder getAPIV5URI(String endpoint){
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        builder.setPath(endpoint);
        return builder;
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
        ContactsResponse cr = this.makeJSONRequest(RequestMethod.GET, builder, request, ContactsResponse.class);
        return cr.getResults();
    }

    public boolean deleteContact(List<Number> addressIds) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ADDRESS_BOOK_EP);
        return this.makeRequest(RequestMethod.DELETE, builder, this.gson.toJson(new AddressIds(addressIds)), DeleteResponse.class).isDeleted();
    }
    
    public List<Contact> getDepotsFromAddressBook() throws APIException{
         URIBuilder builder = this.getAPIV5URI(ADDRESS_BOOK_DEPOTS);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, new TypeToken<ArrayList<Contact>>() {
        }.getType());
    }

    @Getter
    static class ContactsResponse {

        @SerializedName("results")
        private List<Contact> results;
        @SerializedName("total")
        private int total;
    }

    @Getter
    @RequiredArgsConstructor
    static class AddressIds {

        @SerializedName("address_ids")
        private final List<Number> addressIds;
    }

}
