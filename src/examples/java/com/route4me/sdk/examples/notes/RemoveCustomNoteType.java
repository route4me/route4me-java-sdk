package com.route4me.sdk.examples.notes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.notes.CustomNoteTypeResponse;
import com.route4me.sdk.services.notes.NotesManager;

/**
 * Deletes a custom note type through the V4 API with {@link NotesManager#removeCustomNoteType}. Set
 * {@code CUSTOM_NOTE_TYPE_ID} to a custom note type of a test account: the example deletes it.
 */
public class RemoveCustomNoteType {
    public static void main(String[] args) {
        String noteTypeId = System.getenv("CUSTOM_NOTE_TYPE_ID");
        if (noteTypeId == null || noteTypeId.isEmpty()) {
            System.err.println("Set CUSTOM_NOTE_TYPE_ID to the ID of a custom note type of a test account; this"
                    + " example deletes it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        NotesManager manager = new NotesManager(apiKey);
        try {
            long customNoteTypeId = Long.parseLong(noteTypeId);
            CustomNoteTypeResponse result = manager.removeCustomNoteType(customNoteTypeId);
            System.out.println(result.getResult() + ", affected: " + result.getAffected());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
