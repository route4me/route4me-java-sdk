package com.route4me.sdk.examples.notes;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.notes.NoteStoreRequest;
import com.route4me.sdk.services.notes.NotesManagerV5;
import com.route4me.sdk.services.notes.RouteNoteResource;
public class CreateNoteV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        NotesManagerV5 manager = new NotesManagerV5(apiKey);
        try {
            NoteStoreRequest request = new NoteStoreRequest();
            request.setRouteId("ROUTE_ID_HERE");
            request.setNoteContents("Test note content");
            request.setDeviceType("web");
            RouteNoteResource result = manager.createNote(request);
            System.out.println("Created note: " + result.getNoteId());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
