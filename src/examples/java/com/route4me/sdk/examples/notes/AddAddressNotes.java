package com.route4me.sdk.examples.notes;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.notes.NoteRequest;
import com.route4me.sdk.services.notes.NotesManager;
import com.route4me.sdk.services.notes.StatusUpdateType;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.Constants;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RoutesRequest;
import com.route4me.sdk.services.routing.RoutingManager;

/**
 * Adds a note to a destination of a route with {@link NotesManager#addAddressNotes}, after reading the stop's
 * position with {@link RoutingManager#getRoute}. Set {@code ROUTE_ID} and {@code ROUTE_DESTINATION_ID} to a route of a
 * test account and one of its stops: the example adds a note to that stop.
 */
public class AddAddressNotes {

    public static void main(String[] args) throws APIException {
        String routeId = System.getenv("ROUTE_ID");
        String stopId = System.getenv("ROUTE_DESTINATION_ID");
        if (routeId == null || routeId.isEmpty() || stopId == null || stopId.isEmpty()) {
            System.err.println("Set ROUTE_ID and ROUTE_DESTINATION_ID to a route of a test account and one of its"
                    + " stops; this example adds a note to that stop.");
            return;
        }
        Long routeDestinationId = ExampleInput.toLong("ROUTE_DESTINATION_ID", stopId);
        if (routeDestinationId == null) {
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager routeManager = new RoutingManager(apiKey);
        Route route = routeManager.getRoute(new RoutesRequest().setId(routeId));
        Address address = null;
        if (route != null && route.getAddresses() != null) {
            for (Address stop : route.getAddresses()) {
                if (routeDestinationId.equals(stop.getRouteDestinationId())) {
                    address = stop;
                }
            }
        }
        if (address == null) {
            System.err.println("Route " + routeId + " has no stop " + routeDestinationId + ".");
            return;
        }
        NotesManager notesManager = new NotesManager(apiKey);
        String noteContent = "Adding a note to an Address";
        NoteRequest request = new NoteRequest().setRouteId(routeId)
                .setAddressId(Long.toString(routeDestinationId))
                .setLatitude(address.getLatitude())
                .setLongitude(address.getLongitude())
                .setDeviceType(Constants.DeviceType.WEB);
        NotesManager.AddedNote note = notesManager.addAddressNotes(request, noteContent,
                StatusUpdateType.WRONG_DELIVERY);
        if (note.getStatus()) {
            System.out.println(note);
        }
    }

}
