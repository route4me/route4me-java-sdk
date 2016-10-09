package com.route4me.sdk.examples.notes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.notes.Note;
import com.route4me.sdk.services.notes.NotesManager;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RoutesRequest;
import com.route4me.sdk.services.routing.RoutingManager;

import java.util.List;

/**
 * @author juan
 */
public class GetAddressNotes {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        RoutingManager routingManager = new RoutingManager(apiKey);
        try {
            List<Route> routes = routingManager.getRoutes(new RoutesRequest().setLimit(2));
            for(Route route : routes) {
                DataObject dataObject = routingManager.getRoute(new RoutesRequest().setId(route.getId()));
                NotesManager notesManager = new NotesManager(apiKey);
                List<Note> notes = notesManager.getAddressNotes(route.getId(), Long.toString(dataObject.getAddresses().get(0).getRouteDestinationId()));
                for (Note note : notes) {
                    System.out.println(note);
                }
            }
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }

}
