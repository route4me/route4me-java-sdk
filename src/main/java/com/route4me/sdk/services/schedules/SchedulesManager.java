package com.route4me.sdk.services.schedules;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

import java.util.List;

public class SchedulesManager extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String SCHEDULES_EP = V5_BASE + "/recurring-routes/schedules";
    private static final String SCHEDULES_PAGINATION_EP = V5_BASE + "/recurring-routes/schedules/pagination";
    private static final String ROUTE_SCHEDULES_EP = V5_BASE + "/recurring-routes/route-schedules";
    private static final String ROUTE_SCHEDULES_PAGINATION_EP = V5_BASE + "/recurring-routes/route-schedules/pagination";
    private static final String REPLACE_ROUTE_SCHEDULES_EP = V5_BASE + "/recurring-routes/route-schedules/replace";
    private static final String SCHEDULED_ROUTES_IS_COPY_EP = V5_BASE + "/recurring-routes/scheduled-routes/is-copy";
    private static final String SCHEDULED_ROUTES_GET_COPIES_EP = V5_BASE + "/recurring-routes/scheduled-routes/get-copies";
    private static final String MASTER_ROUTES_EP = V5_BASE + "/recurring-routes/master-routes";

    public SchedulesManager(String apiKey) {
        super(apiKey);
    }

    public SchedulesManager(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    private static URIBuilder v5Builder(String endpoint) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        builder.setPath(endpoint);
        return builder;
    }

    // --- Schedules ---

    public ScheduleList getSchedules() throws APIException {
        URIBuilder builder = v5Builder(SCHEDULES_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, ScheduleList.class);
    }

    public ScheduleListWithPagination getSchedulesWithPagination(int page, int perPage) throws APIException {
        URIBuilder builder = v5Builder(SCHEDULES_PAGINATION_EP);
        builder.setParameter("page", String.valueOf(page));
        builder.setParameter("per_page", String.valueOf(perPage));
        builder.setParameter("with_pagination", "1");
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, ScheduleListWithPagination.class);
    }

    public ScheduleList createSchedule(Schedule schedule) throws APIException {
        URIBuilder builder = v5Builder(SCHEDULES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(schedule), ScheduleList.class);
    }

    public ScheduleList getSchedule(String scheduleUid) throws APIException {
        URIBuilder builder = v5Builder(SCHEDULES_EP + "/" + scheduleUid);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, ScheduleList.class);
    }

    public ScheduleList updateSchedule(String scheduleUid, Schedule schedule) throws APIException {
        URIBuilder builder = v5Builder(SCHEDULES_EP + "/" + scheduleUid);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(schedule), ScheduleList.class);
    }

    public ScheduleList deleteSchedule(String scheduleUid, boolean withRoutes) throws APIException {
        URIBuilder builder = v5Builder(SCHEDULES_EP + "/" + scheduleUid);
        if (withRoutes) {
            builder.setParameter("with-routes", "1");
        }
        return this.makeRequest(RequestMethod.DELETE, builder, "", ScheduleList.class);
    }

    // --- Route Schedules ---

    public RouteScheduleList getRouteSchedules() throws APIException {
        URIBuilder builder = v5Builder(ROUTE_SCHEDULES_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, RouteScheduleList.class);
    }

    public RouteScheduleListWithPagination getRouteSchedulesWithPagination(int page, int perPage) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_SCHEDULES_PAGINATION_EP);
        builder.setParameter("page", String.valueOf(page));
        builder.setParameter("per_page", String.valueOf(perPage));
        builder.setParameter("with_pagination", "1");
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, RouteScheduleListWithPagination.class);
    }

    public RouteScheduleList createRouteSchedule(RouteSchedule routeSchedule) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_SCHEDULES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(routeSchedule), RouteScheduleList.class);
    }

    public RouteScheduleResponse getRouteSchedule(String routeId) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_SCHEDULES_EP + "/" + routeId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, RouteScheduleResponse.class);
    }

    public RouteScheduleList updateRouteSchedule(String routeId, RouteSchedule routeSchedule) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_SCHEDULES_EP + "/" + routeId);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(routeSchedule), RouteScheduleList.class);
    }

    public StatusResponse deleteRouteSchedule(String routeId) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_SCHEDULES_EP + "/" + routeId);
        return this.makeRequest(RequestMethod.DELETE, builder, "", StatusResponse.class);
    }

    public StatusResponse deleteRouteScheduleItems(String routeId) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_SCHEDULES_EP + "/" + routeId + "/items");
        return this.makeRequest(RequestMethod.DELETE, builder, "", StatusResponse.class);
    }

    public RouteSchedulePreview getRouteSchedulePreview(String routeId, String start, String end) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_SCHEDULES_EP + "/" + routeId + "/preview");
        builder.setParameter("start", start);
        builder.setParameter("end", end);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, RouteSchedulePreview.class);
    }

    public RouteScheduleResponse replaceRouteSchedule(String routeId, ReplaceRouteScheduleRequest request) throws APIException {
        URIBuilder builder = v5Builder(REPLACE_ROUTE_SCHEDULES_EP + "/" + routeId);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(request), RouteScheduleResponse.class);
    }

    // --- Master Routes ---

    public CreateMasterRouteResponse createMasterRoute(CreateMasterRouteRequest request) throws APIException {
        URIBuilder builder = v5Builder(MASTER_ROUTES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), CreateMasterRouteResponse.class);
    }

    // --- Scheduled Routes ---

    public StatusResponse isRouteScheduleCopied(String routeId) throws APIException {
        URIBuilder builder = v5Builder(SCHEDULED_ROUTES_IS_COPY_EP + "/" + routeId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, StatusResponse.class);
    }

    public RouteScheduleCopiesResponse getRouteScheduleCopies(GetRouteScheduleCopiesRequest request) throws APIException {
        URIBuilder builder = v5Builder(SCHEDULED_ROUTES_GET_COPIES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), RouteScheduleCopiesResponse.class);
    }
}
