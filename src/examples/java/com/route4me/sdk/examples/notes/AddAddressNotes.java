package com.route4me.sdk.examples.notes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.notes.NoteRequest;
import com.route4me.sdk.services.notes.NotesManager;
import com.route4me.sdk.services.notes.StatusUpdateType;
import com.route4me.sdk.services.routing.*;

import java.util.List;

/**
 * @author juan
 */
public class AddAddressNotes {

    public static void main(String[] args) throws APIException {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager routeManager = new RoutingManager(apiKey);
        List<Route> routes = routeManager.getRoutes(new RoutesRequest().setLimit(10));
        Route responseObject = routeManager.getRoute(new RoutesRequest().setId(routes.get(0).getId()));
        NotesManager notesManager = new NotesManager(apiKey);
        Address address = responseObject.getAddresses().get(0);
        long routeDestinationID = address.getRouteDestinationId();
        String noteContent = "Adding a note to an Address";
        NoteRequest request = new NoteRequest().setRouteId(routes.get(0).getId())
                .setAddressId(Long.toString(routeDestinationID))
                .setLatitude(address.getLatitude())
                .setLongitude(address.getLongitude())
                .setDeviceType(Constants.DeviceType.WEB);
        NotesManager.AddedNote note = notesManager.addAddressNotes(request, noteContent, StatusUpdateType.WRONG_DELIVERY);
        if (note.getStatus()) {
            System.out.println(note);
        }
    }

}
