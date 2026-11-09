package com.route4me.sdk.examples.notes;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.notes.Note;
import com.route4me.sdk.services.notes.NotesManager;

import java.util.List;

/**
 * Reads the notes of one stop of a route with {@link NotesManager#getAddressNotes}. Set {@code ROUTE_ID} to one of
 * your routes and {@code ROUTE_DESTINATION_ID} to one of its stops.
 */
public class GetAddressNotes {

    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        String stopId = System.getenv("ROUTE_DESTINATION_ID");
        if (routeId == null || routeId.isEmpty() || stopId == null || stopId.isEmpty()) {
            System.err.println("Set ROUTE_ID and ROUTE_DESTINATION_ID to one of your routes and one of its stops; this"
                    + " example reads the notes of the stop.");
            return;
        }
        Long routeDestinationId = ExampleInput.toLong("ROUTE_DESTINATION_ID", stopId);
        if (routeDestinationId == null) {
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        NotesManager notesManager = new NotesManager(apiKey);
        try {
            List<Note> notes = notesManager.getAddressNotes(routeId, Long.toString(routeDestinationId));
            if (notes != null) {
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
