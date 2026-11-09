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
package com.route4me.sdk.examples.routes.dynamic.insert;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.RoutingManager;
import com.route4me.sdk.services.routing.dynamic.insert.DynamicInsertManager;
import com.route4me.sdk.services.routing.dynamic.insert.InsertMode;
import com.route4me.sdk.services.routing.dynamic.insert.MatchedRoute;
import com.route4me.sdk.services.routing.dynamic.insert.RecommendedBy;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Finds the routes where a new stop fits at the end ({@code InsertMode.END_OF_ROUTE}) with
 * {@link DynamicInsertManager#lookForNewDestination} and adds it to one of them with
 * {@link RoutingManager#addAddressesToRoute}.
 * Set {@code ROUTE_ID} to a route of a test account: the example adds the stop to that route, at the recommended
 * position, only when the route is among those found. {@code ROUTE_DATE} sets the date of the routes to search, as
 * {@code YYYY-MM-DD}; it defaults to today.
 */
public class DynamicInsertStopEndOfRoute {

    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of a route of a test account; this example adds a stop to it.");
            return;
        }
        String routeDate = System.getenv("ROUTE_DATE");
        if (routeDate == null || routeDate.isEmpty()) {
            routeDate = LocalDate.now().toString();
        }
        String apiKey = System.getenv("R4M_API_KEY");

        DynamicInsertManager dynamicManager = new DynamicInsertManager(apiKey);
        RoutingManager routeManager = new RoutingManager(apiKey);

        Address address = new Address("Ascension St. Vincent Hospital - Indianapolis", 39.921478, -86.175649);

        try {
            List<MatchedRoute> matchedRoutes = dynamicManager.lookForNewDestination(routeDate,
                    address.getLatitude(), address.getLongitude(), InsertMode.END_OF_ROUTE,
                    RecommendedBy.DISTANCE, 5);
            MatchedRoute target = null;
            if (matchedRoutes != null) {
                for (MatchedRoute matchedRoute : matchedRoutes) {
                    System.out.println(matchedRoute);
                    if (routeId.equalsIgnoreCase(matchedRoute.getRouteID())) {
                        target = matchedRoute;
                    }
                }
            }
            if (target == null) {
                System.out.println("Route " + routeId + " is not among the routes found for " + routeDate
                        + "; nothing was inserted.");
                return;
            }
            System.out.println("Inserting the stop into route " + routeId);
            address.setSequenceNo(target.getRecommendedInsertionStopNumber());

            List<Address> addresses = Arrays.asList(address);

            routeManager.addAddressesToRoute(routeId, addresses);

        } catch (APIException ex) {
            Logger.getLogger(DynamicInsertStopEndOfRoute.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
