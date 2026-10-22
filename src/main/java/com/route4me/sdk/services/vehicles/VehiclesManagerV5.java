package com.route4me.sdk.services.vehicles;

import com.route4me.sdk.AsyncJob;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.queryconverter.QueryConverter;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

import java.util.List;

/**
 * Manages the vehicles of the account, their vehicle profiles and their capacity profiles, through the V5 API
 * ({@code https://wh.route4me.com/modules/api/v5.0/vehicles}, {@code
 * https://wh.route4me.com/modules/api/v5.0/vehicle-profiles} and {@code
 * https://wh.route4me.com/modules/api/v5.0/vehicle-capacity-profiles}).
 *
 * <p>The bulk methods ({@link #activateVehicles(String[])}, {@link #deactivateVehicles(String[])},
 * {@link #deleteVehicles(String[])}, {@link #restoreVehicles(String[])} and {@link #updateVehicles(List)}) start an
 * asynchronous job: the API answers {@code 202 Accepted}, usually with an empty body and the job ID in the
 * {@code X-Job-Id} and {@code Location} response headers. They return an {@link AsyncJob}; follow the job with
 * {@link #getVehicleJobStatus(String)} and {@link #getVehicleJobResult(String)}:
 * <pre>{@code
 * AsyncJob job = manager.activateVehicles(vehicleIds);
 * if (job.getJobId() != null) {
 *     VehicleJobResponse status = manager.getVehicleJobStatus(job.getJobId());
 * }
 * }</pre>
 * When the API puts the job in the body instead, {@code job.getBodyAs(VehicleJobResponse.class)} reads it.
 */
