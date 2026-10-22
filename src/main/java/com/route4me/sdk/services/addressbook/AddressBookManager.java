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

/**
 * Manages the address book (contacts) and its groups through the V4 API ({@code
 * https://api.route4me.com/api.v4/address_book.php} and {@code address_book_group.php}).
 * {@link AddressBookManagerV5} covers the V5 address book API.
 */
public class AddressBookManager extends Manager {
    private static final String ADDRESS_BOOK_EP = "/api.v4/address_book.php";
    private static final String ADDRESS_BOOK_GROUP_EP = "/api.v4/address_book_group.php";
    private static final String GROUP_SEARCH_EP = "/api/address_book/get_search_group_addresses.php";
    private static final String SAVE_GEOCODED_EP = "/api/address_book/save_geocoded.php";
    private static final String ADDRESS_BOOK_DEPOTS = "/modules/api/v5.0/address-book/addresses/depots";


    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token to the V5 API and as the {@code api_key} query
     *     parameter to the V4 API
     */
    public AddressBookManager(String apiKey) {
        super(apiKey);
    }

    private URIBuilder getAPIV5URI(String endpoint) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        builder.setPath(endpoint);
        return builder;
    }



    /**
     * Creates a contact: {@code POST https://api.route4me.com/api.v4/address_book.php}, with the contact as the JSON
     * body.
     *
     * @param contact the contact
     * @return the created contact
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Contact createContact(Contact contact) throws APIException {
        return this.makeRequest(RequestMethod.POST,
                Manager.defaultBuilder(ADDRESS_BOOK_EP),
                this.gson.toJson(contact),
                Contact.class);
    }

    /**
     * Updates a contact: {@code PUT https://api.route4me.com/api.v4/address_book.php}, with the contact as the JSON
     * body.
     *
     * @param contact the contact, with its address ID
     * @return the updated contact
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Contact updateContact(Contact contact) throws APIException {
        return this.makeRequest(RequestMethod.PUT,
                Manager.defaultBuilder(ADDRESS_BOOK_EP),
                this.gson.toJson(contact),
                Contact.class);
    }

    /**
     * Gets one address book contact: {@code GET /api.v4/address_book.php?address_id=...}.
     *
     * @param addressID the contact's address ID
     * @return the contact, or {@code null} when the address book has no contact with that ID
     * @throws APIException if the request fails
     */
    public Contact getContact(Number addressID) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ADDRESS_BOOK_EP);
        builder.setParameter("address_id", addressID.toString());
        ContactsResponse response = this.makeRequest(RequestMethod.GET, builder, "", ContactsResponse.class);
        if (response == null || response.getResults() == null || response.getResults().isEmpty()) {
            return null;
        }
        return response.getResults().get(0);
    }

    /**
     * Searches the address book: {@code GET https://api.route4me.com/api.v4/address_book.php}, with the IDs, the
     * search text, the display filter and the range of the request in the query. When the request names the
     * {@code fields} to return, the API answers rows of values instead of contacts; use
     * {@link #searchAddressBookLocation(ContactsRequest)} for that.
     *
     * @param request the IDs or search parameters
     * @return the contacts, or {@code null} when the answer has no results
     * @throws APIException if the request fails
     */
    public List<Contact> getContacts(ContactsRequest request) throws APIException {
        URIBuilder builder = contactsQuery(request);
        ContactsResponse cr = this.makeJSONRequest(RequestMethod.GET, builder, request, ContactsResponse.class);
        return cr == null ? null : cr.getResults();
    }

    /**
     * Searches the address book and returns some fields of each contact: {@code GET
     * https://api.route4me.com/api.v4/address_book.php}, with the IDs, the search text ({@code query}), the
     * {@code fields}, the display filter and the range ({@code offset}, {@code limit}) of the request in the query.
     * With {@code fields}, the API answers each contact as a row of values in that order.
     *
     * @param request the search: usually {@code query} and {@code fields}, a comma-separated list such as
     *     {@code address_id,address_1}
     * @return the rows (or contacts) and their total
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if {@code request} is {@code null}; nothing is sent
     */
    public AddressBookSearchResponse searchAddressBookLocation(ContactsRequest request) throws APIException {
        if (request == null) {
            throw new IllegalArgumentException("The search (request) is required");
        }
        URIBuilder builder = contactsQuery(request);
        return this.makeJSONRequest(RequestMethod.GET, builder, request, AddressBookSearchResponse.class);
    }

    /**
     * Searches the address book with a filter: {@code POST https://api.route4me.com/api.v4/address_book.php}, with the
     * range in the query ({@code offset}, {@code limit}) and the request as the JSON body. The {@code filter} of the
     * body tells this search apart from {@link #createContact(Contact)}, which uses the same endpoint.
     *
     * @param request the filter and the fields to return; the filter is required
     * @param offset the number of contacts to skip; {@code null} leaves {@code offset} out
     * @param limit the number of contacts to return; {@code null} leaves {@code limit} out
     * @return the contacts that match, as rows of values when the request names the fields
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if {@code request} or its {@code filter} is {@code null}; nothing is sent
     *     (without the filter, the same POST would be a {@link #createContact(Contact)} attempt)
     */
    public AddressBookSearchResponse searchAddressBookContactsByFilter(AddressBookFilterSearchRequest request,
            Integer offset, Integer limit) throws APIException {
        if (request == null || request.getFilter() == null) {
            throw new IllegalArgumentException("The filter is required");
        }
        URIBuilder builder = Manager.defaultBuilder(ADDRESS_BOOK_EP);
        if (offset != null) {
            builder.setParameter("offset", String.valueOf(offset));
        }
        if (limit != null) {
            builder.setParameter("limit", String.valueOf(limit));
        }
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request),
                AddressBookSearchResponse.class);
    }

    /**
     * Lists the contacts of an address book group: {@code POST
     * https://api.route4me.com/api/address_book/get_search_group_addresses.php?group_id=...}, with
     * {@code {"fields": [...]}} as the JSON body.
     *
     * @param groupId the ID of the group; required
     * @param fields the fields to return for each contact, such as {@code address_id}; {@code null} for the API's
     *     default
     * @return the contacts of the group, as rows of values in the order of {@code fields}
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if {@code groupId} is {@code null} or empty; nothing is sent
     */
    public AddressBookSearchResponse getAddressBookContactsByGroup(String groupId, List<String> fields)
            throws APIException {
        requireText(groupId, "group_id");
        URIBuilder builder = Manager.defaultBuilder(GROUP_SEARCH_EP);
        builder.setParameter("group_id", groupId);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(new GroupFields(fields)),
                AddressBookSearchResponse.class);
    }

    /**
     * Saves the geocoded addresses of a temporary optimization to the address book: {@code POST
     * https://api.route4me.com/api/address_book/save_geocoded.php}, with
     * {@code {"optimization_problem_id": "..."}} as the JSON body.
     *
     * @param optimizationProblemId the ID of the temporary optimization that holds the geocoded addresses; required
     * @return the status of the request
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if {@code optimizationProblemId} is {@code null} or empty; nothing is sent
     */
    public SaveGeocodedAddressesResponse saveGeocodedAddressesToDatabase(String optimizationProblemId)
            throws APIException {
        requireText(optimizationProblemId, "optimization_problem_id");
        return this.makeRequest(RequestMethod.POST, Manager.defaultBuilder(SAVE_GEOCODED_EP),
                this.gson.toJson(new OptimizationProblemId(optimizationProblemId)),
                SaveGeocodedAddressesResponse.class);
    }

    /**
     * Creates an address book group: {@code POST https://api.route4me.com/api.v4/address_book_group.php}, with the
     * group as the JSON body.
     *
     * @param group the group: its name (required), color, icon and the rules ({@code filter}) its contacts match
     * @return the created group, with its ID
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if {@code group} is {@code null} or has no {@code group_name}; nothing is sent
     */
    public AddressBookGroup addAddressBookGroup(AddressBookGroup group) throws APIException {
        requireText(group == null ? null : group.getGroupName(), "group_name");
        return this.makeRequest(RequestMethod.POST, Manager.defaultBuilder(ADDRESS_BOOK_GROUP_EP),
                this.gson.toJson(group), AddressBookGroup.class);
    }

    /**
     * Gets one address book group: {@code GET
     * https://api.route4me.com/api.v4/address_book_group.php?group_id=...}. This is the same request as
     * {@link #getAddressBookGroups(Integer, Integer)} with a {@code group_id}; the API then answers one group
     * instead of a list.
     *
     * @param groupId the ID of the group; required
     * @return the group
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if {@code groupId} is {@code null} or empty; nothing is sent
     */
    public AddressBookGroup getAddressBookGroup(String groupId) throws APIException {
        requireText(groupId, "group_id");
        URIBuilder builder = Manager.defaultBuilder(ADDRESS_BOOK_GROUP_EP);
        builder.setParameter("group_id", groupId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, AddressBookGroup.class);
    }

    /**
     * Lists the address book groups: {@code GET https://api.route4me.com/api.v4/address_book_group.php}. Every
     * {@code null} argument is left out of the query.
     *
     * @param offset the number of groups to skip ({@code offset})
     * @param limit the number of groups to return ({@code limit})
     * @return the groups
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public AddressBookGroup[] getAddressBookGroups(Integer offset, Integer limit) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ADDRESS_BOOK_GROUP_EP);
        if (offset != null) {
            builder.setParameter("offset", String.valueOf(offset));
        }
        if (limit != null) {
            builder.setParameter("limit", String.valueOf(limit));
        }
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, AddressBookGroup[].class);
    }

    /**
     * Updates an address book group: {@code PUT https://api.route4me.com/api.v4/address_book_group.php}, with the
     * group as the JSON body.
     *
     * @param group the group, with its {@code group_id}, which is required, and the values to change
     * @return the updated group
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if the {@code group_id} is missing; nothing is sent
     */
    public AddressBookGroup updateAddressBookGroup(AddressBookGroup group) throws APIException {
        requireText(group == null ? null : group.getGroupId(), "group_id");
        return this.makeRequest(RequestMethod.PUT, Manager.defaultBuilder(ADDRESS_BOOK_GROUP_EP),
                this.gson.toJson(group), AddressBookGroup.class);
    }

    /**
     * Deletes an address book group: {@code DELETE https://api.route4me.com/api.v4/address_book_group.php}, with
     * {@code {"group_id": "..."}} as the JSON body.
     *
     * @param groupId the ID of the group; required
     * @return the status of the request
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if {@code groupId} is {@code null} or empty; nothing is sent
     */
    public StatusResponse removeAddressBookGroup(String groupId) throws APIException {
        requireText(groupId, "group_id");
        return this.makeRequest(RequestMethod.DELETE, Manager.defaultBuilder(ADDRESS_BOOK_GROUP_EP),
                this.gson.toJson(new GroupId(groupId)), StatusResponse.class);
    }

    /** Builds the URL of the V4 contact searches, with the IDs of the request as a comma-separated address_id. */
    private static URIBuilder contactsQuery(ContactsRequest request) {
        URIBuilder builder = Manager.defaultBuilder(ADDRESS_BOOK_EP);
        if (request.getAddressIds() != null && !request.getAddressIds().isEmpty()) {
            StringBuilder addresses = new StringBuilder();
            for (String address : request.getAddressIds()) {
                addresses.append(address).append(',');
            }
            builder.setParameter("address_id", addresses.substring(0, addresses.length() - 1));
        }
        return builder;
    }

    /** Rejects a required text that is {@code null} or empty, before any request is sent. */
    private static void requireText(String value, String name) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(name + " is required");
        }
    }

    /**
     * Deletes address book contacts: {@code DELETE /api.v4/address_book.php}, with the IDs in
     * the JSON body.
     *
     * @param addressIds the contacts' address IDs
     * @return whether they were deleted; {@code false} also when the answer is empty
     * @throws APIException if the request fails
     */
    public boolean deleteContact(List<Number> addressIds) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ADDRESS_BOOK_EP);
        DeleteResponse response = this.makeRequest(RequestMethod.DELETE, builder,
                this.gson.toJson(new AddressIds(addressIds)), DeleteResponse.class);
        return response != null && response.isDeleted();
    }

    /**
     * Lists the contacts that are depots: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/depots}.
     *
     * @return the depots
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public List<Contact> getDepotsFromAddressBook() throws APIException {
        URIBuilder builder = this.getAPIV5URI(ADDRESS_BOOK_DEPOTS);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, new TypeToken<ArrayList<Contact>>() {
        }.getType());
    }

    @Getter
    static class ContactsResponse {

        /** An array of the Order type objects ({@code results}). */
        @SerializedName("results")
        private List<Contact> results;
        /** Total number of the returned orders ({@code total}). */
        @SerializedName("total")
        private int total;
    }

    @Getter
    @RequiredArgsConstructor
    static class GroupId {

        /** The ID of the group ({@code group_id}). */
        @SerializedName("group_id")
        private final String groupId;
    }

    @Getter
    @RequiredArgsConstructor
    static class GroupFields {

        /** The fields to return ({@code fields}). */
        @SerializedName("fields")
        private final List<String> fields;
    }

    @Getter
    @RequiredArgsConstructor
    static class OptimizationProblemId {

        /** The ID of the temporary optimization ({@code optimization_problem_id}). */
        @SerializedName("optimization_problem_id")
        private final String optimizationProblemId;
    }

    @Getter
    @RequiredArgsConstructor
    static class AddressIds {

        /** The IDs of the contacts to delete ({@code address_ids}). */
        @SerializedName("address_ids")
        private final List<Number> addressIds;
    }

}
