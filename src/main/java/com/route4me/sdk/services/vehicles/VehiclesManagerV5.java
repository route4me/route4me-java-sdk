package com.route4me.sdk.services.vehicles;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

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

    public VehiclesManagerV5(String apiKey) {
        super(apiKey);
    }

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

    // --- Vehicle CRUD ---

    public VehicleV5[] getVehicles(Integer page, Integer perPage) throws APIException {
        URIBuilder builder = v5Builder(VEHICLES_EP);
        if (page != null) builder.setParameter("page", String.valueOf(page));
        if (perPage != null) builder.setParameter("per_page", String.valueOf(perPage));
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, VehicleV5[].class);
    }

    public VehicleV5 getVehicleById(String vehicleId) throws APIException {
        URIBuilder builder = v5Builder(VEHICLES_EP, vehicleId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, VehicleV5.class);
    }

    public VehicleV5 createVehicle(VehicleV5 vehicle) throws APIException {
        URIBuilder builder = v5Builder(VEHICLES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(vehicle), VehicleV5.class);
    }

    public VehicleV5 updateVehicle(String vehicleId, VehicleV5 vehicle) throws APIException {
        URIBuilder builder = v5Builder(VEHICLES_EP, vehicleId);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(vehicle), VehicleV5.class);
    }

    public VehicleV5 deleteVehicle(String vehicleId) throws APIException {
        URIBuilder builder = v5Builder(VEHICLES_EP, vehicleId);
        return this.makeRequest(RequestMethod.DELETE, builder, "", VehicleV5.class);
    }

    public VehicleV5[] searchVehicles(String search) throws APIException {
        URIBuilder builder = v5Builder(VEHICLES_SEARCH_EP);
        builder.setParameter("search", search);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, VehicleV5[].class);
    }

    public VehicleResponse getVehicleByLicensePlate(String licensePlate) throws APIException {
        URIBuilder builder = v5Builder(VEHICLES_LICENSE_EP);
        builder.setParameter("vehicle_license_plate", licensePlate);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, VehicleResponse.class);
    }

    public VehicleLocationResponse getVehicleLocations(String[] vehicleIds) throws APIException {
        URIBuilder builder = v5Builder(VEHICLES_LOCATION_EP);
        if (vehicleIds != null) {
            for (String id : vehicleIds) {
                builder.addParameter("ids[]", id);
            }
        }
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, VehicleLocationResponse.class);
    }

    // --- Bulk Operations ---

    public VehiclesResults activateVehicles(String[] vehicleIds) throws APIException {
        URIBuilder builder = v5Builder(VEHICLES_BULK_ACTIVATE_EP);
        VehicleIdsRequest request = new VehicleIdsRequest();
        request.setVehicleIds(vehicleIds);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), VehiclesResults.class);
    }

    public VehicleV5[] deactivateVehicles(String[] vehicleIds) throws APIException {
        URIBuilder builder = v5Builder(VEHICLES_BULK_DEACTIVATE_EP);
        VehicleIdsRequest request = new VehicleIdsRequest();
        request.setVehicleIds(vehicleIds);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), VehicleV5[].class);
    }

    public StatusResponse deleteVehicles(String[] vehicleIds) throws APIException {
        URIBuilder builder = v5Builder(VEHICLES_BULK_DELETE_EP);
        VehicleIdsRequest request = new VehicleIdsRequest();
        request.setVehicleIds(vehicleIds);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), StatusResponse.class);
    }

    public StatusResponse restoreVehicles(String[] vehicleIds) throws APIException {
        URIBuilder builder = v5Builder(VEHICLES_BULK_RESTORE_EP);
        VehicleIdsRequest request = new VehicleIdsRequest();
        request.setVehicleIds(vehicleIds);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), StatusResponse.class);
    }

    public StatusResponse getVehicleJobResult(String jobId) throws APIException {
        URIBuilder builder = v5Builder(VEHICLES_JOB_RESULT_EP, jobId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, StatusResponse.class);
    }

    public StatusResponse getVehicleJobStatus(String jobId) throws APIException {
        URIBuilder builder = v5Builder(VEHICLES_JOB_STATUS_EP, jobId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, StatusResponse.class);
    }

    // --- Vehicle Profiles ---

    public VehicleProfilesResponse getVehicleProfiles(Integer page, Integer perPage) throws APIException {
        URIBuilder builder = v5Builder(VEHICLE_PROFILES_EP);
        if (page != null) builder.setParameter("page", String.valueOf(page));
        if (perPage != null) builder.setParameter("per_page", String.valueOf(perPage));
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, VehicleProfilesResponse.class);
    }

    public VehicleProfile getVehicleProfileById(String profileId) throws APIException {
        URIBuilder builder = v5Builder(VEHICLE_PROFILES_EP, profileId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, VehicleProfile.class);
    }

    public VehicleProfile createVehicleProfile(VehicleProfile profile) throws APIException {
        URIBuilder builder = v5Builder(VEHICLE_PROFILES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(profile), VehicleProfile.class);
    }

    public VehicleProfile updateVehicleProfile(String profileId, VehicleProfile profile) throws APIException {
        URIBuilder builder = v5Builder(VEHICLE_PROFILES_EP, profileId);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(profile), VehicleProfile.class);
    }

    public VehicleProfile deleteVehicleProfile(String profileId) throws APIException {
        URIBuilder builder = v5Builder(VEHICLE_PROFILES_EP, profileId);
        return this.makeRequest(RequestMethod.DELETE, builder, "", VehicleProfile.class);
    }

    // --- Vehicle Capacity Profiles ---

    public VehicleCapacityProfilesResponse getVehicleCapacityProfiles(Integer page, Integer perPage) throws APIException {
        URIBuilder builder = v5Builder(VEHICLE_CAPACITY_PROFILES_EP);
        if (page != null) builder.setParameter("page", String.valueOf(page));
        if (perPage != null) builder.setParameter("per_page", String.valueOf(perPage));
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, VehicleCapacityProfilesResponse.class);
    }

    public VehicleCapacityProfileResponse getVehicleCapacityProfileById(String profileId) throws APIException {
        URIBuilder builder = v5Builder(VEHICLE_CAPACITY_PROFILES_EP, profileId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, VehicleCapacityProfileResponse.class);
    }

    public VehicleCapacityProfileResponse createVehicleCapacityProfile(VehicleCapacityProfile profile) throws APIException {
        URIBuilder builder = v5Builder(VEHICLE_CAPACITY_PROFILES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(profile), VehicleCapacityProfileResponse.class);
    }

    public VehicleCapacityProfileResponse updateVehicleCapacityProfile(String profileId, VehicleCapacityProfile profile) throws APIException {
        URIBuilder builder = v5Builder(VEHICLE_CAPACITY_PROFILES_EP, profileId);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(profile), VehicleCapacityProfileResponse.class);
    }

    public VehicleCapacityProfileResponse deleteVehicleCapacityProfile(String profileId) throws APIException {
        URIBuilder builder = v5Builder(VEHICLE_CAPACITY_PROFILES_EP, profileId);
        return this.makeRequest(RequestMethod.DELETE, builder, "", VehicleCapacityProfileResponse.class);
    }
}
