package com.route4me.sdk.services.addressbook;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 * Manages the address book (contacts) through the V5 API ({@code
 * https://wh.route4me.com/modules/api/v5.0/address-book}).
 */
public class AddressBookManagerV5 extends Manager {
    private static final String V5_BASE = "/modules/api/v5.0/address-book";
    private static final String CONTACTS_EP = V5_BASE + "/addresses";
    private static final String CONTACTS_ALL_EP = CONTACTS_EP + "/index/all";
    private static final String CONTACTS_PAGINATED_EP = CONTACTS_EP + "/index/pagination";
    private static final String CONTACTS_SHOW_EP = CONTACTS_EP + "/show";
    private static final String CONTACTS_BATCH_CREATE_EP = CONTACTS_EP + "/batch-create";
    private static final String CONTACTS_BATCH_UPDATE_EP = CONTACTS_EP + "/batch-update";
    private static final String CONTACTS_DELETE_EP = CONTACTS_EP + "/delete";
    private static final String CONTACTS_CUSTOM_FIELDS_EP = CONTACTS_EP + "/custom-fields";
    private static final String CONTACTS_DEPOTS_EP = CONTACTS_EP + "/depots";
    private static final String CONTACTS_EXPORT_EP = CONTACTS_EP + "/export";
    private static final String JOB_STATUS_EP = V5_BASE + "/job-tracker/status";
    private static final String JOB_RESULT_EP = V5_BASE + "/job-tracker/result";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     */
    public AddressBookManagerV5(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
    public AddressBookManagerV5(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    private static URIBuilder v5Builder(String endpoint, Object... segments) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        Manager.withPathSegments(builder, endpoint, segments);
        return builder;
    }

    /**
     * Lists all the contacts that match the filters: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/index/all}, with the request as the JSON
     * body.
     *
     * @param request the filters, sort order and fields
     * @return the contacts
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public AddressBookContactsResponse getContacts(AddressBookContactsBodyRequest request) throws APIException {
        return this.makeRequest(RequestMethod.POST, v5Builder(CONTACTS_ALL_EP), this.gson.toJson(request),
                AddressBookContactsResponse.class);
    }

    /**
     * Lists the contacts that match the filters, one page at a time: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/index/pagination}, with the
     * request as the JSON body.
     *
     * @param request the filters, sort order, page and fields
     * @return the page of contacts
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public AddressBookContactsResponse getContactsPaginated(AddressBookContactsBodyRequest request)
            throws APIException {
        return this.makeRequest(RequestMethod.POST, v5Builder(CONTACTS_PAGINATED_EP), this.gson.toJson(request),
                AddressBookContactsResponse.class);
    }

    /**
     * Gets one contact: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/show?address_id=...}.
     *
     * @param contactId the contact ID
     * @return the contact
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public AddressBookContactV5 getContactById(long contactId) throws APIException {
        URIBuilder builder = v5Builder(CONTACTS_SHOW_EP);
        builder.setParameter("address_id", String.valueOf(contactId));
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, AddressBookContactV5.class);
    }

    /**
     * Gets several contacts: {@code POST https://wh.route4me.com/modules/api/v5.0/address-book/addresses/show}, with
     * {@code {"address_ids": [...]}} as the JSON body.
     *
     * @param contactIds the contact IDs
     * @return the contacts
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public AddressBookContactsResponse getContactsByIds(long[] contactIds) throws APIException {
        ContactIdsRequest request = new ContactIdsRequest();
        request.setAddressIds(contactIds);
        return this.makeRequest(RequestMethod.POST, v5Builder(CONTACTS_SHOW_EP), this.gson.toJson(request),
                AddressBookContactsResponse.class);
    }

    /**
     * Creates a contact: {@code POST https://wh.route4me.com/modules/api/v5.0/address-book/addresses}, with the contact
     * as the JSON body.
     *
     * @param contact the contact
     * @return the created contact
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public AddressBookContactV5 addContact(AddressBookContactV5 contact) throws APIException {
        return this.makeRequest(RequestMethod.POST, v5Builder(CONTACTS_EP), this.gson.toJson(contact),
                AddressBookContactV5.class);
    }

    /**
     * Updates a contact: {@code PUT https://wh.route4me.com/modules/api/v5.0/address-book/addresses/{addressId}}, with
     * the contact as the JSON body.
     *
     * @param addressId the contact ID
     * @param contact the new values
     * @return the updated contact
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public AddressBookContactV5 updateContact(long addressId, AddressBookContactV5 contact) throws APIException {
        return this.makeRequest(RequestMethod.PUT, v5Builder(CONTACTS_EP, addressId), this.gson.toJson(contact),
                AddressBookContactV5.class);
    }

    /**
     * Updates several contacts: {@code PUT
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/batch-update}, with the contacts as the JSON
     * body.
     *
     * @param request the contacts
     * @return the updated contacts
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public AddressBookContactV5[] batchUpdateContacts(BatchUpdateContactsRequest request) throws APIException {
        return this.makeRequest(RequestMethod.PUT, v5Builder(CONTACTS_BATCH_UPDATE_EP), this.gson.toJson(request),
                AddressBookContactV5[].class);
    }

    /**
     * Creates several contacts: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/batch-create}, with the contacts as the JSON
     * body.
     *
     * @param request the contacts
     * @return the status of the request
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public StatusResponse batchCreateContacts(BatchCreateContactsRequest request) throws APIException {
        return this.makeRequest(RequestMethod.POST, v5Builder(CONTACTS_BATCH_CREATE_EP), this.gson.toJson(request),
                StatusResponse.class);
    }

    /**
     * Deletes contacts: {@code DELETE https://wh.route4me.com/modules/api/v5.0/address-book/addresses/delete}, with
     * {@code {"address_ids": [...]}} as the JSON body.
     *
     * @param contactIds the contact IDs
     * @return {@code true}; an error status throws instead
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public boolean deleteContacts(long[] contactIds) throws APIException {
        ContactIdsRequest request = new ContactIdsRequest();
        request.setAddressIds(contactIds);
        this.makeRequest(RequestMethod.DELETE, v5Builder(CONTACTS_DELETE_EP), this.gson.toJson(request), String.class);
        return true;
    }

    /**
     * Lists the custom fields of the address book: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/custom-fields}.
     *
     * @return the custom fields
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public CustomFieldsResponse getCustomFields() throws APIException {
        return this.makeRequest(RequestMethod.GET, v5Builder(CONTACTS_CUSTOM_FIELDS_EP), (HttpEntity) null,
                CustomFieldsResponse.class);
    }

    /**
     * Lists the contacts that are depots: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/depots}.
     *
     * @return the depots
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public AddressBookContactV5[] getDepots() throws APIException {
        return this.makeRequest(RequestMethod.GET, v5Builder(CONTACTS_DEPOTS_EP), (HttpEntity) null,
                AddressBookContactV5[].class);
    }

    /**
     * Starts an export of contacts to a file: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/export}, with the request as the JSON body.
     *
     * @param request the filters, the columns, the file name and the format
     * @return the status of the request
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public StatusResponse exportAddresses(AddressExportRequest request) throws APIException {
        return this.makeRequest(RequestMethod.POST, v5Builder(CONTACTS_EXPORT_EP), this.gson.toJson(request),
                StatusResponse.class);
    }

    /**
     * Gets the status of an address book background job: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/address-book/job-tracker/status/{jobId}}.
     *
     * @param jobId the job ID
     * @return the status
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public StatusResponse getJobStatus(String jobId) throws APIException {
        return this.makeRequest(RequestMethod.GET, v5Builder(JOB_STATUS_EP, jobId), (HttpEntity) null,
                StatusResponse.class);
    }

    /**
     * Gets the result of an address book background job: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/address-book/job-tracker/result/{jobId}}.
     *
     * @param jobId the job ID
     * @return the result
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public StatusResponse getJobResult(String jobId) throws APIException {
        return this.makeRequest(RequestMethod.GET, v5Builder(JOB_RESULT_EP, jobId), (HttpEntity) null,
                StatusResponse.class);
    }
}
