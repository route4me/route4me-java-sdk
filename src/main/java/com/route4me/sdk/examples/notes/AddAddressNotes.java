
package com.route4me.sdk.examples.notes;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.services.notes.NotesManager;
import com.route4me.sdk.managers.RouteManager;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.notes.Notes;
import com.route4me.sdk.model.Routes;
import com.route4me.sdk.model.enums.Constants;
import java.util.List;

/**
 *
 * @author juan
 */
public class AddAddressNotes {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        RouteManager routeManager = route4me.getRouteManager();
        List<Routes> routes = routeManager.getRoutes(10, 5);
        Routes route = routes.get(0);
        DataObject responseOnbject = routeManager.getRoute(route.getOptimization_problem_id());
        NotesManager notesManager = route4me.getNotesManager();
        Address address = responseOnbject.getAddresses().get(0);
        String routeDestinationID = address.getRoute_destination_id().toString();
        Number latitude = address.getLat();
        Number longitude = address.getLng();
        String noteContent = "Adding a note to an Address";
        String activityType = "wrongdelivery";
        Notes note = notesManager.addAddressNotes(route.getRoute_id(),
                                                  routeDestinationID,
                                                  noteContent,
                                                  activityType,
                                                  Constants.DeviceType.WEB,
                                                  latitude,
                                                  longitude
                                            );
        if (note.getStatus()) {
            System.out.println("Note ID" + note.getNote_id());
            System.out.println("Note Content: " + note.getNote().getContents());
            System.out.println("Route ID: " + note.getNote().getRoute_id());

        }
    }

}
