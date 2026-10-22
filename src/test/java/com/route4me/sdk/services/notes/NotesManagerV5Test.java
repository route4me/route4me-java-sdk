package com.route4me.sdk.services.notes;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link NotesManagerV5}: route notes and note custom types on the V5 API.
 */
public class NotesManagerV5Test {

    private static final String HOST = "wh.route4me.com";
    private static final String NOTES_EP = "/modules/api/v5.0/notes";
    private static final String NOTE = json("{'note_id':9001,'note_uuid':'NOTE-UUID-1','route_id':'ROUTE-A1',"
            + "'route_destination_id':5002,'contents':'Left at the "
                    + "door','activity_type':'dropoff','lat':40.5,'lng':-74.5}");

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private NotesManagerV5 manager() {
        return http.install(new NotesManagerV5(MockHttp.API_KEY));
    }

    /** Builds a sample note store request with fake data. */
    private static NoteStoreRequest storeRequest() {
        NoteStoreRequest request = new NoteStoreRequest();
        request.setRouteId("ROUTE-A1");
        request.setAddressId(5002);
        request.setNoteContents("Left at the door");
        request.setDeviceType("web");
        request.setLat(40.5);
        request.setLng(-74.5);
        request.setActivityType("dropoff");
        return request;
    }

    @Test
    public void createGetUpdateAndDeleteNote() throws Exception {
        NotesManagerV5 manager = manager();
        http.respond(200, NOTE).respond(200, NOTE).respond(200, NOTE)
                .respond(200, json("{'status':true,'note_id':9001}"));
        NoteUpdateRequest update = new NoteUpdateRequest();
        update.setNoteContents("Left with the neighbour");

        RouteNoteResource created = manager.createNote(storeRequest());
        manager.getNote("9001");
        manager.updateNote("9001", update);
        DeleteNoteResponse deleted = manager.deleteNote("9001");

        http.request(0).assertEndpoint("POST", HOST, NOTES_EP).assertApiKeyAsBearer()
                .assertJson("route_id", "ROUTE-A1")
                .assertJson("address_id", 5002)
                .assertJson("strNoteContents", "Left at the door")
                .assertJson("device_type", "web")
                .assertJson("lat", 40.5)
                .assertJson("activity_type", "dropoff");
        http.request(1).assertEndpoint("GET", HOST, NOTES_EP + "/9001").assertNoBody();
        http.request(2).assertEndpoint("POST", HOST, NOTES_EP + "/9001")
                .assertJson("strNoteContents", "Left with the neighbour");
        http.request(3).assertEndpoint("DELETE", HOST, NOTES_EP + "/9001");
        assertEquals("NOTE-UUID-1", created.getNoteUuid());
        assertEquals(Integer.valueOf(9001), deleted.getNoteId());
    }

    @Test
    public void notesByRouteAndByDestinationSendPaging() throws Exception {
        NotesManagerV5 manager = manager();
        http.respondToAll(200, "{\"data\":[" + NOTE + "],\"current_page\":1,\"total\":1,\"per_page\":15}");

        RouteNoteCollection byRoute = manager.getNotesByRoute("ROUTE-A1", 1, 15);
        manager.getNotesByRoute("ROUTE-A1", null, null);
        manager.getNotesByDestination("5002", 2, 5);

        http.request(0).assertEndpoint("GET", HOST, NOTES_EP + "/route/ROUTE-A1")
                .assertQuery("page", "1").assertQuery("per_page", "15");
        http.request(1).assertNoQuery("page").assertNoQuery("per_page");
        http.request(2).assertEndpoint("GET", HOST, NOTES_EP + "/destination/5002")
                .assertQuery("page", "2").assertQuery("per_page", "5");
        assertEquals("Left at the door", byRoute.getData().get(0).getContents());
        assertEquals(Integer.valueOf(1), byRoute.getTotal());
    }

    @Test
    public void noteCustomTypesListAndCreate() throws Exception {
        NotesManagerV5 manager = manager();
        http.respondToAll(200, json("{'data':[{'note_custom_type_id':3,'note_custom_type':'Reason',"
                + "'note_custom_type_values':['Closed','Moved']}]}"));
        NoteCustomTypeStoreRequest request = new NoteCustomTypeStoreRequest();
        request.setNoteCustomType("Reason");
        request.setNoteCustomTypeValues(Arrays.asList("Closed", "Moved"));

        NoteCustomTypeCollection types = manager.getNoteCustomTypes();
        manager.createNoteCustomType(request);

        http.request(0).assertEndpoint("GET", HOST, NOTES_EP + "/custom-types");
        http.request(1).assertEndpoint("POST", HOST, NOTES_EP + "/custom-types")
                .assertJson("note_custom_type", "Reason")
                .assertJson("note_custom_type_values[1]", "Moved");
        assertEquals("Moved", types.getData().get(0).getNoteCustomTypeValues().get(1));
    }

    @Test
    public void bulkCreateNotesPostsTheNotes() throws Exception {
        NotesManagerV5 manager = manager();
        http.respond(200, json("{'status':true,'async':false}"));
        NoteStoreBulkRequest request = new NoteStoreBulkRequest();
        request.setNotes(Collections.singletonList(storeRequest()));

        BulkNotesResponse response = manager.bulkCreateNotes(request);

        http.request().assertEndpoint("POST", HOST, NOTES_EP + "/bulk-create")
                .assertJson("notes[0].strNoteContents", "Left at the door");
        assertTrue(response.getStatus());
    }

    @Test
    public void disabledRedirectsAddRedirectZero() throws Exception {
        NotesManagerV5 manager = http.install(new NotesManagerV5(MockHttp.API_KEY, true));
        http.respond(200, NOTE);

        manager.getNote("9001");

        http.request().assertQuery("redirect", "0");
    }

    @Test
    public void errorBecomesApiException() {
        final NotesManagerV5 manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getNote("9001"));
    }
}
