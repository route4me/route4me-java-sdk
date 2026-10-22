package com.route4me.sdk.services.trackinghistory;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link TrackingHistoryManager}: the V5 tracking history export.
 */
public class TrackingHistoryManagerTest {

    private static final String HOST = "wh.route4me.com";
    private static final String EXPORT_EP = "/modules/api/v5.0/tracking-history/export";

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private TrackingHistoryManager manager() {
        return http.install(new TrackingHistoryManager(MockHttp.API_KEY));
    }

    @Test
    public void exportTrackingHistoryPostsTheRequestAndReturnsTheFile() throws Exception {
        TrackingHistoryManager manager = manager();
        String csv = "lat,lng,timestamp\n40.5,-74.5,1761040000\n";
        http.respond(200, csv, "text/csv");
        TrackingHistoryExportRequest request = new TrackingHistoryExportRequest().setMemberId(1001L)
                .setDeviceId("DEVICE-1").setRouteId("C0FFEE0000000000000000000000000A").setFormat("csv")
                .setTimePeriod("custom").setStartDate(1761000000L).setEndDate(1761090000L).setLastPosition(false)
                .setShowCustomData(true).setShowMapMatchingData(false).setDownload(true)
                .setTimeZone("America/New_York").setColumns(Arrays.asList("lat", "lng", "timestamp"));

        String file = manager.exportTrackingHistory(request);

        http.request().assertEndpoint("POST", HOST, EXPORT_EP).assertApiKeyAsBearer()
                .assertContentType("application/json")
                .assertJson("member_id", 1001)
                .assertJson("device_id", "DEVICE-1")
                .assertJson("route_id", "C0FFEE0000000000000000000000000A")
                .assertJson("format", "csv")
                .assertJson("time_period", "custom")
                .assertJson("start_date", 1761000000)
                .assertJson("end_date", 1761090000)
                .assertJson("last_position", false)
                .assertJson("show_cd", true)
                .assertJson("show_mmd", false)
                .assertJson("download", true)
                .assertJson("tz", "America/New_York")
                .assertJson("columns[2]", "timestamp");
        assertEquals(csv, file);
    }

    @Test
    public void exportWithoutARequestSendsAnEmptyObject() throws Exception {
        TrackingHistoryManager manager = http.install(new TrackingHistoryManager(MockHttp.API_KEY, true));
        http.respond(200, "[]");

        manager.exportTrackingHistory(null);

        http.request().assertEndpoint("POST", HOST, EXPORT_EP).assertQuery("redirect", "0");
        assertEquals(Collections.emptySet(), http.request().jsonKeys());
    }

    @Test
    public void errorBecomesApiException() {
        final TrackingHistoryManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.exportTrackingHistory(new TrackingHistoryExportRequest()));
    }
}
