package com.route4me.sdk.services.vehicles;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.queryconverter.QueryConverter;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 * Manages the vehicles of the account, their vehicle profiles and their capacity profiles, through the V5 API
 * ({@code https://wh.route4me.com/modules/api/v5.0/vehicles}, {@code
 * https://wh.route4me.com/modules/api/v5.0/vehicle-profiles} and {@code
 * https://wh.route4me.com/modules/api/v5.0/vehicle-capacity-profiles}).
 *
 * <p>The bulk methods ({@link #activateVehicles(String[])}, {@link #deactivateVehicles(String[])},
 * {@link #deleteVehicles(String[])} and {@link #restoreVehicles(String[])}) start an asynchronous job: the API
 * answers {@code 202 Accepted}, usually with an empty body and the job ID in the {@code X-Job-Id} and
 * {@code Location} response headers, which this manager cannot read yet. They return {@code null} for an empty
 * body, or the {@link VehicleJobResponse} the API sends; when it has a job ID ({@code job_id}), follow the job with
 * {@link #getVehicleJobStatus(String)} and {@link #getVehicleJobResult(String)}.
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
    private static final String VEHICLES_BULK_ACTIVATE_EP = VEHICLES_EP + "/bulk/activate";
    private static final String VEHICLES_BULK_DEACTIVATE_EP = VEHICLES_EP + "/bulk/deactivate";
    private static final String VEHICLES_BULK_DELETE_EP = VEHICLES_EP + "/bulk/delete";
    private static final String VEHICLES_BULK_RESTORE_EP = VEHICLES_EP + "/bulk/restore";
    private static final String VEHICLES_BULK_UPDATE_EP = VEHICLES_EP + "/bulk/update";
    private static final String VEHICLES_JOB_RESULT_EP = VEHICLES_EP + "/job-tracker/result";
    private static final String VEHICLES_JOB_STATUS_EP = VEHICLES_EP + "/job-tracker/status";
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
     * state ({@code active}, {@code pending}, {@code deleted} or {@code all}).
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

    // --- Bulk Operations ---

    /**
     * Activates vehicles, as an asynchronous job: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/vehicles/bulk/activate}, with {@code {"vehicle_ids": [...]}} as the
     * JSON body.
     *
     * @param vehicleIds the vehicle IDs; required
     * @return {@code null} when the API answers {@code 202 Accepted} with an empty body, as it documents; otherwise
     *     the job it sends
     * @throws IllegalArgumentException if {@code vehicleIds} is {@code null}; nothing is sent
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleJobResponse activateVehicles(String[] vehicleIds) throws APIException {
        return this.startBulkJob(VEHICLES_BULK_ACTIVATE_EP, vehicleIds);
    }

    /**
     * Deactivates vehicles, as an asynchronous job: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/vehicles/bulk/deactivate}, with {@code {"vehicle_ids": [...]}} as the
     * JSON body.
     *
     * @param vehicleIds the vehicle IDs; required
     * @return {@code null} when the API answers {@code 202 Accepted} with an empty body, as it documents; otherwise
     *     the job it sends
     * @throws IllegalArgumentException if {@code vehicleIds} is {@code null}; nothing is sent
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleJobResponse deactivateVehicles(String[] vehicleIds) throws APIException {
        return this.startBulkJob(VEHICLES_BULK_DEACTIVATE_EP, vehicleIds);
    }

    /**
     * Deletes vehicles, as an asynchronous job: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/vehicles/bulk/delete}, with {@code {"vehicle_ids": [...]}} as the JSON
     * body.
     *
     * @param vehicleIds the vehicle IDs; required
     * @return {@code null} when the API answers {@code 202 Accepted} with an empty body, as it documents; otherwise
     *     the job it sends
     * @throws IllegalArgumentException if {@code vehicleIds} is {@code null}; nothing is sent
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleJobResponse deleteVehicles(String[] vehicleIds) throws APIException {
        return this.startBulkJob(VEHICLES_BULK_DELETE_EP, vehicleIds);
    }

    /**
     * Restores deleted vehicles, as an asynchronous job: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/vehicles/bulk/restore}, with {@code {"vehicle_ids": [...]}} as the
     * JSON body.
     *
     * @param vehicleIds the vehicle IDs; required
     * @return {@code null} when the API answers {@code 202 Accepted} with an empty body, as it documents; otherwise
     *     the job it sends
     * @throws IllegalArgumentException if {@code vehicleIds} is {@code null}; nothing is sent
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public VehicleJobResponse restoreVehicles(String[] vehicleIds) throws APIException {
        return this.startBulkJob(VEHICLES_BULK_RESTORE_EP, vehicleIds);
    }

    /** Posts {@code {"vehicle_ids": [...]}} to a bulk endpoint, which starts an asynchronous job. */
    private VehicleJobResponse startBulkJob(String endpoint, String[] vehicleIds) throws APIException {
        require(vehicleIds, "vehicle_ids");
        VehicleIdsRequest request = new VehicleIdsRequest();
        request.setVehicleIds(vehicleIds);
        URIBuilder builder = v5Builder(endpoint);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), VehicleJobResponse.class);
    }

    /**
     * Gets the result of a vehicle background job: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/vehicles/job-tracker/result/{jobId}}. While the job runs, the API
     * answers {@code 409}, which throws.
     *
     * @param jobId the job ID
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
     * @param jobId the job ID
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
