package com.route4me.sdk.services.notes;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.queryconverter.QueryConverter;
import com.route4me.sdk.services.routing.Address;
import lombok.Data;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Reads and adds the notes of route destinations, and manages the custom note types, through the V4 API
 * ({@code https://api.route4me.com}). {@link NotesManagerV5} does the same through the V5 API.
 */
public class NotesManager extends Manager {
    /** The path that adds a note to a route destination, on {@code api.route4me.com}. */
    public static final String ADD_NOTES_EP = "/actions/addRouteNotes.php";
    /** The path that reads a route destination with its notes, on {@code api.route4me.com}. */
    public static final String GET_NOTES_EP = "/api.v4/address.php";
    /** The path of the custom note types, on {@code api.route4me.com}. */
    public static final String CUSTOM_NOTE_TYPES_EP = "/api.v4/note_custom_types.php";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as the {@code api_key} query parameter
     */
    public NotesManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Gets the notes of a route destination: {@code GET https://api.route4me.com/api.v4/address.php?notes=1}.
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
     * Adds a note to a route destination: {@code POST https://api.route4me.com/actions/addRouteNotes.php}, with the
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

    /**
     * Lists the custom note types of the account: {@code GET https://api.route4me.com/api.v4/note_custom_types.php},
     * as the C# SDK ({@code GetAllCustomNoteTypes}) sends it. The two specs of the endpoint disagree on the answer, a
     * list or {@code {"data": [...]}}; this method reads both.
     *
     * @return the custom note types; empty when the account has none
     * @throws APIException if the request fails, the server answers with an error status or the answer is neither
     *     shape
     */
    public List<CustomNoteType> getAllCustomNoteTypes() throws APIException {
        URIBuilder builder = Manager.defaultBuilder(CUSTOM_NOTE_TYPES_EP);
        JsonElement answer = this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, JsonElement.class);
        JsonElement list = answer;
        if (answer != null && answer.isJsonObject()) {
            list = answer.getAsJsonObject().get("data");
        }
        if (answer == null || list == null || list.isJsonNull()) {
            return Collections.emptyList();
        }
        if (!list.isJsonArray()) {
            throw new APIException("The custom note types answer is neither a list nor {\"data\": [...]}");
        }
        return new ArrayList<>(Arrays.asList(this.gson.fromJson(list, CustomNoteType[].class)));
    }

    /**
     * Adds a custom note type: {@code POST https://api.route4me.com/api.v4/note_custom_types.php}, with
     * {@code {"type": ..., "values": [...]}} as the JSON body, as the C# and PHP SDKs send it (the API rejects the V4
     * spec's {@code note_custom_type} and {@code note_custom_type_values}).
     *
     * @param request the name, the values and the kind of field of the type; the name ({@code type}) and at least one
     *     value ({@code values}) are required
     * @return {@code result} {@code OK} and the number of types added ({@code affected})
     * @throws IllegalArgumentException if {@code request} is {@code null}, or has no name or no values; nothing is sent
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public CustomNoteTypeResponse addCustomNoteType(CustomNoteTypeRequest request) throws APIException {
        if (request == null || request.getNoteCustomType() == null || request.getNoteCustomType().isEmpty()) {
            throw new IllegalArgumentException("A custom note type needs a name (type)");
        }
        if (request.getNoteCustomTypeValues() == null || request.getNoteCustomTypeValues().isEmpty()) {
            throw new IllegalArgumentException("A custom note type needs at least one value (values)");
        }
        URIBuilder builder = Manager.defaultBuilder(CUSTOM_NOTE_TYPES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), CustomNoteTypeResponse.class);
    }

    /**
     * Deletes a custom note type: {@code DELETE https://api.route4me.com/api.v4/note_custom_types.php}, with
     * {@code {"id": customNoteTypeId}} as the JSON body, as the V4 spec and the C# SDK
     * ({@code RemoveCustomNoteType}) send it.
     *
     * @param customNoteTypeId the custom note type ID ({@code note_custom_type_id})
     * @return {@code result} {@code OK} and the number of types deleted
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public CustomNoteTypeResponse removeCustomNoteType(long customNoteTypeId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(CUSTOM_NOTE_TYPES_EP);
        String body = this.gson.toJson(Collections.singletonMap("id", customNoteTypeId));
        return this.makeRequest(RequestMethod.DELETE, builder, body, CustomNoteTypeResponse.class);
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
