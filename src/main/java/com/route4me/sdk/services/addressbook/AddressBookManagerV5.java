package com.route4me.sdk.services.addressbook;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

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

    public AddressBookManagerV5(String apiKey) { super(apiKey); }
    public AddressBookManagerV5(String apiKey, boolean disableRedirects) { super(apiKey, disableRedirects); }

    private static URIBuilder v5Builder(String endpoint, Object... segments) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        Manager.withPathSegments(builder, endpoint, segments);
        return builder;
    }

    public AddressBookContactsResponse getContacts(AddressBookContactsBodyRequest request) throws APIException {
        return this.makeRequest(RequestMethod.POST, v5Builder(CONTACTS_ALL_EP), this.gson.toJson(request), AddressBookContactsResponse.class);
    }

    public AddressBookContactsResponse getContactsPaginated(AddressBookContactsBodyRequest request) throws APIException {
        return this.makeRequest(RequestMethod.POST, v5Builder(CONTACTS_PAGINATED_EP), this.gson.toJson(request), AddressBookContactsResponse.class);
    }

    public AddressBookContactV5 getContactById(long contactId) throws APIException {
        URIBuilder builder = v5Builder(CONTACTS_SHOW_EP);
        builder.setParameter("address_id", String.valueOf(contactId));
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, AddressBookContactV5.class);
    }

    public AddressBookContactsResponse getContactsByIds(long[] contactIds) throws APIException {
        ContactIdsRequest request = new ContactIdsRequest();
        request.setAddressIds(contactIds);
        return this.makeRequest(RequestMethod.POST, v5Builder(CONTACTS_SHOW_EP), this.gson.toJson(request), AddressBookContactsResponse.class);
    }

    public AddressBookContactV5 addContact(AddressBookContactV5 contact) throws APIException {
        return this.makeRequest(RequestMethod.POST, v5Builder(CONTACTS_EP), this.gson.toJson(contact), AddressBookContactV5.class);
    }

    public AddressBookContactV5 updateContact(long addressId, AddressBookContactV5 contact) throws APIException {
        return this.makeRequest(RequestMethod.PUT, v5Builder(CONTACTS_EP, addressId), this.gson.toJson(contact), AddressBookContactV5.class);
    }

    public AddressBookContactV5[] batchUpdateContacts(BatchUpdateContactsRequest request) throws APIException {
        return this.makeRequest(RequestMethod.PUT, v5Builder(CONTACTS_BATCH_UPDATE_EP), this.gson.toJson(request), AddressBookContactV5[].class);
    }

    public StatusResponse batchCreateContacts(BatchCreateContactsRequest request) throws APIException {
        return this.makeRequest(RequestMethod.POST, v5Builder(CONTACTS_BATCH_CREATE_EP), this.gson.toJson(request), StatusResponse.class);
    }

    public boolean deleteContacts(long[] contactIds) throws APIException {
        ContactIdsRequest request = new ContactIdsRequest();
        request.setAddressIds(contactIds);
        this.makeRequest(RequestMethod.DELETE, v5Builder(CONTACTS_DELETE_EP), this.gson.toJson(request), String.class);
        return true;
    }

    public CustomFieldsResponse getCustomFields() throws APIException {
        return this.makeRequest(RequestMethod.GET, v5Builder(CONTACTS_CUSTOM_FIELDS_EP), (HttpEntity) null, CustomFieldsResponse.class);
    }

    public AddressBookContactV5[] getDepots() throws APIException {
        return this.makeRequest(RequestMethod.GET, v5Builder(CONTACTS_DEPOTS_EP), (HttpEntity) null, AddressBookContactV5[].class);
    }

    public StatusResponse exportAddresses(AddressExportRequest request) throws APIException {
        return this.makeRequest(RequestMethod.POST, v5Builder(CONTACTS_EXPORT_EP), this.gson.toJson(request), StatusResponse.class);
    }

    public StatusResponse getJobStatus(String jobId) throws APIException {
        return this.makeRequest(RequestMethod.GET, v5Builder(JOB_STATUS_EP, jobId), (HttpEntity) null, StatusResponse.class);
    }

    public StatusResponse getJobResult(String jobId) throws APIException {
        return this.makeRequest(RequestMethod.GET, v5Builder(JOB_RESULT_EP, jobId), (HttpEntity) null, StatusResponse.class);
    }
}
