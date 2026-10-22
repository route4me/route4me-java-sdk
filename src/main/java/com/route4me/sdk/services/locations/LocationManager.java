package com.route4me.sdk.services.locations;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.queryconverter.QueryConverter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 * Reads the locations of the address book (its contacts with coordinates), grouped, clustered or by territory, and
 * manages the location types, through the V5 API ({@code
 * https://wh.route4me.com/modules/api/v5.0/address-book/locations}).
 */
public class LocationManager extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0/address-book";
    private static final String LOCATIONS_LIST_COMBINED_EP = V5_BASE + "/locations/list/combined";
    private static final String LOCATIONS_LIST_EP = V5_BASE + "/locations/list";
    private static final String LOCATIONS_CLUSTERING_EP = V5_BASE + "/locations/clustering";
    private static final String LOCATIONS_HEATMAP_EP = V5_BASE + "/locations/heatmap";
    private static final String LOCATIONS_TERRITORIES_EP = V5_BASE + "/locations/territories";
    private static final String LOCATIONS_EXPORT_COLUMNS_EP = V5_BASE + "/locations/export/columns";
    private static final String LOCATIONS_EXPORT_EP = V5_BASE + "/locations/export";
    private static final String LOCATION_TYPES_EP = V5_BASE + "/locations/types";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     */
    public LocationManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
    public LocationManager(String apiKey, boolean disableRedirects) {
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
     * Lists locations, with their column configuration: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/address-book/locations/list/combined}, with the request as the JSON
     * body.
     *
     * @param request the filters, sort order, page and fields
     * @return the page of locations and the columns
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public LocationCombinedResource getLocationsCombined(LocationCombinedRequest request) throws APIException {
        URIBuilder builder = v5Builder(LOCATIONS_LIST_COMBINED_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), LocationCombinedResource.class);
    }

    /**
     * Lists locations: {@code POST https://wh.route4me.com/modules/api/v5.0/address-book/locations/list}, with the
     * request as the JSON body.
     *
     * @param request the filters, sort order, page and fields
     * @return the page of locations
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public LocationListResource getLocationsList(LocationCombinedRequest request) throws APIException {
        URIBuilder builder = v5Builder(LOCATIONS_LIST_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), LocationListResource.class);
    }

    /**
     * Groups locations in clusters for a map zoom level: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/address-book/locations/clustering}, with the request as the JSON
     * body.
     *
     * @param request the filters and the zoom level
     * @return the clusters, and the locations that are not in one
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public LocationClusteringResource getLocationsClustering(LocationClusteringRequest request) throws APIException {
        URIBuilder builder = v5Builder(LOCATIONS_CLUSTERING_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request),
                LocationClusteringResource.class);
    }

    /**
     * Gets a heat map of the locations: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/address-book/locations/heatmap}, with the request as the JSON body.
     *
     * @param request the filters and the zoom level
     * @return the heat map
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public LocationHeatmapCollection getLocationsHeatmap(LocationHeatmapRequest request) throws APIException {
        URIBuilder builder = v5Builder(LOCATIONS_HEATMAP_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request),
                LocationHeatmapCollection.class);
    }

    /**
     * Lists territories with the number of locations in each: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/address-book/locations/territories}, with the request as the
     * JSON body.
     *
     * @param request the location filters and the territory page
     * @return the page of territories
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public LocationTerritoriesCollection getLocationsTerritories(LocationTerritoriesRequest request)
            throws APIException {
        URIBuilder builder = v5Builder(LOCATIONS_TERRITORIES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request),
                LocationTerritoriesCollection.class);
    }

    /**
     * Lists the columns a location export can have: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/address-book/locations/export/columns}, with the request as the JSON
     * body.
     *
     * @param request the location filters
     * @return the columns
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public LocationExportColumnsResource[] getLocationsExportColumns(LocationCombinedRequest request)
            throws APIException {
        URIBuilder builder = v5Builder(LOCATIONS_EXPORT_COLUMNS_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request),
                LocationExportColumnsResource[].class);
    }

    /**
     * Starts an export of locations to a file: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/address-book/locations/export}, with the request as the JSON body.
     * The API builds the file in a background job and answers {@code 202 Accepted} with no body; the
     * {@code Location} header of that answer, the link to the export status, is not exposed by this SDK.
     *
     * @param request the locations, the columns, the file name and the format
     * @return the status in the answer, or {@code null} when the answer has no body (the job was queued)
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public StatusResponse exportLocations(LocationExportRequest request) throws APIException {
        URIBuilder builder = v5Builder(LOCATIONS_EXPORT_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), StatusResponse.class);
    }

    /**
     * Lists the location types, one page at a time: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/address-book/locations/types}. Every {@code null} argument is left out
     * of the query. Same as {@link #getLocationTypes(LocationTypeFilters, Integer, Integer)} without filters.
     *
     * @param page the page number, from 1 ({@code page})
     * @param perPage the number of types per page ({@code per_page})
     * @return the page of location types
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public LocationTypeCollection getLocationTypes(Integer page, Integer perPage) throws APIException {
        return this.getLocationTypes(null, page, perPage);
    }

    /**
     * Lists the location types that match filters, one page at a time: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/address-book/locations/types}. The spec describes the filters and the
     * page as a body; since a GET has no body, they are sent in the query, the filters as
     * {@code filters[is_parent_type]=1}. Every {@code null} argument or filter is left out.
     *
     * @param filters the filters, or {@code null} for all the types
     * @param page the page number, from 1 ({@code page})
     * @param perPage the number of types per page ({@code per_page})
     * @return the page of location types
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public LocationTypeCollection getLocationTypes(LocationTypeFilters filters, Integer page, Integer perPage)
            throws APIException {
        URIBuilder builder = v5Builder(LOCATION_TYPES_EP);
        if (filters != null) {
            Map<String, Object> values = new LinkedHashMap<>();
            values.put("is_parent_type", filters.getIsParentType());
            values.put("is_child_type", filters.getIsChildType());
            values.put("only_custom_types", filters.getOnlyCustomTypes());
            values.put("search_query", filters.getSearchQuery());
            builder.addParameters(QueryConverter.toBracketedParameters("filters", values));
        }
        if (page != null) {
            builder.setParameter("page", String.valueOf(page));
        }
        if (perPage != null) {
            builder.setParameter("per_page", String.valueOf(perPage));
        }
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, LocationTypeCollection.class);
    }

    /**
     * Gets one location type: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/address-book/locations/types/{locationTypeId}}.
     *
     * @param locationTypeId the location type ID
     * @return the location type
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if {@code locationTypeId} is {@code null} or empty; nothing is sent
     */
    public LocationTypeResource getLocationTypeById(String locationTypeId) throws APIException {
        URIBuilder builder = v5Builder(LOCATION_TYPES_EP, locationTypeId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, LocationTypeResource.class);
    }

    /**
     * Creates a location type: {@code POST https://wh.route4me.com/modules/api/v5.0/address-book/locations/types}, with
     * the request as the JSON body.
     *
     * @param request the location type; {@code name}, {@code is_parent_type} and {@code is_child_type} are required
     * @return the created location type
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if a required field is missing; nothing is sent
     */
    public LocationTypeResource createLocationType(StoreLocationTypeRequest request) throws APIException {
        checkLocationType(request);
        URIBuilder builder = v5Builder(LOCATION_TYPES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), LocationTypeResource.class);
    }

    /**
     * Updates a location type: {@code PUT
     * https://wh.route4me.com/modules/api/v5.0/address-book/locations/types/{locationTypeId}}, with the request as the
     * JSON body.
     *
     * @param locationTypeId the location type ID
     * @param request the new values; {@code name}, {@code is_parent_type} and {@code is_child_type} are required
     * @return the updated location type
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if {@code locationTypeId} is {@code null} or empty, or a required field is
     *     missing; nothing is sent
     */
    public LocationTypeResource updateLocationType(String locationTypeId, StoreLocationTypeRequest request)
            throws APIException {
        URIBuilder builder = v5Builder(LOCATION_TYPES_EP, locationTypeId);
        checkLocationType(request);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(request), LocationTypeResource.class);
    }

    /**
     * Deletes a location type: {@code DELETE
     * https://wh.route4me.com/modules/api/v5.0/address-book/locations/types/{locationTypeId}}.
     *
     * @param locationTypeId the location type ID
     * @return whether it was deleted
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if {@code locationTypeId} is {@code null} or empty; nothing is sent
     */
    public DeleteLocationTypeResponse deleteLocationType(String locationTypeId) throws APIException {
        URIBuilder builder = v5Builder(LOCATION_TYPES_EP, locationTypeId);
        return this.makeRequest(RequestMethod.DELETE, builder, "", DeleteLocationTypeResponse.class);
    }

    /** Checks the fields the spec requires in a location type, before any request is sent. */
    private static void checkLocationType(StoreLocationTypeRequest request) {
        if (request == null || request.getName() == null || request.getName().isEmpty()) {
            throw new IllegalArgumentException("name is required");
        }
        if (request.getIsParentType() == null) {
            throw new IllegalArgumentException("is_parent_type is required");
        }
        if (request.getIsChildType() == null) {
            throw new IllegalArgumentException("is_child_type is required");
        }
    }
}
