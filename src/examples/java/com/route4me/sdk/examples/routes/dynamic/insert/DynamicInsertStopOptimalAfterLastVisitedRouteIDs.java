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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Finds, among some routes, where a new stop fits best after the last visited stop with
 * {@link DynamicInsertManager#lookForNewDestination} and adds it to the best one with
 * {@link RoutingManager#addAddressesToRoute}. Set {@code ROUTE_IDS} to the IDs of routes of a test account, separated
 * by commas: the example adds the stop to the best of them.
 */
public class DynamicInsertStopOptimalAfterLastVisitedRouteIDs {

    public static void main(String[] args) {
        String routeIdList = System.getenv("ROUTE_IDS");
        List<String> routeIDs = new ArrayList<>();
        if (routeIdList != null) {
            for (String routeId : routeIdList.split(",")) {
                if (!routeId.trim().isEmpty()) {
                    routeIDs.add(routeId.trim());
                }
            }
        }
        if (routeIDs.isEmpty()) {
            System.err.println("Set ROUTE_IDS to the IDs of routes of a test account, separated by commas; this example"
                    + " adds a stop to one of them.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");

        DynamicInsertManager dynamicManager = new DynamicInsertManager(apiKey);
        RoutingManager routeManager = new RoutingManager(apiKey);

        Address address = new Address("Address 1", 43.103865, -79.093727);

        try {
            List<MatchedRoute> matchedRoutes = dynamicManager.lookForNewDestination(address.getLatitude(),
                    address.getLongitude(), InsertMode.OPTIMAL_AFTER_LAST_VISITED, RecommendedBy.DISTANCE, 5, routeIDs);
            if (matchedRoutes == null || matchedRoutes.isEmpty()) {
                System.out.println("None of the routes fits the stop; nothing was inserted.");
                return;
            }
            for (MatchedRoute matchedRoute : matchedRoutes) {
                System.out.println(matchedRoute);
            }
            MatchedRoute matchedRoute = matchedRoutes.get(0);
            if (!routeIDs.contains(matchedRoute.getRouteID())) {
                System.out.println("The best route is not in ROUTE_IDS; nothing was inserted.");
                return;
            }
            System.out.println("Inserting the stop into route " + matchedRoute.getRouteID());
            address.setSequenceNo(matchedRoute.getRecommendedInsertionStopNumber());

            List<Address> addresses = Arrays.asList(address);

            routeManager.addAddressesToRoute(matchedRoute.getRouteID(), addresses);

        } catch (APIException ex) {
            Logger.getLogger(DynamicInsertStopOptimalAfterLastVisitedRouteIDs.class.getName()).log(Level.SEVERE, null,
                    ex);
        }

    }

}
