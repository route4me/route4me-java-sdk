package com.route4me.sdk.examples.notes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.notes.NotesManagerV5;
import com.route4me.sdk.services.notes.RouteNoteCollection;
import com.route4me.sdk.services.notes.RouteNoteResource;

/**
 * Lists the notes of a route destination through the V5 API with {@link NotesManagerV5#getNotesByDestination}. Set
 * {@code ROUTE_DESTINATION_ID} to a destination of one of your routes.
 */
public class GetNotesByDestinationV5 {
    public static void main(String[] args) {
        String destinationId = System.getenv("ROUTE_DESTINATION_ID");
        if (destinationId == null || destinationId.isEmpty()) {
            System.err.println("Set ROUTE_DESTINATION_ID to a destination of one of your routes; "
                    + "this example lists its notes.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        NotesManagerV5 manager = new NotesManagerV5(apiKey);
        try {
            RouteNoteCollection notes = manager.getNotesByDestination(destinationId, 1, 20);
            if (notes.getData() != null) {
                for (RouteNoteResource note : notes.getData()) {
                    System.out.println(note.getNoteId() + ": " + note.getContents());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
