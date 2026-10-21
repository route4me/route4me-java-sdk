package com.route4me.sdk.services.notes;

import com.route4me.sdk.services.routing.Constants;
import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link NotesManager}: reading and adding route destination notes on the
 * V4 API.
 */
public class NotesManagerTest {

    private static final String HOST = "api.route4me.com";

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private NotesManager manager() {
        return http.install(new NotesManager(MockHttp.API_KEY));
    }

    @Test
    public void getAddressNotesAsksForTheNotesOfOneDestination() throws Exception {
        NotesManager manager = manager();
        http.respond(200, json("{'route_destination_id':5002,'notes':[{'note_id':9001,'route_id':'ROUTE-A1',"
                + "'contents':'Left at the door','activity_type':'dropoff','lat':40.5,'lng':-74.5}]}"));

        List<Note> notes = manager.getAddressNotes("ROUTE-A1", "5002");

        http.request().assertEndpoint("GET", HOST, "/api.v4/address.php").assertApiKeyInQuery()
                .assertQuery("route_id", "ROUTE-A1")
                .assertQuery("route_destination_id", "5002")
                .assertQuery("notes", "1");
        assertEquals(1, notes.size());
        assertEquals("Left at the door", notes.get(0).getContent());
        assertEquals(9001, notes.get(0).getNoteId().intValue());
    }

    @Test
    public void addAddressNotesSendsLocationInTheQueryAndTheNoteAsAForm() throws Exception {
        NotesManager manager = manager();
        http.respond(200, json("{'status':true,'note_id':'9002','note':{'note_id':9002,'contents':'Gate code 1234'}}"));
        NoteRequest request = new NoteRequest().setRouteId("ROUTE-A1").setAddressId("5002")
                .setLatitude(40.5).setLongitude(-74.5).setDeviceType(Constants.DeviceType.WEB);

        NotesManager.AddedNote added = manager.addAddressNotes(request, "Gate code 1234", StatusUpdateType.DROPOFF);

        http.request().assertEndpoint("POST", HOST, "/actions/addRouteNotes.php")
                .assertQuery("route_id", "ROUTE-A1")
                .assertQuery("address_id", "5002")
                .assertQuery("dev_lat", "40.5")
                .assertQuery("dev_lng", "-74.5")
                .assertQuery("device_type", "web")
                .assertContentType("application/x-www-form-urlencoded")
                .assertForm("strNoteContents", "Gate code 1234")
                .assertForm("strUpdateType", "dropoff");
        assertTrue(added.getStatus());
        assertEquals("9002", added.getNoteId());
        assertEquals("Gate code 1234", added.getNote().getContent());
    }

    @Test
    public void errorBecomesApiException() {
        final NotesManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getAddressNotes("ROUTE-A1", "5002"));
    }
}
