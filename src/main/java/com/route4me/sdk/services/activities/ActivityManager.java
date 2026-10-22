package com.route4me.sdk.services.activities;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.responses.StatusResponse;
import org.apache.http.client.utils.URIBuilder;

/**
 * Reads and posts the activities of the account (its activity feed) through the V4 API ({@code
 * https://api.route4me.com}).
 */
public class ActivityManager extends Manager {
    /** The path that lists activities, on {@code api.route4me.com}. */
    public static final String GET_ACTIVITIES_EP = "/api/get_activities.php";
    /** The path that posts to the activity feed, on {@code api.route4me.com}. */
    public static final String ACTIVITY_FEED_EP = "/api.v4/activity_feed.php";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as the {@code api_key} query parameter
     */
    public ActivityManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Lists activities: {@code GET https://api.route4me.com/api/get_activities.php}, with the request as the query.
     *
     * @param request the type, the route, the member, the period and the page
     * @return the activities
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Activities getActivities(ActivityRequest request) throws APIException {
        return this.makeJSONRequest(RequestMethod.GET, Manager.defaultBuilder(GET_ACTIVITIES_EP), request,
                Activities.class);
    }

    /**
     * Posts a user message to the activity feed of a route:
     * {@code POST /api.v4/activity_feed.php}.
     *
     * @param message the message
     * @param routeId the route ID
     * @throws APIException if the request fails, or if the answer is empty, has no
     *                      {@code status} or has {@code "status": false}
     */
    public void logActivity(String message, String routeId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ACTIVITY_FEED_EP);
        Activity activity = new Activity();
        activity.setRouteId(routeId);
        activity.setMessage(message);
        activity.setType(ActivityType.USER_MESSAGE);
        StatusResponse resp = this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(activity),
                StatusResponse.class);
        // An empty body or a missing status counts as a failure, like an explicit false.
        if (resp == null || !Boolean.TRUE.equals(resp.getStatus())) {
            throw new APIException("Operation has not succeeded with an unknown error");
        }
    }
}
