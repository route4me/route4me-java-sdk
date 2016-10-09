package com.route4me.sdk.services.routing;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.http.client.utils.URIBuilder;

import java.util.ArrayList;
import java.util.List;

public class RoutingManager extends Manager {
    public static final String ROUTE_EP = "/api.v4/route.php";
    public static final String ADDRESS_EP = "/api.v4/address.php";
    public static final String OPTIMIZATION_EP = "/api.v4/optimization_problem.php";
    public static final String DUPLICATE_ROUTE_EP = "/actions/duplicate_route.php";

    public RoutingManager(String apiKey) {
        super(apiKey, new Gson());
    }

    public DataObject runOptimization(OptimizationParameters parameters) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(OPTIMIZATION_EP);
        if (parameters.getProblemId() != null) {
            builder.setParameter("optimization_problem_id", parameters.getProblemId());
        }
        if (parameters.isReoptimize()) {
            builder.setParameter("reoptimize", "true");
        }
        if (parameters.isShowDirections()) {
            builder.setParameter("show_directions", "true");
        }
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(parameters), DataObject.class);
    }

    public DataObject getOptimization(OptimizationParameters parameters) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(OPTIMIZATION_EP);
        if (parameters.getProblemId() != null) {
            builder.setParameter("optimization_problem_id", parameters.getProblemId());
        }
        if (parameters.isReoptimize()) {
            builder.setParameter("reoptimize", "1");
        }
        if (parameters.isShowDirections()) {
            builder.setParameter("show_directions", "1");
        }
        return this.makeRequest(RequestMethod.GET, builder, "", DataObject.class);
    }

    public DataObject updateOptimization(OptimizationParameters parameters) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(OPTIMIZATION_EP);
        if (parameters.getProblemId() != null) {
            builder.setParameter("optimization_problem_id", parameters.getProblemId());
        }
        if (parameters.isReoptimize()) {
            builder.setParameter("reoptimize", "true");
        }
        if (parameters.isShowDirections()) {
            builder.setParameter("show_directions", "true");
        }
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(parameters), DataObject.class);
    }

    public List<DataObject> getOptimizations(int limit, int offset) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(OPTIMIZATION_EP);
        builder.setParameter("limit", Integer.toString(limit));
        builder.setParameter("offest", Integer.toString(offset));
        GetOptimizationsResponse resp = this.makeRequest(RequestMethod.GET, builder, "", GetOptimizationsResponse.class);
        return resp.getOptimizations();
    }

    //    public DataObject addAddressesToOptimizaion(String optimizationProblemID, List<Address> addresses) {
