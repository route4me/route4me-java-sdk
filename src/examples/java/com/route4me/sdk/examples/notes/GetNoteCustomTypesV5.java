package com.route4me.sdk.examples.notes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.notes.NoteCustomType;
import com.route4me.sdk.services.notes.NoteCustomTypeCollection;
import com.route4me.sdk.services.notes.NotesManagerV5;

/** Lists the custom note types through the V5 API with {@link NotesManagerV5#getNoteCustomTypes}. */
public class GetNoteCustomTypesV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        NotesManagerV5 manager = new NotesManagerV5(apiKey);
        try {
            NoteCustomTypeCollection types = manager.getNoteCustomTypes();
            if (types.getData() != null) {
                for (NoteCustomType type : types.getData()) {
                    System.out.println(type.getNoteCustomType() + ": " + type.getNoteCustomTypeValues());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
