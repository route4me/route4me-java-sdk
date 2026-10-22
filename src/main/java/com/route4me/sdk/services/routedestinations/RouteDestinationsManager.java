package com.route4me.sdk.services.routedestinations;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 * Reads the destinations (stops) of routes, with their computed times and columns, through the V5 API
 * ({@code https://wh.route4me.com/modules/api/v5.0/route-destinations}).
 */
public class RouteDestinationsManager extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String ROUTE_DESTINATIONS_EP = V5_BASE + "/route-destinations";
    private static final String COLUMNS_EP = ROUTE_DESTINATIONS_EP + "/columns";
    private static final String SORTING_EP = ROUTE_DESTINATIONS_EP + "/sorting";
    private static final String LIST_EP = ROUTE_DESTINATIONS_EP + "/list";
    private static final String LIST_FIELDS_EP = ROUTE_DESTINATIONS_EP + "/list/fields";
    private static final String ORDER_EP = ROUTE_DESTINATIONS_EP + "/order";
    private static final String SEQUENCE_EP = ROUTE_DESTINATIONS_EP + "/sequence";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     */
    public RouteDestinationsManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
    public RouteDestinationsManager(String apiKey, boolean disableRedirects) {
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
     * Gets one route destination: {@code GET https://wh.route4me.com/modules/api/v5.0/route-destinations/{id}}.
     *
     * @param id the route destination ID
     * @return the destination
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteDestinationResource getDestination(String id) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_DESTINATIONS_EP, id);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, RouteDestinationResource.class);
    }

    /**
     * Gets the columns of the destination list: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/route-destinations/columns}.
     *
     * @param columnsConfiguratorKey the column configuration to read ({@code columns_configurator_key}), which the API
     *     requires
     * @return the columns, by category, and their order
     * @throws IllegalArgumentException if {@code columnsConfiguratorKey} is {@code null} or empty
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteDestinationColumnsResource getDestinationColumns(String columnsConfiguratorKey) throws APIException {
        requireValue(columnsConfiguratorKey, "columns_configurator_key");
        URIBuilder builder = v5Builder(COLUMNS_EP);
        builder.setParameter("columns_configurator_key", columnsConfiguratorKey);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, RouteDestinationColumnsResource.class);
    }

    /**
     * Changes the column order of the destination list: {@code PUT
     * https://wh.route4me.com/modules/api/v5.0/route-destinations/columns}, with the request as the JSON body.
     *
     * @param request the column configuration ({@code columns_configurator_key}) and the new order
     *     ({@code order}), which the API both requires
     * @return the saved order
     * @throws IllegalArgumentException if {@code request}, its configuration key or its order is {@code null}, or the
     *     key is empty
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public EditDestinationColumnsResponse editDestinationColumns(EditDestinationColumnsRequest request)
            throws APIException {
        if (request == null || request.getOrder() == null) {
            throw new IllegalArgumentException("The column order (order) is required");
        }
        requireValue(request.getColumnsConfiguratorKey(), "columns_configurator_key");
        URIBuilder builder = v5Builder(COLUMNS_EP);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(request),
                EditDestinationColumnsResponse.class);
    }

    /**
     * Lists route destinations, with filters, sorting and paging: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/route-destinations/list}, with the request as the JSON
     * body.
     *
     * @param request the filters, sort order, page and fields; the API requires {@code filters}
     * @return the matching destinations
     * @throws IllegalArgumentException if {@code request} or its filters are {@code null}
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteDestinationsListResponse getDestinationsList(GetDestinationsRequest request) throws APIException {
        if (request == null || request.getFilters() == null) {
            throw new IllegalArgumentException("The destination filters (filters) are required");
        }
        URIBuilder builder = v5Builder(LIST_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request),
                RouteDestinationsListResponse.class);
    }

    /**
     * Lists the fields the destination list can filter and sort by: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/route-destinations/list/fields}.
     *
     * @return the fields
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteDestinationFieldsResponse getDestinationFields() throws APIException {
        URIBuilder builder = v5Builder(LIST_FIELDS_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, RouteDestinationFieldsResponse.class);
    }

    /**
     * Finds the destination of a label code, for sorting packages: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/route-destinations/sorting?label_code=...}.
     *
     * @param labelCode the label code ({@code label_code}), which the API requires
     * @return the destination, with its route, driver and vehicle
     * @throws IllegalArgumentException if {@code labelCode} is {@code null} or empty
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public SortingFilteringResource getDestinationByLabelCode(String labelCode) throws APIException {
        requireValue(labelCode, "label_code");
        URIBuilder builder = v5Builder(SORTING_EP);
        builder.setParameter("label_code", labelCode);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, SortingFilteringResource.class);
    }

    /**
     * Lists the route destinations of an order: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/route-destinations/order/{orderUuid}}.
     *
     * @param orderUuid the order UUID
     * @return the destinations
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteDestinationsByOrderResponse getDestinationsByOrder(String orderUuid) throws APIException {
        URIBuilder builder = v5Builder(ORDER_EP, orderUuid);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, RouteDestinationsByOrderResponse.class);
    }

    /**
     * Computes the best visit order of a set of coordinates: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/route-destinations/sequence}, with the coordinates as the JSON
     * body.
     *
     * @param request the coordinates, which the API requires
     * @return the positions of the coordinates in visit order
     * @throws IllegalArgumentException if {@code request} or its coordinates are {@code null}
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public DestinationSequenceResponse getDestinationSequence(DestinationSequenceRequest request) throws APIException {
        if (request == null || request.getCoordinates() == null) {
            throw new IllegalArgumentException("The coordinates are required");
        }
        URIBuilder builder = v5Builder(SEQUENCE_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request),
                DestinationSequenceResponse.class);
    }

    /** Rejects a {@code null} or empty value of a query parameter that the API requires. */
    private static void requireValue(String value, String name) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("The query parameter " + name + " is required");
        }
    }
}
