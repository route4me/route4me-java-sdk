package com.route4me.sdk.examples.notes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.notes.NoteStoreRequest;
import com.route4me.sdk.services.notes.NotesManagerV5;
import com.route4me.sdk.services.notes.RouteNoteResource;

/**
 * Adds a note to a route through the V5 API with {@link NotesManagerV5#createNote}. Set {@code ROUTE_ID} to a route
 * of a test account: the example adds a note to it.
 */
public class CreateNoteV5 {
    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to a route of a test account; this example adds a note to it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        NotesManagerV5 manager = new NotesManagerV5(apiKey);
        try {
            NoteStoreRequest request = new NoteStoreRequest();
            request.setRouteId(routeId);
            request.setNoteContents("Test note content");
            request.setDeviceType("web");
            RouteNoteResource result = manager.createNote(request);
            System.out.println("Created note: " + result.getNoteId());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
