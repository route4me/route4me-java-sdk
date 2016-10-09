package com.route4me.sdk.services.activities;

import com.google.gson.Gson;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.responses.StatusResponse;
import com.route4me.sdk.exception.APIException;
import org.apache.http.client.utils.URIBuilder;

public class ActivityManager extends Manager {
    public static final String GET_ACTIVITIES_EP = "/api/get_activities.php";
    public static final String ACTIVITY_FEED_EP = "/api.v4/activity_feed.php";

    public ActivityManager(String apiKey) {
        super(apiKey, new Gson());
    }

    public Activities getActivities(ActivityRequest request) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(GET_ACTIVITIES_EP);
        if (request.getRouteId() != null) {
            builder.setParameter("route_id", request.getRouteId());
        }
        if (request.getDeviceId() != null) {
            builder.setParameter("device_id", request.getDeviceId());
        }
        if (request.getType() != null) {
            builder.setParameter("activity_type", request.getType().toString());
        }
        if (request.getMemberId() != null) {
            builder.setParameter("member_id", Integer.toString(request.getMemberId()));
        }
        if (request.isTeam()) {
            builder.setParameter("team", "true");
        }
        if (request.getLimit() != null) {
            builder.setParameter("limit", Integer.toString(request.getLimit()));
        }
        if (request.getOffset() != null) {
            builder.setParameter("offset", Integer.toString(request.getOffset()));
        }
        if (request.getStart() != null) {
            builder.setParameter("start", Integer.toString(request.getStart()));
        }
        if (request.getEnd() != null) {
            builder.setParameter("end", Integer.toString(request.getEnd()));
        }
        return this.makeRequest(RequestMethod.GET, builder, "", Activities.class);
    }

    public void logActivity(String message, String routeId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ACTIVITY_FEED_EP);
        Activity activity = new Activity();
        activity.setRouteId(routeId);
        activity.setMessage(message);
        activity.setType(ActivityType.USER_MESSAGE);
        StatusResponse resp = this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(activity), StatusResponse.class);
        if(!resp.getStatus()) {
            throw new APIException("Operation has not succeeded with an unknown error");
        }
    }
}
