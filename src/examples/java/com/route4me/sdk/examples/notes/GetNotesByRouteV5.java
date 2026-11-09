package com.route4me.sdk.examples.notes;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.notes.NotesManagerV5;
import com.route4me.sdk.services.notes.RouteNoteCollection;
import com.route4me.sdk.services.notes.RouteNoteResource;
/**
 * Lists the notes of a route through the V5 API with {@link NotesManagerV5#getNotesByRoute}. Set {@code ROUTE_ID} to
 * one of your routes.
 */
public class GetNotesByRouteV5 {
    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to one of your routes; this example lists its notes.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        NotesManagerV5 manager = new NotesManagerV5(apiKey);
        try {
            RouteNoteCollection notes = manager.getNotesByRoute(routeId, 1, 10);
            if (notes.getData() != null) {
                for (RouteNoteResource note : notes.getData()) {
                    System.out.println("Note: " + note.getContents());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
