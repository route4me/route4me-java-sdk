package com.route4me.sdk.services.facilities;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 * Manages the facilities of the account (depots, warehouses and other sites) through the
 * V5 API ({@code https://wh.route4me.com/modules/api/v5.0/facilities}).
 */
public class FacilityManager extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String FACILITIES_EP = V5_BASE + "/facilities";
    private static final String FACILITY_TYPES_EP = FACILITIES_EP + "/types";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     */
    public FacilityManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
    public FacilityManager(String apiKey, boolean disableRedirects) {
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
     * Gets one facility: {@code GET https://wh.route4me.com/modules/api/v5.0/facilities/{facilityId}}.
     *
     * @param facilityId the facility ID
     * @return the facility
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public FacilityResource getFacility(String facilityId) throws APIException {
        URIBuilder builder = v5Builder(FACILITIES_EP, facilityId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, FacilityResource.class);
    }

    /**
     * Lists facilities, with filters, one page at a time: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/facilities}. Every {@code null} argument is left out of
     * the query.
     *
     * @param page the page number, from 1 ({@code page})
     * @param perPage the number of facilities per page ({@code per_page})
     * @param status only facilities with this status: 1 active, 2 inactive, 3 in review ({@code status})
     * @param facilityTypeId only facilities of this type ({@code facility_type_id})
     * @param searchQuery text to search for ({@code search_query})
     * @return the page of facilities
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public FacilitiesPaginateResource getFacilities(Integer page, Integer perPage, Integer status,
            Integer facilityTypeId, String searchQuery) throws APIException {
        URIBuilder builder = v5Builder(FACILITIES_EP);
        if (page != null) {
            builder.setParameter("page", String.valueOf(page));
        }
        if (perPage != null) {
            builder.setParameter("per_page", String.valueOf(perPage));
        }
        if (status != null) {
            builder.setParameter("status", String.valueOf(status));
        }
        if (facilityTypeId != null) {
            builder.setParameter("facility_type_id", String.valueOf(facilityTypeId));
        }
        if (searchQuery != null) {
            builder.setParameter("search_query", searchQuery);
        }
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, FacilitiesPaginateResource.class);
    }

    /**
     * Creates a facility: {@code POST https://wh.route4me.com/modules/api/v5.0/facilities}, with the request as the
     * JSON body.
     *
     * @param request the facility
     * @return the created facility
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public FacilityResource createFacility(FacilityCreateRequest request) throws APIException {
        URIBuilder builder = v5Builder(FACILITIES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), FacilityResource.class);
    }

    /**
     * Updates a facility: {@code PUT https://wh.route4me.com/modules/api/v5.0/facilities/{facilityId}}, with the
     * request as the JSON body.
     *
     * @param facilityId the facility ID
     * @param request the new values of the facility
     * @return the updated facility
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public FacilityResource updateFacility(String facilityId, FacilityUpdateRequest request) throws APIException {
        URIBuilder builder = v5Builder(FACILITIES_EP, facilityId);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(request), FacilityResource.class);
    }

    /**
     * Deletes a facility: {@code DELETE https://wh.route4me.com/modules/api/v5.0/facilities/{facilityId}}.
     *
     * @param facilityId the facility ID
     * @return the deleted facility, in an array, as the API returns it
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public FacilityResource[] deleteFacility(String facilityId) throws APIException {
        URIBuilder builder = v5Builder(FACILITIES_EP, facilityId);
        return this.makeRequest(RequestMethod.DELETE, builder, "", FacilityResource[].class);
    }

    /**
     * Lists the facility types: {@code GET https://wh.route4me.com/modules/api/v5.0/facilities/types}.
     *
     * @return the facility types
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public FacilityTypeCollectionResource getFacilityTypes() throws APIException {
        URIBuilder builder = v5Builder(FACILITY_TYPES_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, FacilityTypeCollectionResource.class);
    }

    /**
     * Gets one facility type: {@code GET https://wh.route4me.com/modules/api/v5.0/facilities/types/{typeId}}.
     *
     * @param typeId the facility type ID
     * @return the facility type
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public FacilityTypeResource getFacilityType(int typeId) throws APIException {
        URIBuilder builder = v5Builder(FACILITY_TYPES_EP, typeId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, FacilityTypeResource.class);
    }
}
