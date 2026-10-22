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
 * without it the API answers 403. {@link #getScheduleCalendar(ScheduleCalendarRequest)} reads the schedule calendar
 * from the V4 API ({@code https://api.route4me.com}).
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
    private static final String SCHEDULE_CALENDAR_EP = "/api/schedule_calendar_data.php";

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
     * @param schedule the schedule; the API requires its name, recurrence rules ({@code schedule}),
     *     {@code advance_interval}, {@code advance_schedule_interval_days} and time zone
     * @return the created schedule
     * @throws IllegalArgumentException if {@code schedule} or one of the fields the API requires is {@code null}
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public ScheduleList createSchedule(Schedule schedule) throws APIException {
        if (schedule == null) {
            throw new IllegalArgumentException("The schedule is required");
        }
        require(schedule.getName(), "name");
        require(schedule.getScheduleData(), "schedule");
        require(schedule.getAdvanceInterval(), "advance_interval");
        require(schedule.getAdvanceScheduleIntervalDays(), "advance_schedule_interval_days");
        require(schedule.getTimezone(), "timezone");
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
     * @param withRoutes {@code true} to delete the routes it created too; sent as {@code with-routes=1} or
     *     {@code with-routes=0}, as the API requires the parameter
     * @return the answer of the API
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public ScheduleList deleteSchedule(String scheduleUid, boolean withRoutes) throws APIException {
        URIBuilder builder = v5Builder(SCHEDULES_EP, scheduleUid);
        builder.setParameter("with-routes", withRoutes ? "1" : "0");
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
     * @param routeSchedule the route, the schedule, the driver and the vehicle; the API requires the route ID and
     *     the schedule ID
     * @return the created route schedule
     * @throws IllegalArgumentException if {@code routeSchedule}, its route ID or its schedule ID is {@code null}
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteScheduleList createRouteSchedule(RouteSchedule routeSchedule) throws APIException {
        if (routeSchedule == null) {
            throw new IllegalArgumentException("The route schedule is required");
        }
        require(routeSchedule.getRouteId(), "route_id");
        require(routeSchedule.getScheduleUid(), "schedule_uid");
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
     * @param start the first date of the preview ({@code start}), which the API requires
     * @param end the last date of the preview ({@code end}), which the API requires
     * @return the dates of the period, and what happens on each
     * @throws IllegalArgumentException if {@code routeId}, {@code start} or {@code end} is {@code null} or empty
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteSchedulePreview getRouteSchedulePreview(String routeId, String start, String end) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_SCHEDULES_EP, routeId, "preview");
        require(start, "start");
        require(end, "end");
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
     * @param request the new schedule, driver and vehicle; the API requires the recurrence rules ({@code schedule})
     * @return the new route schedule
     * @throws IllegalArgumentException if {@code routeId} is {@code null} or empty, or {@code request} or its
     *     recurrence rules are {@code null}
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteScheduleResponse replaceRouteSchedule(String routeId, ReplaceRouteScheduleRequest request)
            throws APIException {
        URIBuilder builder = v5Builder(REPLACE_ROUTE_SCHEDULES_EP, routeId);
        if (request == null) {
            throw new IllegalArgumentException("The replacement is required");
        }
        require(request.getScheduleData(), "schedule");
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(request), RouteScheduleResponse.class);
    }

    // --- Master Routes ---

    /**
     * Makes a route the master route of a new schedule: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/recurring-routes/master-routes}, with the request as the JSON
     * body.
     *
     * @param request the route and its schedule; the API requires the route ID and the recurrence rules
     *     ({@code schedule})
     * @return the created route schedule
     * @throws IllegalArgumentException if {@code request}, its route ID or its recurrence rules are {@code null}
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public CreateMasterRouteResponse createMasterRoute(CreateMasterRouteRequest request) throws APIException {
        if (request == null) {
            throw new IllegalArgumentException("The master route request is required");
        }
        require(request.getRouteId(), "route_id");
        require(request.getScheduleData(), "schedule");
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
     * @param request the master route, the schedule and the date; the API requires the route ID
     * @return the created routes
     * @throws IllegalArgumentException if {@code request} or its route ID is {@code null}
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteScheduleCopiesResponse getRouteScheduleCopies(GetRouteScheduleCopiesRequest request)
            throws APIException {
        if (request == null) {
            throw new IllegalArgumentException("The request is required");
        }
        require(request.getRouteId(), "route_id");
        URIBuilder builder = v5Builder(SCHEDULED_ROUTES_GET_COPIES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request),
                RouteScheduleCopiesResponse.class);
    }

    // --- Schedule calendar (V4) ---

    /**
     * Counts the scheduled address book contacts, orders and routes of each day of a period: {@code GET
     * https://api.route4me.com/api/schedule_calendar_data.php}, with the request as the query
     * ({@code date_from_string}, {@code date_to_string}, {@code member_id}, {@code timezone_offset_minutes},
     * {@code orders}, {@code ab} and {@code routes_count}). This V4 request sends the API key as the
     * {@code api_key} query parameter.
     *
     * @param request the period and the counts to return; {@code null} sends none of them
     * @return the counts of each day
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public ScheduleCalendarResponse getScheduleCalendar(ScheduleCalendarRequest request) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(SCHEDULE_CALENDAR_EP);
        return this.makeJSONRequest(RequestMethod.GET, builder, request, ScheduleCalendarResponse.class);
    }

    /** Rejects a field the API requires when it is {@code null}, or an empty string. */
    private static void require(Object value, String name) {
        if (value == null || (value instanceof String && ((String) value).isEmpty())) {
            throw new IllegalArgumentException("The field " + name + " is required");
        }
    }
}
