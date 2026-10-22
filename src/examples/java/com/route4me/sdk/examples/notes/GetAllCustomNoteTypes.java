package com.route4me.sdk.examples.notes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.notes.CustomNoteType;
import com.route4me.sdk.services.notes.NotesManager;

/** Lists the custom note types through the V4 API with {@link NotesManager#getAllCustomNoteTypes}. */
public class GetAllCustomNoteTypes {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        NotesManager manager = new NotesManager(apiKey);
        try {
            for (CustomNoteType type : manager.getAllCustomNoteTypes()) {
                System.out.println(type.getNoteCustomTypeId() + " " + type.getNoteCustomType() + ": "
                        + type.getNoteCustomTypeValues());
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
