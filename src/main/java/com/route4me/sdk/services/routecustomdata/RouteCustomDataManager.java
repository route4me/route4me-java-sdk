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

/**
 * Reads and writes the custom data of routes, free key-value pairs, through the V5 API
 * ({@code https://wh.route4me.com/modules/api/v5.0/routes/{routeId}/custom-data}).
 */
public class RouteCustomDataManager extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String ROUTES_CUSTOM_DATA_EP = V5_BASE + "/routes";
    private static final String BULK_CUSTOM_DATA_EP = V5_BASE + "/routes/custom-data/bulk";
    private static final String CUSTOM_DATA_KEYS_EP = V5_BASE + "/routes/custom-data/keys";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     */
    public RouteCustomDataManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
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

    /**
     * Gets the custom data of a route: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/routes/{routeId}/custom-data}.
     *
     * @param routeId the route ID
     * @return the key-value pairs
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Map<String, String> getRouteCustomData(String routeId) throws APIException {
        URIBuilder builder = v5Builder(ROUTES_CUSTOM_DATA_EP, routeId, "custom-data");
        Type type = new TypeToken<HashMap<String, String>>() { }.getType();
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, type);
    }

    /**
     * Replaces the custom data of a route: {@code PUT
     * https://wh.route4me.com/modules/api/v5.0/routes/{routeId}/custom-data}, with the pairs as the JSON body.
     *
     * @param routeId the route ID
     * @param customData the key-value pairs
     * @return the custom data of the route after the update
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Map<String, String> updateRouteCustomData(String routeId, Map<String, String> customData)
            throws APIException {
        URIBuilder builder = v5Builder(ROUTES_CUSTOM_DATA_EP, routeId, "custom-data");
        Type type = new TypeToken<HashMap<String, String>>() { }.getType();
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(customData), type);
    }

    /**
     * Gets the custom data of several routes: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/routes/custom-data/bulk}, with {@code {"route_ids": [...]}} as
     * the JSON body.
     *
     * @param routeIds the route IDs
     * @return the custom data of each route
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteCustomDataCollection getBulkRouteCustomData(String[] routeIds) throws APIException {
        URIBuilder builder = v5Builder(BULK_CUSTOM_DATA_EP);
        BulkRouteCustomDataRequest request = new BulkRouteCustomDataRequest();
        request.setRouteIds(routeIds);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request),
                RouteCustomDataCollection.class);
    }

    /**
     * Lists the custom data keys used in the routes of the account: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/routes/custom-data/keys}.
     *
     * @return the keys
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public String[] getCustomDataKeys() throws APIException {
        URIBuilder builder = v5Builder(CUSTOM_DATA_KEYS_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, String[].class);
    }
}
