package com.route4me.sdk.examples.notes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.notes.CustomNoteTypeResponse;
import com.route4me.sdk.services.notes.NotesManager;

/** Deletes a custom note type through the V4 API with {@link NotesManager#removeCustomNoteType}. */
public class RemoveCustomNoteType {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        NotesManager manager = new NotesManager(apiKey);
        try {
            long customNoteTypeId = 1L;
            CustomNoteTypeResponse result = manager.removeCustomNoteType(customNoteTypeId);
            System.out.println(result.getResult() + ", affected: " + result.getAffected());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
