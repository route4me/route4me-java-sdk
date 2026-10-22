package com.route4me.sdk.examples.notes;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.notes.NotesManagerV5;
import com.route4me.sdk.services.notes.RouteNoteCollection;
import com.route4me.sdk.services.notes.RouteNoteResource;
public class GetNotesByRouteV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        NotesManagerV5 manager = new NotesManagerV5(apiKey);
        try {
            RouteNoteCollection notes = manager.getNotesByRoute("ROUTE_ID_HERE", 1, 10);
            if (notes.getData() != null) {
                for (RouteNoteResource note : notes.getData()) {
                    System.out.println("Note: " + note.getContents());
                }
            }
        } catch (APIException e) { e.printStackTrace(); }
    }
}
