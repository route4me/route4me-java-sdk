package com.route4me.sdk.services.routestatus;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 * Reads and changes the status of routes ({@code planned}, {@code started}, {@code paused}, {@code resumed},
 * {@code completed}, {@code canceled}) and of their stops, through the V5 API ({@code
 * https://wh.route4me.com/modules/api/v5.0/route-status} and
 * {@code https://wh.route4me.com/modules/api/v5.0/route-stop-status}).
 */
public class RouteStatusManager extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String ROUTE_STATUS_EP = V5_BASE + "/route-status";
    private static final String ROUTE_STOP_STATUS_EP = V5_BASE + "/route-stop-status";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     */
    public RouteStatusManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
    public RouteStatusManager(String apiKey, boolean disableRedirects) {
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
     * Gets the current status of a route: {@code GET https://wh.route4me.com/modules/api/v5.0/route-status/{routeId}}.
     *
     * @param routeId the route ID
     * @return the status, with the time of each change
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteStatusResponse getRouteStatus(String routeId) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_STATUS_EP, routeId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, RouteStatusResponse.class);
    }

    /**
     * Changes the status of a route: {@code POST https://wh.route4me.com/modules/api/v5.0/route-status/{routeId}}, with
     * the request as the JSON body. Statuses only move
     * forward (planned, then started or paused, then completed); repeating the current status changes nothing.
     *
     * @param routeId the route ID
     * @param request the new status, and where and when it changed
     * @return the status of the route after the change
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteStatusResponse updateRouteStatus(String routeId, RouteStatusRequest request) throws APIException {
        return this.updateRouteStatus(routeId, request, null);
    }

    /**
     * Changes the status of a route, from a device: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/route-status/{routeId}?device_id=...}, with the request as the JSON
     * body. See {@link #updateRouteStatus(String, RouteStatusRequest)}.
     *
     * @param routeId the route ID
     * @param request the new status, and where and when it changed
     * @param deviceId the device that reports the change ({@code device_id}); {@code null} leaves it out
     * @return the status of the route after the change
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteStatusResponse updateRouteStatus(String routeId, RouteStatusRequest request, String deviceId)
            throws APIException {
        URIBuilder builder = v5Builder(ROUTE_STATUS_EP, routeId);
        if (deviceId != null) {
            builder.setParameter("device_id", deviceId);
        }
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), RouteStatusResponse.class);
    }

    /**
     * Lists the status changes of a route: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/route-status/{routeId}/history}. Every {@code null} argument is left out
     * of the query.
     *
     * @param routeId the route ID
     * @param orderBy {@code asc} or {@code desc} ({@code order_by})
     * @param start only changes at or after this Unix timestamp, in seconds ({@code start})
     * @param end only changes at or before this Unix timestamp, in seconds ({@code end})
     * @return the status changes
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteStatusHistoryResponse getRouteStatusHistory(String routeId, String orderBy, Long start, Long end)
            throws APIException {
        URIBuilder builder = v5Builder(ROUTE_STATUS_EP, routeId, "history");
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

    /**
     * Rolls the status of a route back to the previous one: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/route-status/{routeId}/rollback}.
     *
     * @param routeId the route ID
     * @return the status of the route after the rollback
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteStatusResponse rollbackRouteStatus(String routeId) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_STATUS_EP, routeId, "rollback");
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, RouteStatusResponse.class);
    }

    /**
     * Marks routes as planned: {@code POST https://wh.route4me.com/modules/api/v5.0/route-status/planned}, with {@code
     * {"route_ids": [...]}} as the JSON body.
     *
     * @param routeIds the route IDs; at least one
     * @return the new status of each route
     * @throws IllegalArgumentException if no route ID is given: the API requires {@code route_ids}
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public UpdateRouteStatusAsPlannedResponse updateRouteStatusAsPlanned(String[] routeIds) throws APIException {
        if (routeIds == null || routeIds.length == 0) {
            throw new IllegalArgumentException("At least one route ID is required");
        }
        URIBuilder builder = v5Builder(ROUTE_STATUS_EP + "/planned");
        UpdateRouteStatusAsPlannedRequest request = new UpdateRouteStatusAsPlannedRequest();
        request.setRouteIds(routeIds);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request),
                UpdateRouteStatusAsPlannedResponse.class);
    }

    /**
     * Sets the status of route destinations: {@code POST https://wh.route4me.com/modules/api/v5.0/route-stop-status},
     * with the destination IDs and the
     * status as the JSON body.
     *
     * @param destinationIds the route destination IDs; at least one
     * @param status the status; one the account allows
     * @return whether the status was set
     * @throws IllegalArgumentException if no destination ID or no status is given: the API requires both
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public StatusResponse setRouteStopStatus(long[] destinationIds, String status) throws APIException {
        if (destinationIds == null || destinationIds.length == 0) {
            throw new IllegalArgumentException("At least one route destination ID is required");
        }
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("The status is required");
        }
        URIBuilder builder = v5Builder(ROUTE_STOP_STATUS_EP);
        SetRouteStopStatusRequest request = new SetRouteStopStatusRequest();
        request.setDestinationIds(destinationIds);
        request.setStatus(status);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), StatusResponse.class);
    }
}
