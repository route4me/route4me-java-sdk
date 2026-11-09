package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RoutesRequest;
import com.route4me.sdk.services.routing.RoutingManager;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Lists the routes of a period given in local time with {@link RoutingManager#getRoutesbyTimeDate} and reads the
 * addresses of each with {@link RoutingManager#getRoute}.
 */
public class GetRoutesAndAddressesbyLocalDateTime {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager routeManager = new RoutingManager(apiKey);
        List<Route> routes;
        String startDateTime = "2022-04-18T23:00:00:000-05:00";
        String endDateTime = "2022-04-22T23:00:00:000-05:00";
        try {
            routes = routeManager.getRoutesbyTimeDate(startDateTime, endDateTime);
        } catch (APIException e) {
            //handle exceptions
            e.printStackTrace();
            return;
        }
        if (routes == null) {
            System.out.println("No routes found");
            return;
        }
        int i = 0;
        for (Route r : routes) {
            i++;
            String routeName = r.getParameters() != null ? r.getParameters().getRouteName() : null;
            System.out.println("Route #" + i + " " + r.getId() + " " + routeName);
            try {
                Route route = routeManager.getRoute(new RoutesRequest().setId(r.getId()));
                if (route != null && route.getAddresses() != null) {
                    for (Address address : route.getAddresses()) {
                        System.out.println("\t " + address.getSequenceNo() + " " + address.getAddress() + " "
                                + address.getAlias());
                    }
                }
            } catch (APIException ex) {
                Logger.getLogger(GetRoutesAndAddressesbyLocalDateTime.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println();
        }


    }
}