//        URIBuilder builder = Manager.defaultBuilder(APIEndpoints.)
//        try {
//            getParams().put("optimization_problem_id", optimizationProblemID);
//            getParams().put("reoptimize", "1");
//            DataObject dataObj = new DataObject();
//            dataObj.setAddresses(addresses);
//            String data = getGson().toJson(dataObj);
//            String params = Manager.transformParams(this.getParams());
//            Response response = Manager.makeURLConnectionRequest(RequestMethod.PUT, buildBaseURL(), params, data);
//
//            DataObject responseObject = DataObjectDeserializer.GSON_DESERIALIZER.fromJson(response.getResponseBody(), DataObject.class);
//            return responseObject;
//        } catch (UnsupportedEncodingException |InvalidRequestException ex) {
//            throw new APIException(ex);
//        }
//    }
    //Addresses
    public DataObject addAddressesToRoute(String routeId, List<Address> addresses) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ROUTE_EP);
        builder.setParameter("route_id", routeId);
        DataObject dataObj = new DataObject();
        dataObj.setAddresses(addresses);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(dataObj), DataObject.class);
    }

    public Address getAddress(String routeId, Number routeDestinationId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ADDRESS_EP);
        builder.setParameter("route_id", routeId);
        builder.setParameter("route_destination_id", routeDestinationId.toString());
        return this.makeRequest(RequestMethod.GET, builder, "", Address.class);
    }

    public DataObject moveAddresses(DataObject dataObj, String routeId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ROUTE_EP);
        builder.setParameter("route_id", routeId);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(dataObj), DataObject.class);
    }

    public void deleteAddress(String routeId, Number routeDestinationId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ROUTE_EP);
        builder.setParameter("route_id", routeId);
        builder.setParameter("route_destination_id", routeDestinationId.toString());
        this.makeRequest(RequestMethod.DELETE, builder, "", null);
    }

    public Route updateAddressAttribute(String routeId, Number routeDestinationId, Address dataObj) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ROUTE_EP);
        builder.setParameter("route_id", routeId);
        builder.setParameter("route_destination_id", routeDestinationId.toString());
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(dataObj), Route.class);
    }

    //routes
    public List<Route> getRoutes(RoutesRequest request) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ROUTE_EP);
        if (request.getLimit() != null) {
            builder.setParameter("limit", Integer.toString(request.getLimit()));
        }
        if (request.getOffset() != null) {
            builder.setParameter("offset", Integer.toString(request.getOffset()));
        }
        if (request.getQuery() != null) {
            builder.setParameter("query", request.getQuery());
        }
        if (request.isDeviceTrackingHistory()) {
            builder.setParameter("device_tracking_history", "true");
        }
        if (request.isDirections()) {
            builder.setParameter("directions", "true");
        }
        if (request.isOriginal()) {
            builder.setParameter("original", "true");
        }
        if (request.isRecomputeDirections()) {
            builder.setParameter("recompute_directions", "true");
        }
        if (request.isNotes()) {
            builder.setParameter("notes", "true");
        }
        return this.makeRequest(RequestMethod.GET, builder, "", new TypeToken<ArrayList<Route>>() {
        }.getType());
    }

    public Route getRoute(RoutesRequest request) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ROUTE_EP);
        if (request.getId() != null) {
            builder.setParameter("route_id", request.getId());
        }
        if (request.getLimit() != null) {
            builder.setParameter("limit", Integer.toString(request.getLimit()));
        }
        if (request.getOffset() != null) {
            builder.setParameter("offset", Integer.toString(request.getOffset()));
        }
        if (request.getQuery() != null) {
            builder.setParameter("query", request.getQuery());
        }
        if (request.isDeviceTrackingHistory()) {
            builder.setParameter("device_tracking_history", "true");
        }
        if (request.isDirections()) {
            builder.setParameter("directions", "true");
        }
        if (request.isOriginal()) {
            builder.setParameter("original", "true");
        }
        if (request.isRecomputeDirections()) {
            builder.setParameter("recompute_directions", "true");
        }
        if (request.isNotes()) {
            builder.setParameter("notes", "true");
        }
        return this.makeRequest(RequestMethod.GET, builder, "", Route.class);
    }

    public Route updateRoute(Route route) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ROUTE_EP);
        if (route.getId() != null) {
            builder.setParameter("route_id", route.getId());
        }
        if (route.getMemberId() != null) {
            builder.setParameter("member_id", Long.toString(route.getMemberId()));
        }
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(route), Route.class);
    }

    public List<Route> deleteRoutes(String... routeIds) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ROUTE_EP);
        StringBuilder conc = new StringBuilder();
        for (String s : routeIds) {
            conc.append(s).append(',');
        }
        return this.makeRequest(RequestMethod.DELETE, builder, this.gson.toJson(new DeleteRoutes(conc.toString())), new TypeToken<ArrayList<Route>>() {
        }.getType());
    }

    public DuplicateRouteResponse duplicateRoute(String routeID) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(DUPLICATE_ROUTE_EP);
        builder.setParameter("route_id", routeID);
        builder.setParameter("to", "none");
        return this.makeRequest(RequestMethod.GET, builder, "", DuplicateRouteResponse.class);
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
        private List<DataObject> optimizations;
    }
}
