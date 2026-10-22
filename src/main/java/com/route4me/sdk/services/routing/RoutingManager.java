package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.queryconverter.QueryConverter;
import com.route4me.sdk.services.routing.search.RouteSearch;
import com.route4me.sdk.services.routing.search.RouteSearchFilter;
import com.route4me.sdk.services.routing.search.RouteSearchResult;
import com.route4me.sdk.utils.DateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static org.apache.http.Consts.UTF_8;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

/**
 * Runs optimizations and manages routes and their addresses through the V4 API ({@code https://api.route4me.com}), and
 * searches routes
 * through the V5.1 route API ({@code https://wh.route4me.com/modules/api/v5.1/routes}).
 */
public class RoutingManager extends Manager {

    private static final String ROUTE_EP = "/api.v4/route.php";
    private static final String ADDRESS_EP = "/api.v4/address.php";
    private static final String OPTIMIZATION_EP = "/api.v4/optimization_problem.php";
    private static final String DUPLICATE_ROUTE_EP = "/actions/duplicate_route.php";
    private static final String RENAME_ROUTE_EP = "/actions/route/rename_route.php";
    private static final String MOVE_ADDRESS = "/actions/route/move_route_destination.php";
    private static final String ROUTE_SEARCH_V5_1 = "/modules/api/v5.1/routes";

