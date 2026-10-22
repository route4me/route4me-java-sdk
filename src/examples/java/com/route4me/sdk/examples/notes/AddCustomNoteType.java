package com.route4me.sdk.examples.notes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.notes.CustomNoteTypeRequest;
import com.route4me.sdk.services.notes.CustomNoteTypeResponse;
import com.route4me.sdk.services.notes.NotesManager;

import java.util.Arrays;

/** Adds a custom note type through the V4 API with {@link NotesManager#addCustomNoteType}. */
public class AddCustomNoteType {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        NotesManager manager = new NotesManager(apiKey);
        try {
            CustomNoteTypeRequest request = new CustomNoteTypeRequest();
            request.setNoteCustomType("Delivery problem");
            request.setNoteCustomTypeValues(Arrays.asList("Gate closed", "Nobody home", "Wrong address"));
            CustomNoteTypeResponse result = manager.addCustomNoteType(request);
            System.out.println(result.getResult() + ", affected: " + result.getAffected());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
