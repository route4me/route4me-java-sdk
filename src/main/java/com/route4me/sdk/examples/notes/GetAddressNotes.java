
package com.route4me.sdk.examples.notes;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.services.notes.NotesManager;
import com.route4me.sdk.managers.RouteManager;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.model.Routes;
import java.util.List;

/**
 *
 * @author juan
 */
public class GetAddressNotes {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        RouteManager routeManager = route4me.getRouteManager();
        List<Routes> routes = routeManager.getRoutes(10, 5);
        Routes route = routes.get(0);
        DataObject responseOnbject = routeManager.getRoute(route.getOptimization_problem_id());
        NotesManager notesManager = route4me.getNotesManager();
        String routeDestinationID = responseOnbject.getAddresses().get(0).getRoute_destination_id().toString();
        Response response = notesManager.getAddressNotes(route.getRoute_id(), routeDestinationID);
        System.out.println(response.getResponseBody());
    }

}
