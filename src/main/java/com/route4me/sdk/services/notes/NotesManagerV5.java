package com.route4me.sdk.services.notes;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

public class NotesManagerV5 extends Manager {
    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String NOTES_EP = V5_BASE + "/notes";
    private static final String NOTES_CUSTOM_TYPES_EP = NOTES_EP + "/custom-types";
    private static final String NOTES_BULK_CREATE_EP = NOTES_EP + "/bulk-create";

    public NotesManagerV5(String apiKey) {
        super(apiKey);
    }

    public NotesManagerV5(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    private static URIBuilder v5Builder(String endpoint, Object... segments) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        Manager.withPathSegments(builder, endpoint, segments);
        return builder;
    }

    public RouteNoteResource createNote(NoteStoreRequest request) throws APIException {
        return this.makeRequest(RequestMethod.POST, v5Builder(NOTES_EP), this.gson.toJson(request),
                RouteNoteResource.class);
    }

    public RouteNoteResource getNote(String noteId) throws APIException {
        return this.makeRequest(RequestMethod.GET, v5Builder(NOTES_EP, noteId), (HttpEntity) null,
                RouteNoteResource.class);
    }

    public RouteNoteResource updateNote(String noteId, NoteUpdateRequest request) throws APIException {
        return this.makeRequest(RequestMethod.POST, v5Builder(NOTES_EP, noteId), this.gson.toJson(request),
                RouteNoteResource.class);
    }

    public DeleteNoteResponse deleteNote(String noteId) throws APIException {
        return this.makeRequest(RequestMethod.DELETE, v5Builder(NOTES_EP, noteId), "", DeleteNoteResponse.class);
    }

    public RouteNoteCollection getNotesByRoute(String routeId, Integer page, Integer perPage) throws APIException {
        URIBuilder builder = v5Builder(NOTES_EP + "/route", routeId);
        if (page != null) {
            builder.setParameter("page", String.valueOf(page));
        }
        if (perPage != null) {
            builder.setParameter("per_page", String.valueOf(perPage));
        }
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, RouteNoteCollection.class);
    }

    public RouteNoteCollection getNotesByDestination(String routeDestinationId, Integer page, Integer perPage)
            throws APIException {
        URIBuilder builder = v5Builder(NOTES_EP + "/destination", routeDestinationId);
        if (page != null) {
            builder.setParameter("page", String.valueOf(page));
        }
        if (perPage != null) {
            builder.setParameter("per_page", String.valueOf(perPage));
        }
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, RouteNoteCollection.class);
    }

    public NoteCustomTypeCollection getNoteCustomTypes() throws APIException {
        return this.makeRequest(RequestMethod.GET, v5Builder(NOTES_CUSTOM_TYPES_EP), (HttpEntity) null,
                NoteCustomTypeCollection.class);
    }

    public NoteCustomTypeCollection createNoteCustomType(NoteCustomTypeStoreRequest request) throws APIException {
        return this.makeRequest(RequestMethod.POST, v5Builder(NOTES_CUSTOM_TYPES_EP), this.gson.toJson(request),
                NoteCustomTypeCollection.class);
    }

    public BulkNotesResponse bulkCreateNotes(NoteStoreBulkRequest request) throws APIException {
        return this.makeRequest(RequestMethod.POST, v5Builder(NOTES_BULK_CREATE_EP), this.gson.toJson(request),
                BulkNotesResponse.class);
    }
}
