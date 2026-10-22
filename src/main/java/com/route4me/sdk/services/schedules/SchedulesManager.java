package com.route4me.sdk.services.schedules;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 * Manages recurring routes through the V5 API ({@code https://wh.route4me.com/modules/api/v5.0/recurring-routes}): the
 * schedules, the route schedules that link a route
 * to a schedule, master routes, and the routes a schedule created. The account needs the recurring routes module;
 * without it the API answers 403.
 */
public class SchedulesManager extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String SCHEDULES_EP = V5_BASE + "/recurring-routes/schedules";
    private static final String SCHEDULES_PAGINATION_EP = V5_BASE + "/recurring-routes/schedules/pagination";
    private static final String ROUTE_SCHEDULES_EP = V5_BASE + "/recurring-routes/route-schedules";
    private static final String ROUTE_SCHEDULES_PAGINATION_EP = V5_BASE
            + "/recurring-routes/route-schedules/pagination";
    private static final String REPLACE_ROUTE_SCHEDULES_EP = V5_BASE + "/recurring-routes/route-schedules/replace";
    private static final String SCHEDULED_ROUTES_IS_COPY_EP = V5_BASE + "/recurring-routes/scheduled-routes/is-copy";
    private static final String SCHEDULED_ROUTES_GET_COPIES_EP = V5_BASE
            + "/recurring-routes/scheduled-routes/get-copies";
    private static final String MASTER_ROUTES_EP = V5_BASE + "/recurring-routes/master-routes";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     */
    public SchedulesManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
    public SchedulesManager(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    private static URIBuilder v5Builder(String endpoint, Object... segments) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        Manager.withPathSegments(builder, endpoint, segments);
        return builder;
    }

    // --- Schedules ---

    /**
     * Lists the schedules: {@code GET https://wh.route4me.com/modules/api/v5.0/recurring-routes/schedules}.
     *
     * @return the schedules
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public ScheduleList getSchedules() throws APIException {
        URIBuilder builder = v5Builder(SCHEDULES_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, ScheduleList.class);
    }

    /**
     * Lists the schedules, one page at a time: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/recurring-routes/schedules/pagination?with_pagination=1}.
     *
     * @param page the page number, from 1 ({@code page})
     * @param perPage the number of items per page ({@code per_page})
     * @return the page of schedules, with the page links
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public ScheduleListWithPagination getSchedulesWithPagination(int page, int perPage) throws APIException {
        URIBuilder builder = v5Builder(SCHEDULES_PAGINATION_EP);
        builder.setParameter("page", String.valueOf(page));
        builder.setParameter("per_page", String.valueOf(perPage));
        builder.setParameter("with_pagination", "1");
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, ScheduleListWithPagination.class);
    }

    /**
     * Creates a schedule: {@code POST https://wh.route4me.com/modules/api/v5.0/recurring-routes/schedules}, with the
     * schedule as the JSON body.
     *
     * @param schedule the schedule
     * @return the created schedule
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public ScheduleList createSchedule(Schedule schedule) throws APIException {
        URIBuilder builder = v5Builder(SCHEDULES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(schedule), ScheduleList.class);
    }

    /**
     * Gets one schedule: {@code GET https://wh.route4me.com/modules/api/v5.0/recurring-routes/schedules/{scheduleUid}}.
     *
     * @param scheduleUid the schedule ID
     * @return the schedule
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public ScheduleList getSchedule(String scheduleUid) throws APIException {
        URIBuilder builder = v5Builder(SCHEDULES_EP, scheduleUid);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, ScheduleList.class);
    }

    /**
     * Updates a schedule: {@code PUT
     * https://wh.route4me.com/modules/api/v5.0/recurring-routes/schedules/{scheduleUid}}, with the schedule as the JSON
     * body.
     *
     * @param scheduleUid the schedule ID
     * @param schedule the new values of the schedule
     * @return the updated schedule
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public ScheduleList updateSchedule(String scheduleUid, Schedule schedule) throws APIException {
        URIBuilder builder = v5Builder(SCHEDULES_EP, scheduleUid);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(schedule), ScheduleList.class);
    }

    /**
     * Deletes a schedule: {@code DELETE
     * https://wh.route4me.com/modules/api/v5.0/recurring-routes/schedules/{scheduleUid}}.
     *
     * @param scheduleUid the schedule ID
     * @param withRoutes {@code true} to delete the routes it created too
     *     ({@code with-routes=1})
     * @return the answer of the API
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public ScheduleList deleteSchedule(String scheduleUid, boolean withRoutes) throws APIException {
        URIBuilder builder = v5Builder(SCHEDULES_EP, scheduleUid);
        if (withRoutes) {
            builder.setParameter("with-routes", "1");
        }
        return this.makeRequest(RequestMethod.DELETE, builder, "", ScheduleList.class);
    }

    // --- Route Schedules ---

    /**
     * Lists the route schedules: {@code GET https://wh.route4me.com/modules/api/v5.0/recurring-routes/route-schedules}.
     *
     * @return the route schedules
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteScheduleList getRouteSchedules() throws APIException {
        URIBuilder builder = v5Builder(ROUTE_SCHEDULES_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, RouteScheduleList.class);
    }

    /**
     * Lists the route schedules, one page at a time:
     * {@code GET
     * https://wh.route4me.com/modules/api/v5.0/recurring-routes/route-schedules/pagination?with_pagination=1}.
     *
     * @param page the page number, from 1 ({@code page})
     * @param perPage the number of items per page ({@code per_page})
     * @return the page of route schedules, with the page links
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteScheduleListWithPagination getRouteSchedulesWithPagination(int page, int perPage) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_SCHEDULES_PAGINATION_EP);
        builder.setParameter("page", String.valueOf(page));
        builder.setParameter("per_page", String.valueOf(perPage));
        builder.setParameter("with_pagination", "1");
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, RouteScheduleListWithPagination.class);
    }

    /**
     * Links a route to a schedule: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/recurring-routes/route-schedules}, with the route schedule as the JSON
     * body.
     *
     * @param routeSchedule the route, the schedule, the driver and the vehicle
     * @return the created route schedule
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteScheduleList createRouteSchedule(RouteSchedule routeSchedule) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_SCHEDULES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(routeSchedule), RouteScheduleList.class);
    }

    /**
     * Gets the schedule of a route: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/recurring-routes/route-schedules/{routeId}}.
     *
     * @param routeId the route ID
     * @return the route schedule
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteScheduleResponse getRouteSchedule(String routeId) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_SCHEDULES_EP, routeId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, RouteScheduleResponse.class);
    }

    /**
     * Updates the schedule of a route: {@code PUT
     * https://wh.route4me.com/modules/api/v5.0/recurring-routes/route-schedules/{routeId}}, with the route schedule as
     * the JSON
     * body.
     *
     * @param routeId the route ID
     * @param routeSchedule the new values
     * @return the updated route schedule
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteScheduleList updateRouteSchedule(String routeId, RouteSchedule routeSchedule) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_SCHEDULES_EP, routeId);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(routeSchedule), RouteScheduleList.class);
    }

    /**
     * Removes the schedule of a route: {@code DELETE
     * https://wh.route4me.com/modules/api/v5.0/recurring-routes/route-schedules/{routeId}}.
     *
     * @param routeId the route ID
     * @return whether it was removed
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public StatusResponse deleteRouteSchedule(String routeId) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_SCHEDULES_EP, routeId);
        return this.makeRequest(RequestMethod.DELETE, builder, "", StatusResponse.class);
    }

    /**
     * Deletes the schedule items of a route: {@code DELETE
     * https://wh.route4me.com/modules/api/v5.0/recurring-routes/route-schedules/{routeId}/items}.
     *
     * @param routeId the route ID
     * @return whether they were deleted
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public StatusResponse deleteRouteScheduleItems(String routeId) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_SCHEDULES_EP, routeId, "items");
        return this.makeRequest(RequestMethod.DELETE, builder, "", StatusResponse.class);
    }

    /**
     * Previews the dates on which the schedule of a route creates routes:
     * {@code GET
     * https://wh.route4me.com/modules/api/v5.0/recurring-routes/route-schedules/{routeId}/preview?start=...&end=...}.
     *
     * @param routeId the route ID
     * @param start the first date of the preview ({@code start})
     * @param end the last date of the preview ({@code end})
     * @return the dates of the period, and what happens on each
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteSchedulePreview getRouteSchedulePreview(String routeId, String start, String end) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_SCHEDULES_EP, routeId, "preview");
        builder.setParameter("start", start);
        builder.setParameter("end", end);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, RouteSchedulePreview.class);
    }

    /**
     * Replaces the schedule of a route: {@code PUT
     * https://wh.route4me.com/modules/api/v5.0/recurring-routes/route-schedules/replace/{routeId}}, with the request as
     * the JSON
     * body.
     *
     * @param routeId the route ID
     * @param request the new schedule, driver and vehicle
     * @return the new route schedule
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteScheduleResponse replaceRouteSchedule(String routeId, ReplaceRouteScheduleRequest request)
            throws APIException {
        URIBuilder builder = v5Builder(REPLACE_ROUTE_SCHEDULES_EP, routeId);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(request), RouteScheduleResponse.class);
    }

    // --- Master Routes ---

    /**
     * Makes a route the master route of a new schedule: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/recurring-routes/master-routes}, with the request as the JSON
     * body.
     *
     * @param request the route and its schedule
     * @return the created route schedule
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public CreateMasterRouteResponse createMasterRoute(CreateMasterRouteRequest request) throws APIException {
        URIBuilder builder = v5Builder(MASTER_ROUTES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request),
                CreateMasterRouteResponse.class);
    }

    // --- Scheduled Routes ---

    /**
     * Tells whether a route was created by a schedule: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/recurring-routes/scheduled-routes/is-copy/{routeId}}.
     *
     * @param routeId the route ID
     * @return the answer, in {@code status}
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public StatusResponse isRouteScheduleCopied(String routeId) throws APIException {
        URIBuilder builder = v5Builder(SCHEDULED_ROUTES_IS_COPY_EP, routeId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, StatusResponse.class);
    }

    /**
     * Lists the routes a schedule created from a master route: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/recurring-routes/scheduled-routes/get-copies}, with the
     * request as the JSON body.
     *
     * @param request the master route, the schedule and the date
     * @return the created routes
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteScheduleCopiesResponse getRouteScheduleCopies(GetRouteScheduleCopiesRequest request)
            throws APIException {
        URIBuilder builder = v5Builder(SCHEDULED_ROUTES_GET_COPIES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request),
                RouteScheduleCopiesResponse.class);
    }
}
