package com.route4me.sdk.services.addressbook;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.AsyncJob;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.responses.StatusResponse;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 * Manages the address book (contacts) and its groups through the V5 API ({@code
 * https://wh.route4me.com/modules/api/v5.0/address-book}).
 *
 * <p>Some operations run as a background job: the API answers {@code 202 Accepted}, usually with no body, and puts
 * the link to the job status in the {@code Location} header (and the job ID in {@code x-job-id} for
 * {@link #deleteContacts(long[])}). The methods that can start a job return an {@link AsyncJob}: pass its
 * {@link AsyncJob#getJobId()} to {@link #getJobStatus(String)} and {@link #getJobResult(String)}:
 * <pre>{@code
 * AsyncJob job = manager.removeAddressBookContactsByAreas(filter);
 * if (job.getJobId() != null) {
 *     JobStatusResponse status = manager.getJobStatus(job.getJobId());
 * }
 * }</pre>
 */
public class AddressBookManagerV5 extends Manager {
    private static final String ADDRESS_BOOK_BASE = V5_BASE + "/address-book";
    private static final String CONTACTS_EP = ADDRESS_BOOK_BASE + "/addresses";
    private static final String CONTACTS_ALL_EP = CONTACTS_EP + "/index/all";
    private static final String CONTACTS_PAGINATED_EP = CONTACTS_EP + "/index/pagination";
    private static final String CONTACTS_CLUSTERING_EP = CONTACTS_EP + "/index/clustering";
    private static final String CONTACTS_SHOW_EP = CONTACTS_EP + "/show";
    private static final String CONTACTS_BATCH_CREATE_EP = CONTACTS_EP + "/batch-create";
    private static final String CONTACTS_BATCH_UPDATE_EP = CONTACTS_EP + "/batch-update";
    private static final String CONTACTS_UPDATE_BY_AREAS_EP = CONTACTS_EP + "/update-by-areas";
    private static final String CONTACTS_DELETE_EP = CONTACTS_EP + "/delete";
    private static final String CONTACTS_DELETE_BY_AREAS_EP = CONTACTS_EP + "/delete-by-areas";
    private static final String CONTACTS_CUSTOM_FIELDS_EP = CONTACTS_EP + "/custom-fields";
    private static final String CONTACTS_DEPOTS_EP = CONTACTS_EP + "/depots";
    private static final String CONTACTS_EXPORT_EP = CONTACTS_EP + "/export";
    private static final String CONTACTS_EXPORT_STATUS_EP = CONTACTS_EXPORT_EP + "/status";
    private static final String CONTACTS_EXPORT_RESULT_EP = CONTACTS_EXPORT_EP + "/result";
    private static final String CONTACTS_EXPORT_BY_AREAS_EP = CONTACTS_EP + "/export-by-areas";
    private static final String CONTACTS_EXPORT_BY_AREA_IDS_EP = CONTACTS_EP + "/export-by-area-ids";
    private static final String JOB_STATUS_EP = ADDRESS_BOOK_BASE + "/job-tracker/status";
    private static final String JOB_RESULT_EP = ADDRESS_BOOK_BASE + "/job-tracker/result";
    private static final String GROUPS_EP = ADDRESS_BOOK_BASE + "/groups";
    private static final String GROUPS_FILTER_ALL_EP = GROUPS_EP + "/filter/all";

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

    /**
     * Lists the contacts that match query parameters: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/index/all}.
     *
     * @param query the fields, filters and range; an empty {@link AddressBookContactsQuery} (or {@code null} cast to
     *     it) for all the contacts
     * @return the contacts
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public AddressBookContactsResponse getContacts(AddressBookContactsQuery query) throws APIException {
        return this.makeJSONRequest(RequestMethod.GET, v5Builder(CONTACTS_ALL_EP), query,
                AddressBookContactsResponse.class);
    }

    /**
     * Lists all the contacts that match the filters: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/index/all}, with the request as the JSON
     * body. This is the POST form of {@link #getContacts(AddressBookContactsQuery)}; it can also filter by area.
     *
     * @param request the search text, filters, sort order, fields and range; an empty request for all the contacts
     * @return the contacts
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if {@code request} is {@code null}; nothing is sent
     */
    public AddressBookContactsResponse getContacts(AddressBookContactsBodyRequest request) throws APIException {
        requireRequest(request);
        return this.makeRequest(RequestMethod.POST, v5Builder(CONTACTS_ALL_EP), this.gson.toJson(request),
                AddressBookContactsResponse.class);
    }

    /**
     * Lists the contacts that match query parameters, one page at a time: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/index/pagination}.
     *
     * @param query the fields, filters and page; an empty {@link AddressBookContactsPaginatedQuery} (or {@code null}
     *     cast to it) for the first page of all the contacts
     * @return the page of contacts
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public AddressBookContactsResponse getContactsPaginated(AddressBookContactsPaginatedQuery query)
            throws APIException {
        return this.makeJSONRequest(RequestMethod.GET, v5Builder(CONTACTS_PAGINATED_EP), query,
                AddressBookContactsResponse.class);
    }

    /**
     * Lists the contacts that match the filters, one page at a time: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/index/pagination}, with the
     * request as the JSON body. This is the POST form of
     * {@link #getContactsPaginated(AddressBookContactsPaginatedQuery)}.
     *
     * @param request the search text, filters, sort order, fields and page; an empty request for the first page of
     *     all the contacts
     * @return the page of contacts
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if {@code request} is {@code null}; nothing is sent
     */
    public AddressBookContactsResponse getContactsPaginated(AddressBookContactsBodyPaginatedRequest request)
            throws APIException {
        requireRequest(request);
        return this.makeRequest(RequestMethod.POST, v5Builder(CONTACTS_PAGINATED_EP), this.gson.toJson(request),
                AddressBookContactsResponse.class);
    }

    /**
     * Groups the contacts that match query parameters into map clusters: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/index/clustering}.
     *
     * @param query the search text and display filters; an empty {@link AddressBookClusteringQuery} (or {@code null}
     *     cast to it) for all the contacts
     * @return the clusters
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public AddressBookClusteringResponse getAddressBookContactsClustering(AddressBookClusteringQuery query)
            throws APIException {
        return this.makeJSONRequest(RequestMethod.GET, v5Builder(CONTACTS_CLUSTERING_EP), query,
                AddressBookClusteringResponse.class);
    }

    /**
     * Groups the contacts that match the filters into map clusters: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/index/clustering}, with the request as the
     * JSON body. This is the POST form of {@link #getAddressBookContactsClustering(AddressBookClusteringQuery)}.
     *
     * @param request the search text, the clustering precision and the filters
     * @return the clusters
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if {@code request} is {@code null}; nothing is sent
     */
    public AddressBookClusteringResponse getAddressBookContactsClustering(AddressBookClusteringRequest request)
            throws APIException {
        requireRequest(request);
        return this.makeRequest(RequestMethod.POST, v5Builder(CONTACTS_CLUSTERING_EP), this.gson.toJson(request),
                AddressBookClusteringResponse.class);
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
     * @throws IllegalArgumentException if {@code contactIds} is {@code null} or empty; nothing is sent
     */
    public AddressBookContactsResponse getContactsByIds(long[] contactIds) throws APIException {
        requireIds(contactIds);
        ContactIdsRequest request = new ContactIdsRequest();
        request.setAddressIds(contactIds);
        return this.makeRequest(RequestMethod.POST, v5Builder(CONTACTS_SHOW_EP), this.gson.toJson(request),
                AddressBookContactsResponse.class);
    }

    /**
     * Creates a contact: {@code POST https://wh.route4me.com/modules/api/v5.0/address-book/addresses}, with the contact
     * as the JSON body.
     *
     * @param contact the contact; {@code address_1}, {@code cached_lat} and {@code cached_lng} are required
     * @return the created contact
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if a required field is missing; nothing is sent
     */
    public AddressBookContactV5 addContact(AddressBookContactV5 contact) throws APIException {
        checkContact(contact, "contact");
        return this.makeRequest(RequestMethod.POST, v5Builder(CONTACTS_EP), this.gson.toJson(contact),
                AddressBookContactV5.class);
    }

    /**
     * Updates a contact: {@code PUT https://wh.route4me.com/modules/api/v5.0/address-book/addresses/{addressId}}, with
     * the contact as the JSON body.
     *
     * @param addressId the contact ID
     * @param contact the new values; the spec requires {@code address_1}, {@code cached_lat} and {@code cached_lng}
     *     here too
     * @return the updated contact
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if a required field is missing; nothing is sent
     */
    public AddressBookContactV5 updateContact(long addressId, AddressBookContactV5 contact) throws APIException {
        checkContact(contact, "contact");
        return this.makeRequest(RequestMethod.PUT, v5Builder(CONTACTS_EP, addressId), this.gson.toJson(contact),
                AddressBookContactV5.class);
    }

    /**
     * Sets the same field values on several contacts: {@code PUT
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/batch-update}, with the request as the JSON
     * body: {@code address_ids} and the fields to set.
     *
     * @param request the contact IDs and the fields to set; at least one contact ID
     * @return the updated contacts; the spec describes {@code {"results": [...]}}, while the C# and PHP SDKs read an
     *     array, so both are read
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if {@code request} is {@code null} or has no {@code address_ids}; nothing is
     *     sent
     */
    public AddressBookContactsResponse batchUpdateContacts(BatchUpdateContactsRequest request) throws APIException {
        requireRequest(request);
        if (request.getAddressIds() == null || request.getAddressIds().isEmpty()) {
            throw new IllegalArgumentException("address_ids is required");
        }
        return this.makeRequest(RequestMethod.PUT, v5Builder(CONTACTS_BATCH_UPDATE_EP), this.gson.toJson(request),
                AddressBookContactsResponse.class);
    }

    /**
     * Sets the same field values on the contacts inside some areas: {@code PUT
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/update-by-areas}, with the request as the JSON
     * body. The API runs the update as a background job and answers {@code 202 Accepted}, with the job status URL
     * in {@code Location}; follow it with {@link #getJobStatus(String)}.
     *
     * @param request the areas and the fields to set; the filter must select at least one area
     * @return the accepted job: its ID, status URL and reply body
     * @throws APIException if the request fails or the server answers with an error status
     * @throws IllegalArgumentException if {@code request} or its {@code filter} is {@code null}, a
     *     {@code selected_areas} entry is {@code null} or lacks a {@code type} or a {@code value}, or the filter has
     *     neither {@code selected_areas} nor a {@code bounding_box} with its four edges; nothing is sent, since an
     *     update with an empty filter could change every contact
     */
    public AsyncJob updateAddressBookContactByAreas(UpdateContactsByAreasRequest request) throws APIException {
        requireRequest(request);
        requireArea(request.getFilter());
        return this.makeJobRequest(RequestMethod.PUT, v5Builder(CONTACTS_UPDATE_BY_AREAS_EP),
                this.gson.toJson(request));
    }

    /**
     * Creates several contacts: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/batch-create}, with the contacts as the JSON
     * body.
     *
     * @param request the contacts, at least one; each needs {@code address_1}, {@code cached_lat} and
     *     {@code cached_lng}
     * @return the status of the request
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if {@code request} is {@code null}, has no contacts, or a contact misses a
     *     required field; nothing is sent
     */
    public StatusResponse batchCreateContacts(BatchCreateContactsRequest request) throws APIException {
        if (request == null || request.getData() == null || request.getData().isEmpty()) {
            throw new IllegalArgumentException("data is required");
        }
        for (AddressBookContactV5 contact : request.getData()) {
            checkContact(contact, "data[]");
        }
        return this.makeRequest(RequestMethod.POST, v5Builder(CONTACTS_BATCH_CREATE_EP), this.gson.toJson(request),
                StatusResponse.class);
    }

    /**
     * Deletes contacts: {@code DELETE https://wh.route4me.com/modules/api/v5.0/address-book/addresses/delete}, with
     * {@code {"address_ids": [...]}} as the JSON body. The API deletes the contacts at once and answers
     * {@code 200} with {@code {"status": true}}, or queues the deletion as a background job: {@code 202 Accepted},
     * no body, and the job ID in {@code x-job-id}; follow it with {@link #getJobStatus(String)}.
     *
     * @param contactIds the contact IDs; at least one
     * @return the accepted job: its ID, status URL and reply body; the job ID is {@code null} when the contacts were
     *     deleted at once, and {@code getBodyAs(StatusResponse.class)} then reads the status
     * @throws APIException if the request fails or the server answers with an error status
     * @throws IllegalArgumentException if {@code contactIds} is {@code null} or empty; nothing is sent, since the
     *     endpoint also selects contacts by filters and an empty list must not reach it
     */
    public AsyncJob deleteContacts(long[] contactIds) throws APIException {
        requireIds(contactIds);
        ContactIdsRequest request = new ContactIdsRequest();
        request.setAddressIds(contactIds);
        return this.makeJobRequest(RequestMethod.DELETE, v5Builder(CONTACTS_DELETE_EP), this.gson.toJson(request));
    }

    /**
     * Deletes the contacts inside some areas: {@code DELETE
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/delete-by-areas}, with
     * {@code {"filter": ...}} as the JSON body. The API runs the deletion as a background job and answers
     * {@code 202 Accepted}, with the job status URL in {@code Location}; follow it with
     * {@link #getJobStatus(String)}.
     *
     * @param filter the contacts to delete: drawn areas or a rectangle, and optionally a text that narrows them
     * @return the accepted job: its ID, status URL and reply body
     * @throws APIException if the request fails or the server answers with an error status
     * @throws IllegalArgumentException if {@code filter} is {@code null}, a {@code selected_areas} entry is
     *     {@code null} or lacks a {@code type} or a {@code value}, or the filter has neither {@code selected_areas} nor
     *     a {@code bounding_box} with its four edges; nothing is sent, since a deletion with an empty filter could
     *     delete every contact
     */
    public AsyncJob removeAddressBookContactsByAreas(AddressBookAreasFilter filter) throws APIException {
        requireArea(filter);
        return this.makeJobRequest(RequestMethod.DELETE, v5Builder(CONTACTS_DELETE_BY_AREAS_EP),
                this.gson.toJson(new FilterBody(filter)));
    }

    /**
     * Lists the contact fields that address book groups can filter on, custom data fields included: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/custom-fields}.
     *
     * @return the fields
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
     * <p>The API builds the file in a background job and answers {@code 202 Accepted}, with no {@code X-Job-Id} and
     * the export status URL ({@code .../address-book/addresses/export/status/{id}}) in {@code Location}
     * ({@link AsyncJob#getLocation()}); {@link AsyncJob#getJobId()} reads the ID from it. Follow the job with
     * {@link #getExportStatus(String)}, then download the file with {@link #getExportResult(String)}.
     *
     * @param request the contact IDs, the file name and its format
     * @return the accepted job: its ID, status URL and reply body
     * @throws APIException if the request fails or the server answers with an error status
     * @throws IllegalArgumentException if {@code request} is {@code null}; nothing is sent
     */
    public AsyncJob exportAddresses(AddressExportRequest request) throws APIException {
        requireRequest(request);
        return this.makeJobRequest(RequestMethod.POST, v5Builder(CONTACTS_EXPORT_EP), this.gson.toJson(request));
    }

    /**
     * Starts an export of the contacts inside some areas to a file: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/export-by-areas}, with {@code {"filter": ...}}
     * as the JSON body.
     *
     * <p>The API builds the file in a background job and answers {@code 202 Accepted}, with no {@code X-Job-Id} and
     * the export status URL ({@code .../address-book/addresses/export/status/{id}}) in {@code Location}
     * ({@link AsyncJob#getLocation()}); {@link AsyncJob#getJobId()} reads the ID from it. Follow the job with
     * {@link #getExportStatus(String)}, then download the file with {@link #getExportResult(String)}.
     *
     * @param filter the contacts to export and the file to make
     * @return the accepted job: its ID, status URL and reply body
     * @throws APIException if the request fails or the server answers with an error status
     * @throws IllegalArgumentException if {@code filter} is {@code null}; nothing is sent
     */
    public AsyncJob exportAddressesByAreas(AddressExportByAreasFilter filter) throws APIException {
        if (filter == null) {
            throw new IllegalArgumentException("The filter is required");
        }
        return this.makeJobRequest(RequestMethod.POST, v5Builder(CONTACTS_EXPORT_BY_AREAS_EP),
                this.gson.toJson(new FilterBody(filter)));
    }

    /**
     * Starts an export of the contacts inside some territories to a file: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/export-by-area-ids}, with the request as the
     * JSON body.
     *
     * <p>The API builds the file in a background job and answers {@code 202 Accepted}, with no {@code X-Job-Id} and
     * the export status URL ({@code .../address-book/addresses/export/status/{id}}) in {@code Location}
     * ({@link AsyncJob#getLocation()}); {@link AsyncJob#getJobId()} reads the ID from it. Follow the job with
     * {@link #getExportStatus(String)}, then download the file with {@link #getExportResult(String)}.
     *
     * @param request the territory IDs and the file to make
     * @return the accepted job: its ID, status URL and reply body
     * @throws APIException if the request fails or the server answers with an error status
     * @throws IllegalArgumentException if {@code request} is {@code null}; nothing is sent
     */
    public AsyncJob exportAddressesByAreaIds(AddressExportByAreaIdsRequest request) throws APIException {
        requireRequest(request);
        return this.makeJobRequest(RequestMethod.POST, v5Builder(CONTACTS_EXPORT_BY_AREA_IDS_EP),
                this.gson.toJson(request));
    }

    /**
     * Gets the status of an address book export: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/export/status/{jobId}}. The API answers a JSON
     * string, which this method returns as text.
     *
     * @param jobId the export ID: {@link AsyncJob#getJobId()} of the job that {@link #exportAddresses},
     *     {@link #exportAddressesByAreas} or {@link #exportAddressesByAreaIds} started
     * @return the status: the value of the JSON string; any other answer as it came; {@code ""} for an empty answer
     * @throws APIException if the request fails or the server answers with an error status
     * @throws IllegalArgumentException if {@code jobId} is {@code null}, empty, {@code .} or {@code ..}, or contains
     *     {@code /}, {@code \} or {@code ;}; nothing is sent
     */
    public String getExportStatus(String jobId) throws APIException {
        String body = this.makeRequest(RequestMethod.GET, v5Builder(CONTACTS_EXPORT_STATUS_EP, jobId),
                (HttpEntity) null, String.class);
        return jsonStringValue(body);
    }

    /**
     * Downloads the file of a finished address book export: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/export/result/{jobId}}. The API answers the file
     * itself, for example as {@code text/csv}, and this method returns its bytes unchanged, so an {@code xls} or
     * {@code xlsx} file can be saved as it is. The whole file is held in memory.
     *
     * <p>To read a CSV file as text, decode it with its charset. The API does not document which charset it uses,
     * and this method does not return the reply's {@code Content-Type}. {@code new String(file,
     * StandardCharsets.UTF_8)} reads UTF-8, the charset of the API's JSON answers; check a file with accented
     * letters before you rely on it.
     *
     * @param jobId the export ID: {@link AsyncJob#getJobId()} of the export job
     * @return the bytes of the file, as the API sent them; an empty array for an empty answer
     * @throws APIException if the request fails or the server answers with an error status, for example while the
     *     export is still running
     * @throws IllegalArgumentException if {@code jobId} is {@code null}, empty, {@code .} or {@code ..}, or contains
     *     {@code /}, {@code \} or {@code ;}; nothing is sent
     */
    public byte[] getExportResult(String jobId) throws APIException {
        return this.makeRequest(RequestMethod.GET, v5Builder(CONTACTS_EXPORT_RESULT_EP, jobId), (HttpEntity) null,
                byte[].class);
    }

    /** Returns the value of a JSON string answer, or the answer as it is when it is not a JSON string. */
    private static String jsonStringValue(String body) {
        if (body == null) {
            return "";
        }
        try {
            JsonElement element = JsonParser.parseString(body);
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                return element.getAsString();
            }
        } catch (JsonParseException ex) {
            // Not JSON: return the text as it came.
        }
        return body;
    }

    /**
     * Gets the status of an address book background job: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/address-book/job-tracker/status/{jobId}}. When the job is over the API
     * answers {@code 303 See Other} to its result; see the {@code status} of {@link JobStatusResponse}.
     *
     * @param jobId the job ID, such as {@link AsyncJob#getJobId()} of the job a method of this manager started
     * @return the status
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if {@code jobId} is {@code null}, empty, {@code .} or {@code ..}, or contains
     *     {@code /}, {@code \} or {@code ;}; nothing is sent
     */
    public JobStatusResponse getJobStatus(String jobId) throws APIException {
        return this.makeRequest(RequestMethod.GET, v5Builder(JOB_STATUS_EP, jobId), (HttpEntity) null,
                JobStatusResponse.class);
    }

    /**
     * Gets the result of a finished address book background job: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/address-book/job-tracker/result/{jobId}}.
     *
     * @param jobId the job ID, such as {@link AsyncJob#getJobId()} of the job a method of this manager started
     * @return the result: {@code status} is {@code true} when the job succeeded
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if {@code jobId} is {@code null}, empty, {@code .} or {@code ..}, or contains
     *     {@code /}, {@code \} or {@code ;}; nothing is sent
     */
    public StatusResponse getJobResult(String jobId) throws APIException {
        return this.makeRequest(RequestMethod.GET, v5Builder(JOB_RESULT_EP, jobId), (HttpEntity) null,
                StatusResponse.class);
    }

    /**
     * Lists the address book groups: {@code GET https://wh.route4me.com/modules/api/v5.0/address-book/groups}.
     *
     * @return the groups
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public AddressBookGroupsResponse getAddressBookGroups() throws APIException {
        return this.makeRequest(RequestMethod.GET, v5Builder(GROUPS_EP), (HttpEntity) null,
                AddressBookGroupsResponse.class);
    }

    /**
     * Lists the contacts that match address book group rules: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/address-book/groups/filter/all?display=...}, with the request as the
     * JSON body.
     *
     * @param request the rules, and the range, fields and order of the contacts; {@code rules} is required
     * @param display which contacts to return: {@code all} (the default), {@code routed} or {@code unrouted};
     *     {@code null} leaves {@code display} out
     * @return the contacts that match
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if {@code rules} is missing; nothing is sent
     */
    public AddressBookContactsResponse filterContactsByGroupRules(AddressBookGroupRulesRequest request, String display)
            throws APIException {
        if (request == null || request.getRules() == null) {
            throw new IllegalArgumentException("rules is required");
        }
        URIBuilder builder = v5Builder(GROUPS_FILTER_ALL_EP);
        if (display != null) {
            builder.setParameter("display", display);
        }
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request),
                AddressBookContactsResponse.class);
    }


    /** Rejects a {@code null} or empty list of contact IDs, before any request is sent. */
    private static void requireIds(long[] contactIds) {
        if (contactIds == null || contactIds.length == 0) {
            throw new IllegalArgumentException("At least one contact ID is required");
        }
    }

    /**
     * Rejects an area filter that could select every contact, before any request is sent: it needs drawn areas or a
     * complete rectangle. A text alone is not enough: the spec marks only {@code query} and {@code bounding_box} as
     * optional, so it expects {@code selected_areas}, and a complete rectangle bounds the contacts as well. Every
     * drawn area must be complete, even next to a complete one or a rectangle: Gson would send an incomplete one as
     * {@code {}} or {@code null}, and how the API reads that is not documented.
     */
    private static void requireArea(AddressBookAreasFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("The filter is required");
        }
        List<SelectedArea> areas = filter.getSelectedAreas();
        if (areas != null && !areas.isEmpty()) {
            for (SelectedArea area : areas) {
                if (!isComplete(area)) {
                    throw new IllegalArgumentException("Every selected_areas entry needs a type and a value");
                }
            }
        } else if (!isComplete(filter.getBoundingBox())) {
            throw new IllegalArgumentException("The filter needs selected_areas or a bounding_box with four edges");
        }
    }

    /** Tells whether a drawn area has a type and a geometry. */
    private static boolean isComplete(SelectedArea area) {
        return area != null && area.getType() != null && !area.getType().isEmpty() && area.getValue() != null
                && !area.getValue().isEmpty();
    }

    /** Tells whether a rectangle has its four edges. */
    private static boolean isComplete(BoundingBox box) {
        return box != null && box.getTop() != null && box.getLeft() != null && box.getBottom() != null
                && box.getRight() != null;
    }

    /** Checks the fields the spec requires in a contact, before any request is sent. */
    private static void checkContact(AddressBookContactV5 contact, String name) {
        if (contact == null) {
            throw new IllegalArgumentException(name + " is required");
        }
        if (contact.getAddress1() == null || contact.getAddress1().isEmpty()) {
            throw new IllegalArgumentException(name + ".address_1 is required");
        }
        if (contact.getCachedLat() == null || contact.getCachedLng() == null) {
            throw new IllegalArgumentException(name + ".cached_lat and cached_lng are required");
        }
    }

    /** A {@code {"filter": ...}} body. */
    private static final class FilterBody {
        /** The filter ({@code filter}). */
        @SerializedName("filter")
        private final Object filter;

        FilterBody(Object filter) {
            this.filter = filter;
        }
    }
}