public class VehiclesManagerV5 extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String VEHICLES_EP = V5_BASE + "/vehicles";
    private static final String VEHICLES_PAGINATE_EP = VEHICLES_EP + "/paginate";
    private static final String VEHICLES_SEARCH_EP = VEHICLES_EP + "/search";
    private static final String VEHICLES_LICENSE_EP = VEHICLES_EP + "/license";
    private static final String VEHICLES_LOCATION_EP = VEHICLES_EP + "/location";
    private static final String VEHICLES_ASSIGN_EP = VEHICLES_EP + "/assign";
    private static final String VEHICLES_EXECUTE_EP = VEHICLES_EP + "/execute";
    private static final String VEHICLES_SYNC_PENDING_EP = VEHICLES_EP + "/sync/pending";
    private static final String VEHICLES_BULK_ACTIVATE_EP = VEHICLES_EP + "/bulk/activate";
    private static final String VEHICLES_BULK_DEACTIVATE_EP = VEHICLES_EP + "/bulk/deactivate";
    private static final String VEHICLES_BULK_DELETE_EP = VEHICLES_EP + "/bulk/delete";
    private static final String VEHICLES_BULK_RESTORE_EP = VEHICLES_EP + "/bulk/restore";
    private static final String VEHICLES_BULK_UPDATE_EP = VEHICLES_EP + "/bulk/update";
    private static final String VEHICLES_JOB_RESULT_EP = VEHICLES_EP + "/job-tracker/result";
    private static final String VEHICLES_JOB_STATUS_EP = VEHICLES_EP + "/job-tracker/status";
    private static final String TRACK_SEGMENT = "track";
    private static final String VEHICLE_PROFILES_EP = V5_BASE + "/vehicle-profiles";
    private static final String VEHICLE_CAPACITY_PROFILES_EP = V5_BASE + "/vehicle-capacity-profiles";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     */
    public VehiclesManagerV5(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
    public VehiclesManagerV5(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    private static URIBuilder v5Builder(String endpoint, Object... segments) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        Manager.withPathSegments(builder, endpoint, segments);
        return builder;
    }

    /** Throws {@link IllegalArgumentException} when a field the API requires is {@code null}. */
    private static void require(Object value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(name + " is required");
        }
    }

    /** Throws {@link IllegalArgumentException} when a text the API requires is {@code null} or empty. */
    private static void requireText(String value, String name) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(name + " is required");
        }
    }

    // --- Vehicle CRUD ---

    /**
     * Lists the vehicles, all at once: {@code GET https://wh.route4me.com/modules/api/v5.0/vehicles}, with the
     * filters as query parameters. Set the {@code show} filter of {@link VehicleListRequest} to get the vehicles by
     * state ({@code active}, {@code pending}, {@code deleted} or {@code all}). For one page at a time, use
     * {@link #getPaginatedVehiclesList(VehiclePaginateRequest)}.
     *
     * @param filters the filters; {@code null}, or a {@code null} field, leaves them out
     * @return the vehicles
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleV5[] getVehicles(VehicleListRequest filters) throws APIException {
        URIBuilder builder = v5Builder(VEHICLES_EP);
        return this.makeJSONRequest(RequestMethod.GET, builder, filters, VehicleV5[].class);
    }

    /**
     * Lists the vehicles one page at a time: {@code GET https://wh.route4me.com/modules/api/v5.0/vehicles/paginate},
     * with the paging, the sort order and the filters as query parameters.
     *
     * @param request the paging, sort order and filters; {@code null}, or a {@code null} field, leaves them out
     * @return the page of vehicles
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehiclesPaginatedResponse getPaginatedVehiclesList(VehiclePaginateRequest request) throws APIException {
        URIBuilder builder = v5Builder(VEHICLES_PAGINATE_EP);
        return this.makeJSONRequest(RequestMethod.GET, builder, request, VehiclesPaginatedResponse.class);
    }

    /**
     * Gets one vehicle: {@code GET https://wh.route4me.com/modules/api/v5.0/vehicles/{vehicleId}}.
     *
     * @param vehicleId the vehicle ID
     * @return the vehicle
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleV5 getVehicleById(String vehicleId) throws APIException {
        URIBuilder builder = v5Builder(VEHICLES_EP, vehicleId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, VehicleV5.class);
    }

    /**
     * Creates a vehicle: {@code POST https://wh.route4me.com/modules/api/v5.0/vehicles}, with the vehicle as the JSON
     * body.
     *
     * @param vehicle the vehicle; {@code vehicle_alias} is required
     * @return the created vehicle
     * @throws IllegalArgumentException if {@code vehicle} or its alias is {@code null} or empty; nothing is sent
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleV5 createVehicle(VehicleV5 vehicle) throws APIException {
        require(vehicle, "vehicle");
        requireText(vehicle.getVehicleAlias(), "vehicle_alias");
        URIBuilder builder = v5Builder(VEHICLES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(vehicle), VehicleV5.class);
    }

    /**
     * Updates a vehicle: {@code PATCH https://wh.route4me.com/modules/api/v5.0/vehicles/{vehicleId}}, with the
     * vehicle as the JSON body. Only the fields that are set are sent, so the others keep their value.
     *
     * @param vehicleId the vehicle ID
     * @param vehicle the new values
     * @return the updated vehicle
     * @throws IllegalArgumentException if {@code vehicleId} is {@code null} or empty; nothing is sent
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleV5 updateVehicle(String vehicleId, VehicleV5 vehicle) throws APIException {
        URIBuilder builder = v5Builder(VEHICLES_EP, vehicleId);
        return this.makeRequest(RequestMethod.PATCH, builder, this.gson.toJson(vehicle), VehicleV5.class);
    }

    /**
     * Deletes a vehicle: {@code DELETE https://wh.route4me.com/modules/api/v5.0/vehicles/{vehicleId}}.
     *
     * @param vehicleId the vehicle ID
     * @return the deleted vehicle
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleV5 deleteVehicle(String vehicleId) throws APIException {
        URIBuilder builder = v5Builder(VEHICLES_EP, vehicleId);
        return this.makeRequest(RequestMethod.DELETE, builder, "", VehicleV5.class);
    }

    /**
     * Gets the track of a vehicle: {@code GET https://wh.route4me.com/modules/api/v5.0/vehicles/{vehicleId}/track},
     * optionally between two dates.
     *
     * @param vehicleId the vehicle ID
     * @param start the first day, such as {@code 2026-10-01}; {@code null} leaves {@code start} out
     * @param end the last day, such as {@code 2026-10-02}; {@code null} leaves {@code end} out
     * @return the positions of the track
     * @throws IllegalArgumentException if {@code vehicleId} is {@code null} or empty; nothing is sent
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleTrackResponse getVehicleTrack(String vehicleId, String start, String end) throws APIException {
        URIBuilder builder = v5Builder(VEHICLES_EP, vehicleId, TRACK_SEGMENT);
        if (start != null) {
            builder.setParameter("start", start);
        }
        if (end != null) {
            builder.setParameter("end", end);
        }
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, VehicleTrackResponse.class);
    }

    /**
     * Finds where some vehicles are, and how far they are from a location: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/vehicles/search}, with {@code {"vehicle_ids": [...], "lat": ...,
     * "lng": ...}} as the JSON body.
     *
     * @param request the vehicle IDs and the location; all its fields are required
     * @return one result per vehicle found
     * @throws IllegalArgumentException if {@code request} or one of its fields is {@code null}; nothing is sent
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleSearchResult[] searchVehicles(VehicleSearchRequest request) throws APIException {
        require(request, "request");
        require(request.getVehicleIds(), "vehicle_ids");
        require(request.getLat(), "lat");
        require(request.getLng(), "lng");
        URIBuilder builder = v5Builder(VEHICLES_SEARCH_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), VehicleSearchResult[].class);
    }

    /**
     * Finds a vehicle by its license plate: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/vehicles/license?vehicle_license_plate=...}.
     *
     * @param licensePlate the license plate, at most 7 characters
     * @return the vehicle, under {@code data.vehicle}
     * @throws IllegalArgumentException if {@code licensePlate} is {@code null} or empty; nothing is sent
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleResponse getVehicleByLicensePlate(String licensePlate) throws APIException {
        requireText(licensePlate, "vehicle_license_plate");
        URIBuilder builder = v5Builder(VEHICLES_LICENSE_EP);
        builder.setParameter("vehicle_license_plate", licensePlate);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, VehicleResponse.class);
    }

    /**
     * Gets the last known position of vehicles: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/vehicles/location?ids[]=...}, one {@code ids[]} per vehicle.
     *
     * @param vehicleIds the vehicle IDs; {@code null} sends none
     * @return the positions
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleLocationResponse getVehicleLocations(String[] vehicleIds) throws APIException {
        URIBuilder builder = v5Builder(VEHICLES_LOCATION_EP);
        builder.addParameters(QueryConverter.toBracketedParameters("ids", vehicleIds));
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, VehicleLocationResponse.class);
    }

    /**
     * Assigns a vehicle to a member until a given time: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/vehicles/assign}, with the assignment as the JSON body.
     *
     * @param request the assignment; {@code vehicle_id}, {@code assigned_member_id} and {@code expires_at} are
     *     required
     * @return the assignments
     * @throws IllegalArgumentException if {@code request} or one of its required fields is {@code null}, or
     *     {@code vehicle_id} is empty; nothing is sent
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public AssignedVehiclesResponse createTemporaryVehicle(VehicleTemporaryRequest request) throws APIException {
        require(request, "request");
        requireText(request.getVehicleId(), "vehicle_id");
        require(request.getAssignedMemberId(), "assigned_member_id");
        require(request.getExpiresAt(), "expires_at");
        URIBuilder builder = v5Builder(VEHICLES_ASSIGN_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), AssignedVehiclesResponse.class);
    }

    /**
     * Executes the order of a vehicle at a location: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/vehicles/execute}, with {@code {"vehicle_id": ..., "lat": ...,
     * "lng": ...}} as the JSON body.
     *
     * @param request the vehicle and the location; all its fields are required
     * @return the vehicle and the order
     * @throws IllegalArgumentException if {@code request} or one of its fields is {@code null}, or {@code vehicle_id}
     *     is empty; nothing is sent
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleOrderResponse executeVehicleOrder(VehicleOrderRequest request) throws APIException {
        require(request, "request");
        requireText(request.getVehicleId(), "vehicle_id");
        require(request.getLat(), "lat");
        require(request.getLng(), "lng");
        URIBuilder builder = v5Builder(VEHICLES_EXECUTE_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), VehicleOrderResponse.class);
    }

    /**
     * Syncs a pending vehicle from a telematics connection: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/vehicles/sync/pending}, with the vehicle as the JSON body. The API
     * identifies the vehicle by its {@code vehicle_alias}, {@code r4m_telematics_gateway_connection_id} and
     * {@code r4m_telematics_gateway_vehicle_id}.
     *
     * @param vehicle the vehicle
     * @return the synced vehicle
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleV5 syncPendingTelematicsData(VehicleV5 vehicle) throws APIException {
        URIBuilder builder = v5Builder(VEHICLES_SYNC_PENDING_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(vehicle), VehicleV5.class);
    }

    // --- Bulk Operations ---

    /**
     * Activates vehicles, as an asynchronous job: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/vehicles/bulk/activate}, with {@code {"vehicle_ids": [...]}} as the
     * JSON body.
     *
     * @param vehicleIds the vehicle IDs; required
     * @return the accepted job: its ID, status URL and reply body
     * @throws IllegalArgumentException if {@code vehicleIds} is {@code null}; nothing is sent
     * @throws APIException if the request fails or the server answers with an error status
     */
    public AsyncJob activateVehicles(String[] vehicleIds) throws APIException {
        return this.startBulkJob(VEHICLES_BULK_ACTIVATE_EP, vehicleIds);
    }

    /**
     * Deactivates vehicles, as an asynchronous job: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/vehicles/bulk/deactivate}, with {@code {"vehicle_ids": [...]}} as the
     * JSON body.
     *
     * @param vehicleIds the vehicle IDs; required
     * @return the accepted job: its ID, status URL and reply body
     * @throws IllegalArgumentException if {@code vehicleIds} is {@code null}; nothing is sent
     * @throws APIException if the request fails or the server answers with an error status
     */
    public AsyncJob deactivateVehicles(String[] vehicleIds) throws APIException {
        return this.startBulkJob(VEHICLES_BULK_DEACTIVATE_EP, vehicleIds);
    }

    /**
     * Deletes vehicles, as an asynchronous job: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/vehicles/bulk/delete}, with {@code {"vehicle_ids": [...]}} as the JSON
     * body.
     *
     * @param vehicleIds the vehicle IDs; required
     * @return the accepted job: its ID, status URL and reply body
     * @throws IllegalArgumentException if {@code vehicleIds} is {@code null}; nothing is sent
     * @throws APIException if the request fails or the server answers with an error status
     */
    public AsyncJob deleteVehicles(String[] vehicleIds) throws APIException {
        return this.startBulkJob(VEHICLES_BULK_DELETE_EP, vehicleIds);
    }

    /**
     * Restores deleted vehicles, as an asynchronous job: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/vehicles/bulk/restore}, with {@code {"vehicle_ids": [...]}} as the
     * JSON body.
     *
     * @param vehicleIds the vehicle IDs; required
     * @return the accepted job: its ID, status URL and reply body
     * @throws IllegalArgumentException if {@code vehicleIds} is {@code null}; nothing is sent
     * @throws APIException if the request fails or the server answers with an error status
     */
    public AsyncJob restoreVehicles(String[] vehicleIds) throws APIException {
        return this.startBulkJob(VEHICLES_BULK_RESTORE_EP, vehicleIds);
    }

    /**
     * Updates several vehicles at once, as an asynchronous job: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/vehicles/bulk/update}, with {@code {"vehicles": [...]}} as the JSON
     * body.
     *
     * @param vehicles the vehicles, each with its {@code vehicle_id} and the fields to change; required
     * @return the accepted job: its ID, status URL and reply body
     * @throws IllegalArgumentException if {@code vehicles}, one of them or its {@code vehicle_id} is {@code null}, or
     *     a {@code vehicle_id} is empty; nothing is sent
     * @throws APIException if the request fails or the server answers with an error status
     */
    public AsyncJob updateVehicles(List<VehicleV5> vehicles) throws APIException {
        require(vehicles, "vehicles");
        for (VehicleV5 vehicle : vehicles) {
            require(vehicle, "vehicles[]");
            requireText(vehicle.getVehicleId(), "vehicles[].vehicle_id");
        }
        VehicleBulkUpdateRequest request = new VehicleBulkUpdateRequest();
        request.setVehicles(vehicles);
        URIBuilder builder = v5Builder(VEHICLES_BULK_UPDATE_EP);
        return this.makeJobRequest(RequestMethod.POST, builder, this.gson.toJson(request));
    }

    /** Posts {@code {"vehicle_ids": [...]}} to a bulk endpoint, which starts an asynchronous job. */
    private AsyncJob startBulkJob(String endpoint, String[] vehicleIds) throws APIException {
        require(vehicleIds, "vehicle_ids");
        VehicleIdsRequest request = new VehicleIdsRequest();
        request.setVehicleIds(vehicleIds);
        URIBuilder builder = v5Builder(endpoint);
        return this.makeJobRequest(RequestMethod.POST, builder, this.gson.toJson(request));
    }

    /**
     * Gets the result of a vehicle background job: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/vehicles/job-tracker/result/{jobId}}. While the job runs, the API
     * answers {@code 409}, which throws.
     *
     * @param jobId the job ID, such as {@link AsyncJob#getJobId()} of the job a bulk method started
     * @return the result: {@code status} is {@code "true"} and {@code result} holds what the job wrote
     * @throws IllegalArgumentException if {@code jobId} is {@code null} or empty; nothing is sent
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleJobResponse getVehicleJobResult(String jobId) throws APIException {
        URIBuilder builder = v5Builder(VEHICLES_JOB_RESULT_EP, jobId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, VehicleJobResponse.class);
    }

    /**
     * Gets the status of a vehicle background job: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/vehicles/job-tracker/status/{jobId}}. While the job runs, the status
     * is {@code processing}. Once it is over, the API answers {@code 303 See Other} towards the result: a manager
     * that follows redirects (the default) returns that result, as {@link #getVehicleJobResult(String)} does; one
     * created with redirects disabled returns the final status ({@code processed}, {@code failed} or
     * {@code canceled}).
     *
     * @param jobId the job ID, such as {@link AsyncJob#getJobId()} of the job a bulk method started
     * @return the status, or the result
     * @throws IllegalArgumentException if {@code jobId} is {@code null} or empty; nothing is sent
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleJobResponse getVehicleJobStatus(String jobId) throws APIException {
        URIBuilder builder = v5Builder(VEHICLES_JOB_STATUS_EP, jobId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, VehicleJobResponse.class);
    }

    // --- Vehicle Profiles ---

    /**
     * Lists the vehicle profiles one page at a time: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/vehicle-profiles?with_pagination=1}. The API requires
     * {@code with_pagination}; this method always sends {@code 1}, because the unpaginated answer is a bare array of
     * another shape.
     *
     * @param page the page number, from 1; {@code null} leaves {@code page} out
     * @param perPage the number of items per page; {@code null} leaves {@code per_page} out
     * @return the page of vehicle profiles
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleProfilesResponse getVehicleProfiles(Integer page, Integer perPage) throws APIException {
        URIBuilder builder = v5Builder(VEHICLE_PROFILES_EP);
        builder.setParameter("with_pagination", "1");
        if (page != null) {
            builder.setParameter("page", String.valueOf(page));
        }
        if (perPage != null) {
            builder.setParameter("per_page", String.valueOf(perPage));
        }
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, VehicleProfilesResponse.class);
    }

    /**
     * Gets one vehicle profile: {@code GET https://wh.route4me.com/modules/api/v5.0/vehicle-profiles/{profileId}}.
     *
     * @param profileId the profile ID
     * @return the profile
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleProfile getVehicleProfileById(String profileId) throws APIException {
        URIBuilder builder = v5Builder(VEHICLE_PROFILES_EP, profileId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, VehicleProfile.class);
    }

    /**
     * Creates a vehicle profile: {@code POST https://wh.route4me.com/modules/api/v5.0/vehicle-profiles}, with the
     * profile as the JSON body.
     *
     * @param profile the profile; {@code name} is required
     * @return the created profile
     * @throws IllegalArgumentException if {@code profile} or its name is {@code null} or empty; nothing is sent
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleProfile createVehicleProfile(VehicleProfile profile) throws APIException {
        require(profile, "profile");
        requireText(profile.getName(), "name");
        URIBuilder builder = v5Builder(VEHICLE_PROFILES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(profile), VehicleProfile.class);
    }

    /**
     * Updates a vehicle profile: {@code PATCH
     * https://wh.route4me.com/modules/api/v5.0/vehicle-profiles/{profileId}}, with the profile as the JSON body.
     *
     * @param profileId the profile ID
     * @param profile the new values
     * @return the updated profile
     * @throws IllegalArgumentException if {@code profileId} is {@code null} or empty; nothing is sent
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleProfile updateVehicleProfile(String profileId, VehicleProfile profile) throws APIException {
        URIBuilder builder = v5Builder(VEHICLE_PROFILES_EP, profileId);
        return this.makeRequest(RequestMethod.PATCH, builder, this.gson.toJson(profile), VehicleProfile.class);
    }

    /**
     * Deletes a vehicle profile: {@code DELETE https://wh.route4me.com/modules/api/v5.0/vehicle-profiles/{profileId}}.
     *
     * @param profileId the profile ID
     * @return the deleted profile
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleProfile deleteVehicleProfile(String profileId) throws APIException {
        URIBuilder builder = v5Builder(VEHICLE_PROFILES_EP, profileId);
        return this.makeRequest(RequestMethod.DELETE, builder, "", VehicleProfile.class);
    }

    // --- Vehicle Capacity Profiles ---

    /**
     * Lists the vehicle capacity profiles: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/vehicle-capacity-profiles}.
     *
     * @param page the page number, from 1; {@code null} leaves {@code page} out
     * @param perPage the number of items per page; {@code null} leaves {@code per_page} out
     * @return the capacity profiles
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleCapacityProfilesResponse getVehicleCapacityProfiles(Integer page, Integer perPage)
            throws APIException {
        URIBuilder builder = v5Builder(VEHICLE_CAPACITY_PROFILES_EP);
        if (page != null) {
            builder.setParameter("page", String.valueOf(page));
        }
        if (perPage != null) {
            builder.setParameter("per_page", String.valueOf(perPage));
        }
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, VehicleCapacityProfilesResponse.class);
    }

    /**
     * Gets one vehicle capacity profile: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/vehicle-capacity-profiles/{profileId}}.
     *
     * @param profileId the profile ID
     * @return the profile
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleCapacityProfileResponse getVehicleCapacityProfileById(String profileId) throws APIException {
        URIBuilder builder = v5Builder(VEHICLE_CAPACITY_PROFILES_EP, profileId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, VehicleCapacityProfileResponse.class);
    }

    /**
     * Creates a vehicle capacity profile: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/vehicle-capacity-profiles}, with the profile as the JSON body.
     *
     * @param profile the profile; {@code name} is required
     * @return the created profile
     * @throws IllegalArgumentException if {@code profile} or its name is {@code null} or empty; nothing is sent
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleCapacityProfileResponse createVehicleCapacityProfile(VehicleCapacityProfile profile)
            throws APIException {
        require(profile, "profile");
        requireText(profile.getName(), "name");
        URIBuilder builder = v5Builder(VEHICLE_CAPACITY_PROFILES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(profile),
                VehicleCapacityProfileResponse.class);
    }

    /**
     * Updates a vehicle capacity profile: {@code PATCH
     * https://wh.route4me.com/modules/api/v5.0/vehicle-capacity-profiles/{profileId}}, with the profile as the JSON
     * body.
     *
     * @param profileId the profile ID
     * @param profile the new values
     * @return the updated profile
     * @throws IllegalArgumentException if {@code profileId} is {@code null} or empty; nothing is sent
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleCapacityProfileResponse updateVehicleCapacityProfile(String profileId, VehicleCapacityProfile profile)
            throws APIException {
        URIBuilder builder = v5Builder(VEHICLE_CAPACITY_PROFILES_EP, profileId);
        return this.makeRequest(RequestMethod.PATCH, builder, this.gson.toJson(profile),
                VehicleCapacityProfileResponse.class);
    }

    /**
     * Deletes a vehicle capacity profile: {@code DELETE
     * https://wh.route4me.com/modules/api/v5.0/vehicle-capacity-profiles/{profileId}}.
     *
     * @param profileId the profile ID
     * @return the deleted profile
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleCapacityProfileResponse deleteVehicleCapacityProfile(String profileId) throws APIException {
        URIBuilder builder = v5Builder(VEHICLE_CAPACITY_PROFILES_EP, profileId);
        return this.makeRequest(RequestMethod.DELETE, builder, "", VehicleCapacityProfileResponse.class);
    }
}
