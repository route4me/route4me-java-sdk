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
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author route4me
 */
public class DynamicInsertStopOptimalAfterLastVisited {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");

        DynamicInsertManager dynamicManager = new DynamicInsertManager(apiKey);
        RoutingManager routeManager = new RoutingManager(apiKey);

        Address address = new Address("Ascension St. Vincent Hospital - Indianapolis", 39.921478, -86.175649);
        try {
            List<MatchedRoute> matchedRoutes = dynamicManager.lookForNewDestination("2022-04-27", address.getLatitude(), address.getLongitude(), InsertMode.OPTIMAL_AFTER_LAST_VISITED, RecommendedBy.DISTANCE, 5);
            for (MatchedRoute matchedRoute : matchedRoutes) {
                System.out.println(matchedRoute);
            }
            System.out.println("Inserting Stop into first Route from the List");

            MatchedRoute matchedRoute = matchedRoutes.get(0);
            address.setSequenceNo(matchedRoute.getRecommendedInsertionStopNumber());

            List<Address> addresses = Arrays.asList(address);

            routeManager.addAddressesToRoute(matchedRoute.getRouteID(), addresses);

        } catch (APIException ex) {
            Logger.getLogger(DynamicInsertStopOptimalAfterLastVisited.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
