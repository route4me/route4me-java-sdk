package com.route4me.sdk.examples.notes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.notes.BulkNotesResponse;
import com.route4me.sdk.services.notes.NoteStoreBulkRequest;
import com.route4me.sdk.services.notes.NoteStoreRequest;
import com.route4me.sdk.services.notes.NotesManagerV5;

import java.util.ArrayList;
import java.util.List;

/** Adds two notes to a route in one request through the V5 API with {@link NotesManagerV5#bulkCreateNotes}. */
public class BulkCreateNotesV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        NotesManagerV5 manager = new NotesManagerV5(apiKey);
        try {
            List<NoteStoreRequest> notes = new ArrayList<>();
            for (String text : new String[] {"Gate code 1234", "Leave at the back door"}) {
                NoteStoreRequest note = new NoteStoreRequest();
                note.setRouteId("ROUTE_ID_HERE");
                note.setNoteContents(text);
                notes.add(note);
            }
            NoteStoreBulkRequest request = new NoteStoreBulkRequest();
            request.setNotes(notes);
            request.setDeviceType("web");
            BulkNotesResponse response = manager.bulkCreateNotes(request);
            System.out.println("Accepted: " + response.getStatus() + ", in the background: " + response.getAsync());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
