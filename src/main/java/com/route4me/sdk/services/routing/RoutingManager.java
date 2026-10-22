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

    public RoutingManager(String apiKey) {
        super(apiKey);
    }

    public RoutingManager(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    public RoutingManager(String apiKey, boolean disableRedirects, String callBackURL) {
        super(apiKey, disableRedirects, callBackURL);
    }

    public RoutingManager(String apiKey, String proxyHost, int proxyPort, String proxySchema) {
        super(apiKey, proxyHost, proxyPort, proxySchema);
    }

    public RoutingManager(String apiKey, String proxyHost, int proxyPort, String proxySchema,
            boolean disableRedirects) {
        super(apiKey, proxyHost, proxyPort, proxySchema, disableRedirects);
    }

    public DataObject runOptimization(OptimizationParameters parameters) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(OPTIMIZATION_EP);
        return this.makeJSONRequest(RequestMethod.POST, builder, parameters, DataObject.class);
    }

    public DataObject[] runOptimizationMulti(OptimizationParameters parameters) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(OPTIMIZATION_EP);
        return this.makeJSONRequest(RequestMethod.POST, builder, parameters, DataObject[].class);
    }

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

    public Map<String, Object> deleteOptimization(List<String> optimizationProblemIDs) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(OPTIMIZATION_EP);
        OptimizationProblemIDs dataObj = new OptimizationProblemIDs();
        dataObj.setOptimizationProblemIDs(optimizationProblemIDs);
        return this.makeRequest(RequestMethod.DELETE, builder, this.gson.toJson(dataObj),
                new TypeToken<Map<String, Object>>() {
                }.getType());
    }

    public DataObject addAddressesToRoute(String routeId, List<Address> addresses) throws APIException {
        return this.addAddressesToRoute(routeId, addresses, false);
    }

    public DataObject addAddressesToRoute(String routeId, List<Address> addresses, Boolean optimalPosition)
            throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ROUTE_EP);
        builder.setParameter("route_id", routeId);
        DataObject dataObj = new DataObject();
        dataObj.setAddresses(addresses);
        dataObj.setOptimalPosition(optimalPosition);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(dataObj), DataObject.class);
    }

    public Address getAddress(String routeId, Number routeDestinationId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ADDRESS_EP);
        builder.setParameter("route_id", routeId);
        builder.setParameter("route_destination_id", routeDestinationId.toString());
        return this.makeRequest(RequestMethod.GET, builder, "", Address.class);
    }

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

    public MovedAddressesResponse moveAddresses(Number routeDestinationId,
            String toRouteId) throws APIException {
        return moveAddresses(routeDestinationId, toRouteId, null);
    }

    public void deleteAddress(String routeId, Number routeDestinationId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ADDRESS_EP);
        builder.setParameter("route_id", routeId);
        builder.setParameter("route_destination_id", routeDestinationId.toString());
        this.makeRequest(RequestMethod.DELETE, builder, "", null);
    }

    public void deleteAddressFromOptimization(String optimizationProblemId, Number routeDestinationId)
            throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ADDRESS_EP);
        builder.setParameter("optimization_problem_id", optimizationProblemId);
        builder.setParameter("route_destination_id", routeDestinationId.toString());
        this.makeRequest(RequestMethod.DELETE, builder, "", null);
    }

    public Address updateAddressAttribute(String routeId, Number routeDestinationId, Address dataObj)
            throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ADDRESS_EP);
        builder.setParameter("route_id", routeId);
        builder.setParameter("route_destination_id", routeDestinationId.toString());
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(dataObj), Address.class);
    }

    public List<Route> getRoutesbyTimeDate(String startTimeDate, String endTimeDate) throws APIException {
        return this.getRoutesbyTimeDate(startTimeDate, endTimeDate, null, null);
    }

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

    public RouteSearchResult getRoutesbyDateTimeZone(String scheduledForStart, String scheduledForEnd, String timeZone)
            throws APIException {
        return this.getRoutesbyDateTimeZone(scheduledForStart, scheduledForEnd, timeZone, 1, 30);
    }

    public List<Route> getRoutes(RoutesRequest request) throws APIException {
        return this.makeJSONRequest(RequestMethod.GET, Manager.defaultBuilder(ROUTE_EP), request,
                new TypeToken<ArrayList<Route>>() {
                }.getType());
    }

    public Route getRoute(RoutesRequest request) throws APIException {
        return this.makeJSONRequest(RequestMethod.GET, Manager.defaultBuilder(ROUTE_EP), request, Route.class);
    }

    public Route updateRoute(Route route) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ROUTE_EP);
        String routeId = route.getId();
        builder.setParameter("route_id", routeId);
        return this.makeJSONRequest(RequestMethod.PUT, builder, route, Route.class);
    }

    public Route assignDriver(String routeId, String memberID) throws APIException {
        Parameters params = new Parameters();
        params.setMemberId(memberID);

        Route route = new Route();
        route.setId(routeId);
        route.setParameters(params);
        return updateRoute(route);
    }

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

    public DuplicateRouteResponse duplicateRoute(String routeID) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(DUPLICATE_ROUTE_EP);
        builder.setParameter("route_id", routeID);
        builder.setParameter("to", "none");
        return this.makeRequest(RequestMethod.GET, builder, "", DuplicateRouteResponse.class);
    }

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

    @Getter
    @ToString
    public static class DuplicateRouteResponse {

        @SerializedName("optimization_problem_id")
        private String problemId;
        @SerializedName("success")
        private boolean success;
    }

    @Getter
    @AllArgsConstructor
    private static class DeleteRoutes {

        @SerializedName("route_id")
        private String routeId;
    }

    @Getter
    private static class GetOptimizationsResponse {

        @SerializedName("optimizations")
        private List<DataObject> optimizations;
    }
}
