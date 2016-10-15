package com.route4me.sdk.services.activities;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.responses.StatusResponse;
import org.apache.http.client.utils.URIBuilder;

public class ActivityManager extends Manager {
    public static final String GET_ACTIVITIES_EP = "/api/get_activities.php";
    public static final String ACTIVITY_FEED_EP = "/api.v4/activity_feed.php";

    public ActivityManager(String apiKey) {
        super(apiKey);
    }

    public Activities getActivities(ActivityRequest request) throws APIException {
        return this.makeJSONRequest(RequestMethod.GET, Manager.defaultBuilder(GET_ACTIVITIES_EP), request, Activities.class);
    }

    public void logActivity(String message, String routeId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ACTIVITY_FEED_EP);
        Activity activity = new Activity();
        activity.setRouteId(routeId);
        activity.setMessage(message);
        activity.setType(ActivityType.USER_MESSAGE);
        StatusResponse resp = this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(activity), StatusResponse.class);
        if (!resp.getStatus()) {
            throw new APIException("Operation has not succeeded with an unknown error");
        }
    }
}