    private URIBuilder getRouteSearchURI(String endpoint) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        builder.setPath(endpoint);
        return builder;
    }

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token to the V5 API and as the {@code api_key} query
     *     parameter to the V4 API
     */
    public RoutingManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token to the V5 API and as the {@code api_key} query
     *     parameter to the V4 API
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
    public RoutingManager(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    /**
     * Creates a manager that also asks the API to call a URL when an optimization finishes.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token to the V5 API and as the {@code api_key} query
     *     parameter to the V4 API
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     * @param callBackURL the URL, sent as {@code optimized_callback_url} with every request
     */
    public RoutingManager(String apiKey, boolean disableRedirects, String callBackURL) {
        super(apiKey, disableRedirects, callBackURL);
    }

    /**
     * Creates a manager that sends its requests through a proxy.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token to the V5 API and as the {@code api_key} query
     *     parameter to the V4 API
     * @param proxyHost the proxy host
     * @param proxyPort the proxy port
     * @param proxySchema the proxy scheme, such as {@code http}
     */
    public RoutingManager(String apiKey, String proxyHost, int proxyPort, String proxySchema) {
        super(apiKey, proxyHost, proxyPort, proxySchema);
    }

    /**
     * Creates a manager that sends its requests through a proxy, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token to the V5 API and as the {@code api_key} query
     *     parameter to the V4 API
     * @param proxyHost the proxy host
     * @param proxyPort the proxy port
     * @param proxySchema the proxy scheme, such as {@code http}
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
    public RoutingManager(String apiKey, String proxyHost, int proxyPort, String proxySchema,
            boolean disableRedirects) {
        super(apiKey, proxyHost, proxyPort, proxySchema, disableRedirects);
    }

    /**
     * Runs an optimization: {@code POST https://api.route4me.com/api.v4/optimization_problem.php}, with the parameters
     * and addresses as the JSON body.
     *
     * @param parameters the route parameters and the addresses
     * @return the optimization, with its state and routes
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public DataObject runOptimization(OptimizationParameters parameters) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(OPTIMIZATION_EP);
        return this.makeJSONRequest(RequestMethod.POST, builder, parameters, DataObject.class);
    }

    /**
     * Runs an optimization whose answer is a list: {@code POST
     * https://api.route4me.com/api.v4/optimization_problem.php}, with the parameters and addresses as the JSON body.
     *
     * @param parameters the route parameters and the addresses
     * @return the optimizations of the answer
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public DataObject[] runOptimizationMulti(OptimizationParameters parameters) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(OPTIMIZATION_EP);
        return this.makeJSONRequest(RequestMethod.POST, builder, parameters, DataObject[].class);
    }

    /**
     * Gets an optimization: {@code GET
     * https://api.route4me.com/api.v4/optimization_problem.php?optimization_problem_id=...}; the other query fields of
     * the parameters are
     * sent too, and nothing goes in a body.
     *
     * @param parameters the optimization ID
     * @return the optimization, with its routes
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Optimization getOptimization(OptimizationParameters parameters) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(OPTIMIZATION_EP);
        return this.makeJSONRequest(RequestMethod.GET, builder, parameters, Optimization.class);
    }

    /**
     * Updates an optimization: {@code PUT /api.v4/optimization_problem.php}. The
     * optimization ID ({@code optimization_problem_id}) and the {@code reoptimize} and
     * {@code show_directions} flags go in the query; the parameters and addresses go in the
     * JSON body.
     *
     * @param parameters the optimization, with its problem ID set
     * @return the updated optimization
     * @throws APIException if the request fails
     */
    public DataObject updateOptimization(OptimizationParameters parameters) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(OPTIMIZATION_EP);
        return this.makeJSONRequest(RequestMethod.PUT, builder, parameters, parameters, DataObject.class);
    }

    /**
     * Re-optimizes an optimization:
     * {@code PUT /api.v4/optimization_problem.php?optimization_problem_id=...&reoptimize=1}.
     * Everything goes in the query; the request has no body.
     *
     * @param parameters the optimization ID and flags
     * @return the re-optimized optimization
     * @throws APIException           if the request fails
     * @throws IllegalAccessException if the parameters cannot be read
     */
    public Optimization reOptimizationByOptimizationID(OptimizationParameters parameters)
            throws APIException, IllegalAccessException {
        URIBuilder builder = Manager.defaultBuilder(OPTIMIZATION_EP);
        List<NameValuePair> params = QueryConverter.convertObjectToParameters(parameters);
        builder.addParameters(params);
        return this.makeRequest(RequestMethod.PUT, builder, (HttpEntity) null, Optimization.class);
    }

    /**
     * Re-optimizes an optimization with new data: {@code PUT https://api.route4me.com/api.v4/optimization_problem.php},
     * with the query fields of the parameters (such as
     * {@code optimization_problem_id} and {@code reoptimize}) in the query and the optimization as the JSON body.
     *
     * @param parameters the optimization ID and flags
     * @param optimization the optimization, with its new addresses
     * @return the re-optimized optimization
     * @throws IllegalAccessException if the parameters cannot be read
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Optimization reOptimization(OptimizationParameters parameters, DataObject optimization)
            throws APIException, IllegalAccessException {
        URIBuilder builder = Manager.defaultBuilder(OPTIMIZATION_EP);
        List<NameValuePair> params = QueryConverter.convertObjectToParameters(parameters);
        builder.addParameters(params);
        return this.makeJSONRequest(RequestMethod.PUT, builder, optimization, Optimization.class);
    }

    /**
     * Lists optimizations: {@code GET /api.v4/optimization_problem.php?limit=...&offset=...}.
     *
     * @param limit  the page size
     * @param offset the number of optimizations to skip
     * @return the optimizations, or {@code null} when the answer has none
     * @throws APIException if the request fails
     */
    public List<DataObject> getOptimizations(int limit, int offset) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(OPTIMIZATION_EP);
        builder.setParameter("limit", Integer.toString(limit));
        builder.setParameter("offset", Integer.toString(offset));
        GetOptimizationsResponse resp = this.makeRequest(RequestMethod.GET, builder, "",
                GetOptimizationsResponse.class);
        return resp == null ? null : resp.getOptimizations();
    }

    /**
     * Deletes optimizations: {@code DELETE https://api.route4me.com/api.v4/optimization_problem.php}, with {@code
     * {"optimization_problem_ids": [...]}} as the JSON body.
     *
     * @param optimizationProblemIDs the optimization IDs
     * @return the answer of the API, such as {@code {"status": true, "removed": 1}}
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Map<String, Object> deleteOptimization(List<String> optimizationProblemIDs) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(OPTIMIZATION_EP);
        OptimizationProblemIDs dataObj = new OptimizationProblemIDs();
        dataObj.setOptimizationProblemIDs(optimizationProblemIDs);
        return this.makeRequest(RequestMethod.DELETE, builder, this.gson.toJson(dataObj),
                new TypeToken<Map<String, Object>>() {
                }.getType());
    }

    /**
     * Adds addresses at the end of a route: {@code PUT https://api.route4me.com/api.v4/route.php?route_id=...}, with
     * the addresses as the JSON body.
     *
     * @param routeId the route ID
     * @param addresses the addresses
     * @return the route with its new addresses
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public DataObject addAddressesToRoute(String routeId, List<Address> addresses) throws APIException {
        return this.addAddressesToRoute(routeId, addresses, false);
    }

    /**
     * Adds addresses to a route: {@code PUT https://api.route4me.com/api.v4/route.php?route_id=...}, with the addresses
     * and {@code optimal_position} as the
     * JSON body.
     *
     * @param routeId the route ID
     * @param addresses the addresses
     * @param optimalPosition {@code true} to insert them where they fit best,
     *     rather than at the end
     * @return the route with its new addresses
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public DataObject addAddressesToRoute(String routeId, List<Address> addresses, Boolean optimalPosition)
            throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ROUTE_EP);
        builder.setParameter("route_id", routeId);
        DataObject dataObj = new DataObject();
        dataObj.setAddresses(addresses);
        dataObj.setOptimalPosition(optimalPosition);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(dataObj), DataObject.class);
    }

    /**
     * Gets one address of a route: {@code GET
     * https://api.route4me.com/api.v4/address.php?route_id=...&route_destination_id=...}.
     *
     * @param routeId the route ID
     * @param routeDestinationId the route destination ID
     * @return the address
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Address getAddress(String routeId, Number routeDestinationId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ADDRESS_EP);
        builder.setParameter("route_id", routeId);
        builder.setParameter("route_destination_id", routeDestinationId.toString());
        return this.makeRequest(RequestMethod.GET, builder, "", Address.class);
    }

    /**
     * Moves a destination to another route: {@code POST https://api.route4me.com/actions/route/move_route_destination.php},
     * with {@code to_route_id}, {@code route_destination_id} and {@code after_destination_id} as a form.
     *
     * @param routeDestinationId the destination to move
     * @param toRouteId the route to move it to
     * @param afterRouteDestinationId the destination of that route to put it after; {@code null} leaves
     *     {@code after_destination_id} out
     * @return whether it moved
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public MovedAddressesResponse moveAddresses(Number routeDestinationId, String toRouteId,
            Number afterRouteDestinationId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(MOVE_ADDRESS);
        List<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("to_route_id", toRouteId));
        body.add(new BasicNameValuePair("route_destination_id", routeDestinationId.toString()));
        if (afterRouteDestinationId != null) {
            body.add(new BasicNameValuePair("after_destination_id", afterRouteDestinationId.toString()));
        }
        return this.makeRequest(RequestMethod.POST, builder, body, MovedAddressesResponse.class);
    }

    /**
     * Moves a destination to another route, without saying where: see
     * {@link #moveAddresses(Number, String, Number)}.
     *
     * @param routeDestinationId the destination to move
     * @param toRouteId the route to move it to
     * @return whether it moved
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public MovedAddressesResponse moveAddresses(Number routeDestinationId,
            String toRouteId) throws APIException {
        return moveAddresses(routeDestinationId, toRouteId, null);
    }

    /**
     * Removes an address from a route: {@code DELETE
     * https://api.route4me.com/api.v4/address.php?route_id=...&route_destination_id=...}.
     *
     * @param routeId the route ID
     * @param routeDestinationId the route destination ID
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public void deleteAddress(String routeId, Number routeDestinationId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ADDRESS_EP);
        builder.setParameter("route_id", routeId);
        builder.setParameter("route_destination_id", routeDestinationId.toString());
        this.makeRequest(RequestMethod.DELETE, builder, "", null);
    }

    /**
     * Removes an address from an optimization:
     * {@code DELETE https://api.route4me.com/api.v4/address.php?optimization_problem_id=...&route_destination_id=...}.
     *
     * @param optimizationProblemId the optimization ID
     * @param routeDestinationId the route destination ID
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public void deleteAddressFromOptimization(String optimizationProblemId, Number routeDestinationId)
            throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ADDRESS_EP);
        builder.setParameter("optimization_problem_id", optimizationProblemId);
        builder.setParameter("route_destination_id", routeDestinationId.toString());
        this.makeRequest(RequestMethod.DELETE, builder, "", null);
    }

    /**
     * Updates an address of a route: {@code PUT
     * https://api.route4me.com/api.v4/address.php?route_id=...&route_destination_id=...}, with the address as the
     * JSON body.
     *
     * @param routeId the route ID
     * @param routeDestinationId the route destination ID
     * @param dataObj the new values
     * @return the updated address
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Address updateAddressAttribute(String routeId, Number routeDestinationId, Address dataObj)
            throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ADDRESS_EP);
        builder.setParameter("route_id", routeId);
        builder.setParameter("route_destination_id", routeDestinationId.toString());
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(dataObj), Address.class);
    }

    /**
     * Lists the routes of a period: {@code GET https://api.route4me.com/api.v4/route.php?start_date=...&end_date=...}.
     *
     * @param startTimeDate the start of the period, a local date and time such as {@code
     *     2026-10-22T08:00:00:000+02:00}, sent as its UTC date
     * @param endTimeDate the end of the period, in the same format
     * @return the routes
     * @throws java.time.format.DateTimeParseException if a date is not in that format
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public List<Route> getRoutesbyTimeDate(String startTimeDate, String endTimeDate) throws APIException {
        return this.getRoutesbyTimeDate(startTimeDate, endTimeDate, null, null);
    }

    /**
     * Lists the routes of a period, one page at a time: {@code GET
     * https://api.route4me.com/api.v4/route.php?start_date=...&end_date=...&limit=...&offset=...}.
     *
     * @param startTimeDate the start of the period, a local date and time such as {@code
     *     2026-10-22T08:00:00:000+02:00}, sent as its UTC date
     * @param endTimeDate the end of the period, in the same format
     * @param limit the maximum number of routes; {@code null} leaves {@code limit} out
     * @param offset the number of routes to skip; {@code null} leaves {@code offset} out
     * @return the routes
     * @throws java.time.format.DateTimeParseException if a date is not in that format
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public List<Route> getRoutesbyTimeDate(String startTimeDate, String endTimeDate, Integer limit, Integer offset)
            throws APIException {
        RoutesRequest request = new RoutesRequest();
        if (limit != null) {
            request.setLimit(limit);
        }
        if (offset != null) {
            request.setOffset(offset);
        }

        request.setStartDate(DateTimeUtils.localDateTimeToUTCParser(startTimeDate));
        request.setEndDate(DateTimeUtils.localDateTimeToUTCParser(endTimeDate));

        return this.makeJSONRequest(RequestMethod.GET, Manager.defaultBuilder(ROUTE_EP), request,
                new TypeToken<ArrayList<Route>>() {
                }.getType());
    }

    /**
     * Searches the routes scheduled in a period, newest first:
     * {@code POST https://wh.route4me.com/modules/api/v5.1/routes}, with the search as the JSON body.
     *
     * @param scheduledForStart the first date, as {@code YYYY-MM-DD}
     * @param scheduledForEnd the last date, as {@code YYYY-MM-DD}
     * @param timeZone the time zone of the dates, such as {@code America/New_York}
     * @param page the page number, from 1
     * @param perPage the number of routes per page
     * @return the page of routes
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteSearchResult getRoutesbyDateTimeZone(String scheduledForStart, String scheduledForEnd, String timeZone,
            Integer page, Integer perPage) throws APIException {

        URIBuilder builder = this.getRouteSearchURI(ROUTE_SEARCH_V5_1);

        RouteSearch request = new RouteSearch();
        request.setTimeZone(timeZone);
        request.setPage(page);
        request.setPerPage(perPage);
        request.setOrderBy(Arrays.asList(Arrays.asList("route_created_unix", "desc")));
        RouteSearchFilter filters = new RouteSearchFilter(Arrays.asList(scheduledForStart, scheduledForEnd));
        request.setFilters(filters);
        return this.makeJSONRequest(RequestMethod.POST, builder, request, RouteSearchResult.class);
    }

    /**
     * Searches the routes scheduled in a period, newest first, 30 at a time: the first page of
     * {@link #getRoutesbyDateTimeZone(String, String, String, Integer, Integer)}.
     *
     * @param scheduledForStart the first date, as {@code YYYY-MM-DD}
     * @param scheduledForEnd the last date, as {@code YYYY-MM-DD}
     * @param timeZone the time zone of the dates, such as {@code America/New_York}
     * @return the first page of routes
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteSearchResult getRoutesbyDateTimeZone(String scheduledForStart, String scheduledForEnd, String timeZone)
            throws APIException {
        return this.getRoutesbyDateTimeZone(scheduledForStart, scheduledForEnd, timeZone, 1, 30);
    }

    /**
     * Lists routes: {@code GET https://api.route4me.com/api.v4/route.php}, with the request as the query.
     *
     * @param request the filters and paging
     * @return the routes
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public List<Route> getRoutes(RoutesRequest request) throws APIException {
        return this.makeJSONRequest(RequestMethod.GET, Manager.defaultBuilder(ROUTE_EP), request,
                new TypeToken<ArrayList<Route>>() {
                }.getType());
    }

    /**
     * Gets one route: {@code GET https://api.route4me.com/api.v4/route.php?route_id=...}, with the request as the
     * query.
     *
     * @param request the route ID and what to include
     * @return the route
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Route getRoute(RoutesRequest request) throws APIException {
        return this.makeJSONRequest(RequestMethod.GET, Manager.defaultBuilder(ROUTE_EP), request, Route.class);
    }

    /**
     * Updates a route: {@code PUT https://api.route4me.com/api.v4/route.php?route_id=...}, with the route as the JSON
     * body.
     *
     * @param route the route, with its ID
     * @return the updated route
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Route updateRoute(Route route) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ROUTE_EP);
        String routeId = route.getId();
        builder.setParameter("route_id", routeId);
        return this.makeJSONRequest(RequestMethod.PUT, builder, route, Route.class);
    }

    /**
     * Assigns a driver to a route: {@link #updateRoute(Route)} with only {@code parameters.member_id}.
     *
     * @param routeId the route ID
     * @param memberID the member ID of the driver
     * @return the updated route
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Route assignDriver(String routeId, String memberID) throws APIException {
        Parameters params = new Parameters();
        params.setMemberId(memberID);

        Route route = new Route();
        route.setId(routeId);
        route.setParameters(params);
        return updateRoute(route);
    }

    /**
     * Assigns a vehicle to a route: {@link #updateRoute(Route)} with only {@code parameters.vehicle_id}.
     *
     * @param routeId the route ID
     * @param vehicleId the vehicle ID
     * @return the updated route
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Route assignVehicle(String routeId, String vehicleId) throws APIException {
        Parameters params = new Parameters();
        params.setVehicleId(vehicleId);

        Route route = new Route();
        route.setId(routeId);
        route.setParameters(params);
        return updateRoute(route);
    }

    /**
     * Deletes routes: {@code DELETE /api.v4/route.php?route_id=ID1,ID2,...}.
     *
     * @param routeIds the IDs of the routes to delete; at least one
     * @return the deletion result
     * @throws IllegalArgumentException if no route ID is given, or one is {@code null} or empty
     * @throws APIException             if the request fails
     */
    public RouteDeletedResponse deleteRoutes(String... routeIds) throws APIException {
        if (routeIds == null || routeIds.length == 0) {
            throw new IllegalArgumentException("At least one route ID is required");
        }
        for (String routeId : routeIds) {
            if (routeId == null || routeId.isEmpty()) {
                throw new IllegalArgumentException("A route ID must not be null or empty");
            }
        }
        URIBuilder builder = Manager.defaultBuilder(ROUTE_EP);
        builder.addParameter("route_id", String.join(",", routeIds));

        return this.makeRequest(RequestMethod.DELETE, builder, "", RouteDeletedResponse.class);
    }

    /**
     * Copies a route: {@code GET https://api.route4me.com/actions/duplicate_route.php?route_id=...&to=none}.
     *
     * @param routeID the route ID
     * @return the optimization of the copy
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public DuplicateRouteResponse duplicateRoute(String routeID) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(DUPLICATE_ROUTE_EP);
        builder.setParameter("route_id", routeID);
        builder.setParameter("to", "none");
        return this.makeRequest(RequestMethod.GET, builder, "", DuplicateRouteResponse.class);
    }

    /**
     * Renames a route: {@code POST https://api.route4me.com/actions/route/rename_route.php?format=json}, with {@code
     * route_name} and
     * {@code route_id} as a form.
     *
     * @param routeName the new name
     * @param routeID the route ID
     * @return whether it was renamed
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteRenamedStatus renameRoute(String routeName, String routeID) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(RENAME_ROUTE_EP);
        builder.setParameter("format", "json");

        List<NameValuePair> data = new ArrayList<>();
        data.add(new BasicNameValuePair("route_name", routeName));
        data.add(new BasicNameValuePair("route_id", routeID));

        UrlEncodedFormEntity body = null;
        body = new UrlEncodedFormEntity(data, UTF_8);

        return this.makeRequest(RequestMethod.POST, builder, body, RouteRenamedStatus.class);
    }

    /** The answer of {@link #duplicateRoute(String)}. */
    @Getter
    @ToString
    public static class DuplicateRouteResponse {

        /** The ID of the optimization of the new route ({@code optimization_problem_id}). */
        @SerializedName("optimization_problem_id")
        private String problemId;
        /** Whether the route was duplicated ({@code success}). */
        @SerializedName("success")
        private boolean success;
    }

    @Getter
    @AllArgsConstructor
    private static class DeleteRoutes {

        /** The route IDs, separated by commas ({@code route_id}). */
        @SerializedName("route_id")
        private String routeId;
    }

    @Getter
    private static class GetOptimizationsResponse {

        /** The optimizations ({@code optimizations}). */
        @SerializedName("optimizations")
        private List<DataObject> optimizations;
    }
}
