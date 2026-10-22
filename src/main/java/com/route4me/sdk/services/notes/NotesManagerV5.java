package com.route4me.sdk.services.notes;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 * Manages the notes of routes and destinations through the V5 API ({@code
 * https://wh.route4me.com/modules/api/v5.0/notes}).
 */
public class NotesManagerV5 extends Manager {
    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String NOTES_EP = V5_BASE + "/notes";
    private static final String NOTES_CUSTOM_TYPES_EP = NOTES_EP + "/custom-types";
    private static final String NOTES_BULK_CREATE_EP = NOTES_EP + "/bulk-create";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     */
    public NotesManagerV5(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
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

    /**
     * Adds a note: {@code POST https://wh.route4me.com/modules/api/v5.0/notes}, with the request as the JSON body.
     *
     * @param request the route, destination and text
     * @return the added note
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteNoteResource createNote(NoteStoreRequest request) throws APIException {
        return this.makeRequest(RequestMethod.POST, v5Builder(NOTES_EP), this.gson.toJson(request),
                RouteNoteResource.class);
    }

    /**
     * Gets one note: {@code GET https://wh.route4me.com/modules/api/v5.0/notes/{noteId}}.
     *
     * @param noteId the note ID
     * @return the note
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteNoteResource getNote(String noteId) throws APIException {
        return this.makeRequest(RequestMethod.GET, v5Builder(NOTES_EP, noteId), (HttpEntity) null,
                RouteNoteResource.class);
    }

    /**
     * Updates a note: {@code POST https://wh.route4me.com/modules/api/v5.0/notes/{noteId}}, with the request as the
     * JSON body.
     *
     * @param noteId the note ID
     * @param request the new text or attachment
     * @return the updated note
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public RouteNoteResource updateNote(String noteId, NoteUpdateRequest request) throws APIException {
        return this.makeRequest(RequestMethod.POST, v5Builder(NOTES_EP, noteId), this.gson.toJson(request),
                RouteNoteResource.class);
    }

    /**
     * Deletes a note: {@code DELETE https://wh.route4me.com/modules/api/v5.0/notes/{noteId}}.
     *
     * @param noteId the note ID
     * @return whether it was deleted
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public DeleteNoteResponse deleteNote(String noteId) throws APIException {
        return this.makeRequest(RequestMethod.DELETE, v5Builder(NOTES_EP, noteId), "", DeleteNoteResponse.class);
    }

    /**
     * Lists the notes of a route, one page at a time: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/notes/route/{routeId}}. Every {@code null} page argument
     * is left out of the query.
     *
     * @param routeId the route ID
     * @param page the page number, from 1 ({@code page})
     * @param perPage the number of items per page ({@code per_page})
     * @return the page of notes
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
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

    /**
     * Lists the notes of a route destination, one page at a time: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/notes/destination/{routeDestinationId}}.
     * Every {@code null} page argument is left out of the query.
     *
     * @param routeDestinationId the route destination ID
     * @param page the page number, from 1 ({@code page})
     * @param perPage the number of items per page ({@code per_page})
     * @return the page of notes
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
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

    /**
     * Lists the custom note types: {@code GET https://wh.route4me.com/modules/api/v5.0/notes/custom-types}.
     *
     * @return the custom note types
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public NoteCustomTypeCollection getNoteCustomTypes() throws APIException {
        return this.makeRequest(RequestMethod.GET, v5Builder(NOTES_CUSTOM_TYPES_EP), (HttpEntity) null,
                NoteCustomTypeCollection.class);
    }

    /**
     * Creates a custom note type: {@code POST https://wh.route4me.com/modules/api/v5.0/notes/custom-types}, with the
     * request as the JSON body.
     *
     * @param request the type and its values
     * @return the custom note types
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public NoteCustomTypeCollection createNoteCustomType(NoteCustomTypeStoreRequest request) throws APIException {
        return this.makeRequest(RequestMethod.POST, v5Builder(NOTES_CUSTOM_TYPES_EP), this.gson.toJson(request),
                NoteCustomTypeCollection.class);
    }

    /**
     * Adds several notes: {@code POST https://wh.route4me.com/modules/api/v5.0/notes/bulk-create}, with the request as
     * the JSON body.
     *
     * @param request the notes
     * @return whether the notes were accepted, and whether a background job creates them
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public BulkNotesResponse bulkCreateNotes(NoteStoreBulkRequest request) throws APIException {
        return this.makeRequest(RequestMethod.POST, v5Builder(NOTES_BULK_CREATE_EP), this.gson.toJson(request),
                BulkNotesResponse.class);
    }
}
