package com.route4me.sdk.examples.notes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Constants;
import com.route4me.sdk.services.notes.Notes;
import com.route4me.sdk.services.notes.NotesManager;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RoutesRequest;
import com.route4me.sdk.services.routing.RoutingManager;

import java.util.List;

/**
 * @author juan
 */
public class AddAddressNotes {

    public static void main(String[] args) throws APIException {
        String apiKey = "11111111111111111111111111111111";
        RoutingManager routeManager = new RoutingManager(apiKey);
        List<Route> routes = routeManager.getRoutes(new RoutesRequest().setLimit(10));
        Route responseObject = routeManager.getRoute(new RoutesRequest().setId(routes.get(0).getId()));
        NotesManager notesManager = new NotesManager(apiKey);
        Address address = responseObject.getAddresses().get(0);
        long routeDestinationID = address.getRouteDestinationId();
        Number latitude = address.getLatitude();
        Number longitude = address.getLongitude();
        String noteContent = "Adding a note to an Address";
        String activityType = "wrongdelivery";
        Notes note = notesManager.addAddressNotes(routes.get(0).getId(),
                Long.toString(routeDestinationID),
                noteContent,
                activityType,
                Constants.DeviceType.WEB,
                latitude,
                longitude
        );
        if (note.getStatus()) {
            System.out.println(note);
        }
    }

}
