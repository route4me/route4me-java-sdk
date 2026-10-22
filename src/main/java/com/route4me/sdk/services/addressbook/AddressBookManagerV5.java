package com.route4me.sdk.services.addressbook;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.AsyncJob;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
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
    private static final String V5_BASE = "/modules/api/v5.0/address-book";
    private static final String CONTACTS_EP = V5_BASE + "/addresses";
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
    private static final String JOB_STATUS_EP = V5_BASE + "/job-tracker/status";
    private static final String JOB_RESULT_EP = V5_BASE + "/job-tracker/result";
    private static final String GROUPS_EP = V5_BASE + "/groups";
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

    private static URIBuilder v5Builder(String endpoint, Object... segments) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        Manager.withPathSegments(builder, endpoint, segments);
        return builder;
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
     * @param request the search text, filters, sort order, fields and range
     * @return the contacts
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public AddressBookContactsResponse getContacts(AddressBookContactsBodyRequest request) throws APIException {
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
     * @param request the search text, filters, sort order, fields and page
     * @return the page of contacts
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public AddressBookContactsResponse getContactsPaginated(AddressBookContactsBodyPaginatedRequest request)
            throws APIException {
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
     */
    public AddressBookClusteringResponse getAddressBookContactsClustering(AddressBookClusteringRequest request)
            throws APIException {
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
     * @param request the contact IDs and the fields to set
     * @return the updated contacts; the spec describes {@code {"results": [...]}}, while the C# and PHP SDKs read an
     *     array, so both are read
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public AddressBookContactsResponse batchUpdateContacts(BatchUpdateContactsRequest request) throws APIException {
        return this.makeRequest(RequestMethod.PUT, v5Builder(CONTACTS_BATCH_UPDATE_EP), this.gson.toJson(request),
                AddressBookContactsResponse.class);
    }

    /**
     * Sets the same field values on the contacts inside some areas: {@code PUT
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/update-by-areas}, with the request as the JSON
     * body. The API runs the update as a background job and answers {@code 202 Accepted}, with the job status URL
     * in {@code Location}; follow it with {@link #getJobStatus(String)}.
     *
     * @param request the areas and the fields to set
     * @return the accepted job: its ID, status URL and reply body
     * @throws APIException if the request fails or the server answers with an error status
     */
    public AsyncJob updateAddressBookContactByAreas(UpdateContactsByAreasRequest request) throws APIException {
        return this.makeJobRequest(RequestMethod.PUT, v5Builder(CONTACTS_UPDATE_BY_AREAS_EP),
                this.gson.toJson(request));
    }

    /**
     * Creates several contacts: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/batch-create}, with the contacts as the JSON
     * body.
     *
     * @param request the contacts; each needs {@code address_1}, {@code cached_lat} and {@code cached_lng}
     * @return the status of the request
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if a contact misses a required field; nothing is sent
     */
    public StatusResponse batchCreateContacts(BatchCreateContactsRequest request) throws APIException {
        if (request != null && request.getData() != null) {
            for (AddressBookContactV5 contact : request.getData()) {
                checkContact(contact, "data[]");
            }
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
     * @param contactIds the contact IDs
     * @return the accepted job: its ID, status URL and reply body; the job ID is {@code null} when the contacts were
     *     deleted at once, and {@code getBodyAs(StatusResponse.class)} then reads the status
     * @throws APIException if the request fails or the server answers with an error status
     */
    public AsyncJob deleteContacts(long[] contactIds) throws APIException {
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
     * @param filter the contacts to delete: a text, a rectangle or drawn areas
     * @return the accepted job: its ID, status URL and reply body
     * @throws APIException if the request fails or the server answers with an error status
     */
    public AsyncJob removeAddressBookContactsByAreas(AddressBookAreasFilter filter) throws APIException {
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
     */
    public AsyncJob exportAddresses(AddressExportRequest request) throws APIException {
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
     */
    public AsyncJob exportAddressesByAreas(AddressExportByAreasFilter filter) throws APIException {
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
     */
    public AsyncJob exportAddressesByAreaIds(AddressExportByAreaIdsRequest request) throws APIException {
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
     * @throws IllegalArgumentException if {@code jobId} is {@code null} or empty; nothing is sent
     */
    public String getExportStatus(String jobId) throws APIException {
        String body = this.makeRequest(RequestMethod.GET, v5Builder(CONTACTS_EXPORT_STATUS_EP, jobId),
                (HttpEntity) null, String.class);
        return jsonStringValue(body);
    }

    /**
     * Downloads the file of a finished address book export: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/address-book/addresses/export/result/{jobId}}. The API answers the file
     * itself, for example as {@code text/csv}.
     *
     * @param jobId the export ID: {@link AsyncJob#getJobId()} of the export job
     * @return the content of the file, as text; {@code ""} for an empty answer
     * @throws APIException if the request fails or the server answers with an error status, for example while the
     *     export is still running
     * @throws IllegalArgumentException if {@code jobId} is {@code null} or empty; nothing is sent
     */
    public String getExportResult(String jobId) throws APIException {
        return this.makeRequest(RequestMethod.GET, v5Builder(CONTACTS_EXPORT_RESULT_EP, jobId), (HttpEntity) null,
                String.class);
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
     * @throws IllegalArgumentException if {@code jobId} is {@code null} or empty; nothing is sent
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
     * @throws IllegalArgumentException if {@code jobId} is {@code null} or empty; nothing is sent
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
