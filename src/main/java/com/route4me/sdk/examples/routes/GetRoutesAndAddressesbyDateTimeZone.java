package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.RoutesRequest;
import com.route4me.sdk.services.routing.RoutingManager;
import com.route4me.sdk.services.routing.search.RouteData;
import com.route4me.sdk.services.routing.search.RouteSearchResult;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetRoutesAndAddressesbyDateTimeZone {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager routeManager = new RoutingManager(apiKey);
        RouteSearchResult routes = new RouteSearchResult();
        String startDateTime = "2022-04-02";
        String endDateTime = "2022-04-02";
        String timeZone = "America/New_York";
        try {
            routes = routeManager.getRoutesbyDateTimeZone(startDateTime, endDateTime, timeZone);
        } catch (APIException e) {
            //handle exceptions
            e.printStackTrace();
        }
        int i = 0;
        for (RouteData r : routes.getData()) {
                i++;
                System.out.println("Route #" + i + " " + r.getRouteID()+ " " + r.getRouteName());
            try {
                for (Address address: routeManager.getRoute(new RoutesRequest().setId(r.getRouteID())).getAddresses()){
                    System.out.println("\t " + address.getSequenceNo() + " " + address.getAddress() + " " + address.getAlias());
                }
            } catch (APIException ex) {
                Logger.getLogger(GetRoutesAndAddressesbyDateTimeZone.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println();
        }


    }
}
