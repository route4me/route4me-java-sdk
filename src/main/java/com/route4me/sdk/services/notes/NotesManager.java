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

public class NotesManager extends Manager {
    public static final String ADD_NOTES_EP = "/actions/addRouteNotes.php";
    public static final String GET_NOTES_EP = "/api.v4/address.php";

    public NotesManager(String apiKey) {
        super(apiKey);
    }

    public List<Note> getAddressNotes(String routeId, String routeDestinationId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(GET_NOTES_EP);
        builder.setParameter("route_id", routeId);
        builder.setParameter("route_destination_id", routeDestinationId);
        builder.setParameter("notes", "1");
        return this.makeRequest(RequestMethod.GET, builder, "", Address.class).getNotes();
    }

    public AddedNote addAddressNotes(NoteRequest note, String content, StatusUpdateType updateType) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ADD_NOTES_EP);
        try {
            builder.addParameters(QueryConverter.convertObjectToParameters(note));
        } catch (IllegalAccessException e) {
            throw new APIException("Could not convert parameters to query string.", e);
        }
        List<NameValuePair> data = new ArrayList<>();
        data.add(new BasicNameValuePair("strNoteContents", content));
        data.add(new BasicNameValuePair("strUpdateType", updateType.toString()));
        return this.makeRequest(RequestMethod.POST, builder, data, AddedNote.class);
    }

    @Data
    public static class AddedNote {
        @SerializedName("status")
        private Boolean status;
        @SerializedName("note_id")
        private String noteId;
        @SerializedName("upload_id")
        private String uploadId;
        @SerializedName("note")
        private Note note;
    }


}
