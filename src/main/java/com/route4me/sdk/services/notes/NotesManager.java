package com.route4me.sdk.services.notes;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.queryconverter.QueryConverter;
import com.route4me.sdk.services.routing.Address;
import lombok.Data;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/** Reads and adds the notes of route destinations through the V4 API ({@code https://api.route4me.com}). */
public class NotesManager extends Manager {
    /** The path that adds a note to a route destination, on {@code api.route4me.com}. */
    public static final String ADD_NOTES_EP = "/actions/addRouteNotes.php";
    /** The path that reads a route destination with its notes, on {@code api.route4me.com}. */
    public static final String GET_NOTES_EP = "/api.v4/address.php";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as the {@code api_key} query parameter
     */
    public NotesManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Gets the notes of a route destination: {@code GET /api.v4/address.php?notes=1}.
     *
     * @param routeId            the route ID
     * @param routeDestinationId the destination ID
     * @return the notes, or {@code null} when the answer has none
     * @throws APIException if the request fails
     */
    public List<Note> getAddressNotes(String routeId, String routeDestinationId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(GET_NOTES_EP);
        builder.setParameter("route_id", routeId);
        builder.setParameter("route_destination_id", routeDestinationId);
        builder.setParameter("notes", "1");
        Address address = this.makeRequest(RequestMethod.GET, builder, "", Address.class);
        return address == null ? null : address.getNotes();
    }

    /**
     * Adds a note to a route destination: {@code POST /actions/addRouteNotes.php}, with the
     * location in the query and the note as a form.
     *
     * @param note       the route, destination and device location
     * @param content    the note text
     * @param updateType the note type; {@code null} sends {@code unclassified}
     * @return the added note
     * @throws APIException if the request fails
     */
    public AddedNote addAddressNotes(NoteRequest note, String content, StatusUpdateType updateType)
            throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ADD_NOTES_EP);
        try {
            builder.addParameters(QueryConverter.convertObjectToParameters(note));
        } catch (IllegalAccessException e) {
            throw new APIException("Could not convert parameters to query string.", e);
        }
        List<NameValuePair> data = new ArrayList<>();
        data.add(new BasicNameValuePair("strNoteContents", content));
        // Without a type the note is "unclassified", as in the C# SDK.
        data.add(new BasicNameValuePair("strUpdateType", updateType != null ? updateType.toString() : "unclassified"));
        return this.makeRequest(RequestMethod.POST, builder, data, AddedNote.class);
    }

    /** The answer of {@link NotesManager#addAddressNotes(NoteRequest, String, StatusUpdateType)}. */
    @Data
    public static class AddedNote {
        /** Whether the note was added ({@code status}). */
        @SerializedName("status")
        private Boolean status;
        /** The ID of the added note ({@code note_id}). */
        @SerializedName("note_id")
        private String noteId;
        /** The ID of the uploaded file of the note, if any ({@code upload_id}). */
        @SerializedName("upload_id")
        private String uploadId;
        /** The added note ({@code note}). */
        @SerializedName("note")
        private Note note;
    }


}
