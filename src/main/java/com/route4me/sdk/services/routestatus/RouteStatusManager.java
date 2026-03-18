package com.route4me.sdk.services.routestatus;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

public class RouteStatusManager extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String ROUTE_STATUS_EP = V5_BASE + "/route-status";
    private static final String ROUTE_STOP_STATUS_EP = V5_BASE + "/route-stop-status";

    public RouteStatusManager(String apiKey) {
        super(apiKey);
    }

    public RouteStatusManager(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    private static URIBuilder v5Builder(String endpoint) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        builder.setPath(endpoint);
        return builder;
    }

    public RouteStatusResponse getRouteStatus(String routeId) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_STATUS_EP + "/" + routeId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, RouteStatusResponse.class);
    }

    public RouteStatusResponse updateRouteStatus(String routeId, RouteStatusRequest request) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_STATUS_EP + "/" + routeId);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), RouteStatusResponse.class);
    }

    public RouteStatusHistoryResponse getRouteStatusHistory(String routeId, String orderBy, Long start, Long end) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_STATUS_EP + "/" + routeId + "/history");
        if (orderBy != null) {
            builder.setParameter("order_by", orderBy);
        }
        if (start != null) {
            builder.setParameter("start", String.valueOf(start));
        }
        if (end != null) {
            builder.setParameter("end", String.valueOf(end));
        }
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, RouteStatusHistoryResponse.class);
    }

    public RouteStatusResponse rollbackRouteStatus(String routeId) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_STATUS_EP + "/" + routeId + "/rollback");
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, RouteStatusResponse.class);
    }

    public UpdateRouteStatusAsPlannedResponse updateRouteStatusAsPlanned(String[] routeIds) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_STATUS_EP + "/planned");
        UpdateRouteStatusAsPlannedRequest request = new UpdateRouteStatusAsPlannedRequest();
        request.setRouteIds(routeIds);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), UpdateRouteStatusAsPlannedResponse.class);
    }

    public StatusResponse setRouteStopStatus(long[] destinationIds, String status) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_STOP_STATUS_EP);
        SetRouteStopStatusRequest request = new SetRouteStopStatusRequest();
        request.setDestinationIds(destinationIds);
        request.setStatus(status);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), StatusResponse.class);
    }
}
