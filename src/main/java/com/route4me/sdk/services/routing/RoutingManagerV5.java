package com.route4me.sdk.services.routing;

import com.google.gson.JsonElement;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.search.RouteSearchResult;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 * Lists, searches and exports routes through the V5 API ({@code https://wh.route4me.com/modules/api/v5.0/routes}
 * and {@code https://wh.route4me.com/modules/api/v5.0/route-export}), as the C# {@code RouteManagerV5} does. The API
 * key is sent as a bearer token. {@link RoutingManager} has the V4 route methods.
 */
public class RoutingManagerV5 extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String ROUTES_EP = V5_BASE + "/routes";
    private static final String ROUTES_LIST_EP = ROUTES_EP + "/list";
    private static final String ROUTES_DATATABLE_EP = ROUTES_EP + "/datatable";
    private static final String ROUTES_DATATABLE_CONFIG_EP = ROUTES_DATATABLE_EP + "/config";
    private static final String ROUTE_EXPORT_EP = V5_BASE + "/route-export";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     */
    public RoutingManagerV5(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
    public RoutingManagerV5(String apiKey, boolean disableRedirects) {
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
     * Lists routes, a page at a time, with filters and sorting: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/routes}, with the request as the JSON body.
     *
     * @param request the filters, sort order and page; {@code null} sends an empty body, {@code {}}
     * @return the page of routes, with the page links and counts
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteSearchResult getRoutes(RouteListRequest request) throws APIException {
        return this.postList(ROUTES_EP, request);
    }

    /**
     * Lists routes, a page at a time, with filters, sorting and grouping: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/routes/list}, with the request as the JSON body.
     *
     * @param request the filters, sort order, grouping, page and fields; {@code null} sends an empty body,
     *     {@code {}}
     * @return the page of routes, with the page links and counts
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteSearchResult getRoutesList(RouteListRequest request) throws APIException {
        return this.postList(ROUTES_LIST_EP, request);
    }

    /**
     * Lists routes as the rows of the route data table, with filters and sorting: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/routes/datatable}, with the request as the JSON body.
     * {@link #getRouteDataTableConfig()} lists the fields it can sort and filter by. No API specification
     * describes this endpoint, so the answer is returned as JSON.
     *
     * @param request the filters, sort order and page; {@code null} sends an empty body, {@code {}}
     * @return the answer of the API, as JSON
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public JsonElement getRouteDatatableWithElasticSearch(RouteListRequest request) throws APIException {
        URIBuilder builder = v5Builder(ROUTES_DATATABLE_EP);
        RouteListRequest body = request != null ? request : new RouteListRequest();
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(body), JsonElement.class);
    }

    /**
     * Gets the configuration of the route data table: the fields it can sort and filter by, and how it pages:
     * {@code GET https://wh.route4me.com/modules/api/v5.0/routes/datatable/config}.
     *
     * @return the configuration
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteDataTableConfigResponse getRouteDataTableConfig() throws APIException {
        URIBuilder builder = v5Builder(ROUTES_DATATABLE_CONFIG_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, RouteDataTableConfigResponse.class);
    }

    /**
     * Exports a route: {@code POST https://wh.route4me.com/modules/api/v5.0/route-export/{routeId}/export}, with the
     * request as the JSON body.
     *
     * @param routeId the route ID
     * @param request the format and what to include; {@code null} sends an empty body, {@code {}}, which exports in
     *     the default format
     * @return the exported route, as the text of the chosen format (CSV, JSON, XML or HTML)
     * @throws IllegalArgumentException if {@code routeId} is {@code null} or empty
     * @throws APIException if the request fails or the server answers with an error status
     */
    public String exportRoute(String routeId, RouteExportRequest request) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_EXPORT_EP, routeId, "export");
        RouteExportRequest body = request != null ? request : new RouteExportRequest();
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(body), String.class);
    }

    /** Posts a route list request, {@code {}} when it is {@code null}, and parses the page of routes. */
    private RouteSearchResult postList(String endpoint, RouteListRequest request) throws APIException {
        URIBuilder builder = v5Builder(endpoint);
        RouteListRequest body = request != null ? request : new RouteListRequest();
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(body), RouteSearchResult.class);
    }
}
