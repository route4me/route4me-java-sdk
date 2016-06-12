package com.route4me.sdk.services.activities;

import com.google.gson.Gson;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.model.enums.APIEndpoints;
import org.apache.http.client.utils.URIBuilder;

public class ActivityManager extends Manager {

    public ActivityManager(String apiKey) {
        super(apiKey, new Gson());
    }

    public Activities getActivities(int limit, int offset, String routeId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(APIEndpoints.GET_ACTIVITIES_HOST);
        builder.setParameter("limit", Integer.toString(limit));
        builder.setParameter("offset", Integer.toString(offset));
        builder.setParameter("route_id", routeId);

        return this.makeRequest(RequestMethod.GET, builder, "", Activities.class);
    }
}
