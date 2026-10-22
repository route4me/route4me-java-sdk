package com.route4me.sdk.services.facilities;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

public class FacilityManager extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String FACILITIES_EP = V5_BASE + "/facilities";
    private static final String FACILITY_TYPES_EP = FACILITIES_EP + "/types";

    public FacilityManager(String apiKey) {
        super(apiKey);
    }

    public FacilityManager(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    private static URIBuilder v5Builder(String endpoint) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        builder.setPath(endpoint);
        return builder;
    }

    public FacilityResource getFacility(String facilityId) throws APIException {
        URIBuilder builder = v5Builder(FACILITIES_EP + "/" + facilityId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, FacilityResource.class);
    }

    public FacilitiesPaginateResource getFacilities(Integer page, Integer perPage, Integer status, Integer facilityTypeId, String searchQuery) throws APIException {
        URIBuilder builder = v5Builder(FACILITIES_EP);
        if (page != null) builder.setParameter("page", String.valueOf(page));
        if (perPage != null) builder.setParameter("per_page", String.valueOf(perPage));
        if (status != null) builder.setParameter("status", String.valueOf(status));
        if (facilityTypeId != null) builder.setParameter("facility_type_id", String.valueOf(facilityTypeId));
        if (searchQuery != null) builder.setParameter("search_query", searchQuery);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, FacilitiesPaginateResource.class);
    }

    public FacilityResource createFacility(FacilityCreateRequest request) throws APIException {
        URIBuilder builder = v5Builder(FACILITIES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), FacilityResource.class);
    }

    public FacilityResource updateFacility(String facilityId, FacilityUpdateRequest request) throws APIException {
        URIBuilder builder = v5Builder(FACILITIES_EP + "/" + facilityId);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(request), FacilityResource.class);
    }

    public FacilityResource[] deleteFacility(String facilityId) throws APIException {
        URIBuilder builder = v5Builder(FACILITIES_EP + "/" + facilityId);
        return this.makeRequest(RequestMethod.DELETE, builder, "", FacilityResource[].class);
    }

    public FacilityTypeCollectionResource getFacilityTypes() throws APIException {
        URIBuilder builder = v5Builder(FACILITY_TYPES_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, FacilityTypeCollectionResource.class);
    }

    public FacilityTypeResource getFacilityType(int typeId) throws APIException {
        URIBuilder builder = v5Builder(FACILITY_TYPES_EP + "/" + typeId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, FacilityTypeResource.class);
    }
}
