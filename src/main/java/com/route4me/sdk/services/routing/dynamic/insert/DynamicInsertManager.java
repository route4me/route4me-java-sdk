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
 *
 * @author route4me
 */
public class DynamicInsertManager extends Manager {

    public static final String ROUTE_DYNAMIC_INSERT = "/modules/api/v5.0/routes/lookup-for-new-destination";

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

    public List<MatchedRoute> lookForNewDestination(String scheduledFor, Double lat, Double lng, InsertMode insertMode, RecommendedBy recommendedBy, Integer limit, List<String> routeIDs) throws APIException {
        URIBuilder builder = this.getURI(ROUTE_DYNAMIC_INSERT);
        DynamicInsert body = new DynamicInsert();
        body.setRouteInfoServer("v5");
        body.setScheduledFor(scheduledFor);
        body.setLatitude(lat);
        body.setLongitude(lng);
        body.setInsertMode(insertMode.getValue());
        body.setLookupResultsLimit(limit);
        body.setRecommendBy(recommendedBy.getValue());
        body.setRouteIDs(routeIDs);

        return this.makeJSONRequest(RequestMethod.POST, builder, body, new TypeToken<ArrayList<MatchedRoute>>() {
        }.getType());

    }

    public List<MatchedRoute> lookForNewDestination(String scheduledFor, Double lat, Double lng, InsertMode insertMode, RecommendedBy recommendedBy, Integer limit) throws APIException {
        return this.lookForNewDestination(scheduledFor, lat, lng, insertMode, recommendedBy, limit, null);
    }

    public List<MatchedRoute> lookForNewDestination(Double lat, Double lng, InsertMode insertMode, RecommendedBy recommendedBy, Integer limit, List<String> routeIDs) throws APIException {
        return this.lookForNewDestination(null, lat, lng, insertMode, recommendedBy, limit, routeIDs);
    }
}
