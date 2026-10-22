package com.route4me.sdk.services.routecustomdata;

import com.google.gson.reflect.TypeToken;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class RouteCustomDataManager extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String ROUTES_CUSTOM_DATA_EP = V5_BASE + "/routes";
    private static final String BULK_CUSTOM_DATA_EP = V5_BASE + "/routes/custom-data/bulk";
    private static final String CUSTOM_DATA_KEYS_EP = V5_BASE + "/routes/custom-data/keys";

    public RouteCustomDataManager(String apiKey) {
        super(apiKey);
    }

    public RouteCustomDataManager(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    private static URIBuilder v5Builder(String endpoint, Object... segments) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        Manager.withPathSegments(builder, endpoint, segments);
        return builder;
    }

    public Map<String, String> getRouteCustomData(String routeId) throws APIException {
        URIBuilder builder = v5Builder(ROUTES_CUSTOM_DATA_EP, routeId, "custom-data");
        Type type = new TypeToken<HashMap<String, String>>() {}.getType();
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, type);
    }

    public Map<String, String> updateRouteCustomData(String routeId, Map<String, String> customData) throws APIException {
        URIBuilder builder = v5Builder(ROUTES_CUSTOM_DATA_EP, routeId, "custom-data");
        Type type = new TypeToken<HashMap<String, String>>() {}.getType();
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(customData), type);
    }

    public RouteCustomDataCollection getBulkRouteCustomData(String[] routeIds) throws APIException {
        URIBuilder builder = v5Builder(BULK_CUSTOM_DATA_EP);
        BulkRouteCustomDataRequest request = new BulkRouteCustomDataRequest();
        request.setRouteIds(routeIds);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), RouteCustomDataCollection.class);
    }

    public String[] getCustomDataKeys() throws APIException {
        URIBuilder builder = v5Builder(CUSTOM_DATA_KEYS_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, String[].class);
    }
}
