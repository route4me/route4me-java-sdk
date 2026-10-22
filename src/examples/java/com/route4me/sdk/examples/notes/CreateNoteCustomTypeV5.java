package com.route4me.sdk.examples.notes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.notes.NoteCustomTypeCollection;
import com.route4me.sdk.services.notes.NoteCustomTypeStoreRequest;
import com.route4me.sdk.services.notes.NotesManagerV5;

import java.util.Arrays;

/** Adds a custom note type through the V5 API with {@link NotesManagerV5#createNoteCustomType}. */
public class CreateNoteCustomTypeV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        NotesManagerV5 manager = new NotesManagerV5(apiKey);
        try {
            NoteCustomTypeStoreRequest request = new NoteCustomTypeStoreRequest();
            request.setNoteCustomType("Delivery problem");
            request.setNoteCustomTypeValues(Arrays.asList("Gate closed", "Nobody home"));
            request.setNoteCustomFieldType(1);
            NoteCustomTypeCollection types = manager.createNoteCustomType(request);
            System.out.println("Custom note types: " + (types.getData() != null ? types.getData().size() : 0));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
