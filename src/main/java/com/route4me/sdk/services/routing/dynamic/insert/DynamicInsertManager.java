/*
 * The MIT License
 *
 * Copyright 2022 Route4Me.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.route4me.sdk.services.routing.dynamic.insert;

import com.google.gson.reflect.TypeToken;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.utils.URIBuilder;

/**
 * Finds the routes where a new destination fits best, through the V5 API
 * ({@code https://wh.route4me.com/modules/api/v5.0/routes/lookup-for-new-destination}).
 */
public class DynamicInsertManager extends Manager {

    /** The path of the lookup, on {@code wh.route4me.com}. */
    public static final String ROUTE_DYNAMIC_INSERT = "/modules/api/v5.0/routes/lookup-for-new-destination";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     */
    public DynamicInsertManager(String apiKey) {
        super(apiKey);
    }

    private URIBuilder getURI(String endpoint) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        builder.setPath(endpoint);
        return builder;

    }

    /**
     * Finds the routes that a new destination fits best:
     * {@code POST https://wh.route4me.com/modules/api/v5.0/routes/lookup-for-new-destination}.
     *
     * @param scheduledFor  the route date, such as {@code 2026-10-22}; {@code null} leaves it out
     * @param lat           the latitude of the new destination
     * @param lng           the longitude of the new destination
     * @param insertMode    where the destination may go; {@code null} leaves it out and the
     *                      API uses its default
     * @param recommendedBy what the ranking minimises; {@code null} leaves it out and the
     *                      API uses its default
     * @param limit         the maximum number of routes to return
     * @param routeIDs      the routes to consider; {@code null} considers all
     * @return the matching routes, best first
     * @throws IllegalArgumentException if {@code lat} or {@code lng} is {@code null}: the API requires both
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public List<MatchedRoute> lookForNewDestination(String scheduledFor, Double lat, Double lng, InsertMode insertMode,
            RecommendedBy recommendedBy, Integer limit, List<String> routeIDs) throws APIException {
        if (lat == null || lng == null) {
            throw new IllegalArgumentException("The latitude and the longitude of the new destination are required");
        }
        URIBuilder builder = this.getURI(ROUTE_DYNAMIC_INSERT);
        DynamicInsert body = new DynamicInsert();
        body.setRouteInfoServer("v5");
        body.setScheduledFor(scheduledFor);
        body.setLatitude(lat);
        body.setLongitude(lng);
        body.setInsertMode(insertMode != null ? insertMode.getValue() : null);
        body.setLookupResultsLimit(limit);
        body.setRecommendBy(recommendedBy != null ? recommendedBy.getValue() : null);
        body.setRouteIDs(routeIDs);

        return this.makeJSONRequest(RequestMethod.POST, builder, body, new TypeToken<ArrayList<MatchedRoute>>() {
        }.getType());

    }

    /**
     * Finds the routes of a date where a new destination fits best, among all the routes of the account:
     * {@code POST https://wh.route4me.com/modules/api/v5.0/routes/lookup-for-new-destination}.
     *
     * @param scheduledFor  the route date, such as {@code 2026-10-22}; {@code null} leaves it out
     * @param lat           the latitude of the new destination
     * @param lng           the longitude of the new destination
     * @param insertMode    where the destination may go; {@code null} leaves it out and the API uses its default
     * @param recommendedBy what the ranking minimises; {@code null} leaves it out and the API uses its default
     * @param limit         the maximum number of routes to return
     * @return the matching routes, best first
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public List<MatchedRoute> lookForNewDestination(String scheduledFor, Double lat, Double lng, InsertMode insertMode,
            RecommendedBy recommendedBy, Integer limit) throws APIException {
        return this.lookForNewDestination(scheduledFor, lat, lng, insertMode, recommendedBy, limit, null);
    }

    /**
     * Finds, among some routes, those where a new destination fits best:
     * {@code POST https://wh.route4me.com/modules/api/v5.0/routes/lookup-for-new-destination}.
     *
     * @param lat           the latitude of the new destination
     * @param lng           the longitude of the new destination
     * @param insertMode    where the destination may go; {@code null} leaves it out and the API uses its default
     * @param recommendedBy what the ranking minimises; {@code null} leaves it out and the API uses its default
     * @param limit         the maximum number of routes to return
     * @param routeIDs      the routes to consider
     * @return the matching routes, best first
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public List<MatchedRoute> lookForNewDestination(Double lat, Double lng, InsertMode insertMode,
            RecommendedBy recommendedBy, Integer limit, List<String> routeIDs) throws APIException {
        return this.lookForNewDestination(null, lat, lng, insertMode, recommendedBy, limit, routeIDs);
    }
}
