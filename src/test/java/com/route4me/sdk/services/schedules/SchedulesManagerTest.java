package com.route4me.sdk.services.schedules;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link SchedulesManager}: recurring-route schedules, route schedules,
 * master routes and scheduled-route copies on the V5 API.
 */
public class SchedulesManagerTest {

    private static final String HOST = "wh.route4me.com";
    private static final String BASE = "/modules/api/v5.0/recurring-routes";
    private static final String SCHEDULE_LIST =
            json("{'data':[{'schedule_uid':'SCH-1','name':'Weekdays','timezone':'UTC','advance_interval':7}]}");
    private static final String ROUTE_SCHEDULE_LIST =
            json("{'data':[{'route_id':'ROUTE-A1','schedule_uid':'SCH-1','member_id':1001,'vehicle_id':'VEH-1'}]}");
    private static final String ROUTE_SCHEDULE_RESPONSE = json("{'data':[{'route_id':'ROUTE-A1',"
            + "'route_name':'Morning run','schedule_uid':'SCH-1','schedule':{'name':'Weekdays'}}]}");
    private static final String STATUS_TRUE = json("{'status':true}");

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private SchedulesManager manager() {
        return http.install(new SchedulesManager(MockHttp.API_KEY));
    }

    /** Builds a sample schedule with fake data. */
    private static Schedule schedule() {
        Schedule schedule = new Schedule();
        schedule.setName("Weekdays");
        schedule.setTimezone("America/New_York");
        schedule.setAdvanceInterval(7);
        schedule.setAdvanceScheduleIntervalDays(14);
        schedule.setScheduleBlacklist(
                Collections.singletonList(new ScheduleBlacklistItem(true, "2026-12-25", "2026-12-26")));
        schedule.setScheduleData(Collections.singletonMap("mode", "weekly"));
        return schedule;
    }

    /** Builds a sample replacement schedule with fake data. */
    private static ReplaceRouteScheduleRequest replacement() {
        ReplaceRouteScheduleRequest request = new ReplaceRouteScheduleRequest();
        request.setScheduleData(Collections.singletonList(Collections.singletonMap("mode", "daily")));
        return request;
    }

    /** Builds a sample route schedule with fake data. */
    private static RouteSchedule routeSchedule() {
        RouteSchedule routeSchedule = new RouteSchedule();
        routeSchedule.setRouteId("ROUTE-A1");
        routeSchedule.setScheduleUid("SCH-1");
        routeSchedule.setMemberId(1001L);
        routeSchedule.setVehicleId("VEH-1");
        return routeSchedule;
    }

    // ------------------------------------------------------------------ schedules

    @Test
    public void getSchedulesListsTheSchedules() throws Exception {
        SchedulesManager manager = manager();
        http.respond(200, SCHEDULE_LIST);

        ScheduleList schedules = manager.getSchedules();

        http.request().assertEndpoint("GET", HOST, BASE + "/schedules").assertApiKeyAsBearer().assertNoBody();
        assertEquals("Weekdays", schedules.getData().get(0).getName());
        assertEquals(Integer.valueOf(7), schedules.getData().get(0).getAdvanceInterval());
    }

    @Test
    public void getSchedulesWithPaginationSendsPaging() throws Exception {
        SchedulesManager manager = manager();
        http.respond(200, json("{'data':[{'schedule_uid':'SCH-1'}],'links':{'next':'page-3'},"
                + "'meta':{'current_page':2,'per_page':10,'total':11}}"));

        ScheduleListWithPagination page = manager.getSchedulesWithPagination(2, 10);

        http.request().assertEndpoint("GET", HOST, BASE + "/schedules/pagination")
                .assertQuery("page", "2").assertQuery("per_page", "10").assertQuery("with_pagination", "1");
        assertEquals("SCH-1", page.getData().get(0).getScheduleUid());
        assertEquals("page-3", page.getLinks().getNext());
        assertEquals(Integer.valueOf(11), page.getMeta().getTotal());
    }

    @Test
    public void createScheduleAndUpdateScheduleSendTheSchedule() throws Exception {
        SchedulesManager manager = manager();
        http.respondToAll(200, SCHEDULE_LIST);

        ScheduleList created = manager.createSchedule(schedule());
        manager.updateSchedule("SCH-1", schedule());

        http.request(0).assertEndpoint("POST", HOST, BASE + "/schedules")
                .assertJson("name", "Weekdays")
                .assertJson("timezone", "America/New_York")
                .assertJson("advance_interval", 7)
                .assertJson("advance_schedule_interval_days", 14)
                .assertJson("schedule_blacklist[0].enabled", true)
                .assertJson("schedule_blacklist[0].start", "2026-12-25")
                .assertJson("schedule_blacklist[0].end", "2026-12-26")
                .assertJson("schedule.mode", "weekly");
        http.request(1).assertEndpoint("PUT", HOST, BASE + "/schedules/SCH-1").assertJson("name", "Weekdays");
        assertEquals("SCH-1", created.getData().get(0).getScheduleUid());
    }

    @Test
    public void scheduleBlacklistParsesAsPeriods() throws Exception {
        SchedulesManager manager = manager();
        http.respond(200, json("{'data':[{'schedule_uid':'SCH-1','schedule_blacklist':"
                + "[{'enabled':true,'start':'2026-12-24','end':'2026-12-26'}]}]}"));

        ScheduleList schedules = manager.getSchedule("SCH-1");

        ScheduleBlacklistItem period = schedules.getData().get(0).getScheduleBlacklist().get(0);
        assertTrue(period.getEnabled());
        assertEquals("2026-12-24", period.getStart());
        assertEquals("2026-12-26", period.getEnd());
    }

    @Test
    public void getScheduleAndDeleteScheduleUseTheUid() throws Exception {
        SchedulesManager manager = manager();
        http.respondToAll(200, SCHEDULE_LIST);

        manager.getSchedule("SCH-1");
        manager.deleteSchedule("SCH-1", true);
        manager.deleteSchedule("SCH-1", false);

        http.request(0).assertEndpoint("GET", HOST, BASE + "/schedules/SCH-1");
        http.request(1).assertEndpoint("DELETE", HOST, BASE + "/schedules/SCH-1").assertQuery("with-routes", "1")
                .assertNoBody();
        http.request(2).assertEndpoint("DELETE", HOST, BASE + "/schedules/SCH-1").assertQuery("with-routes", "0");
    }

    // ------------------------------------------------------------------ route schedules

    @Test
    public void getRouteSchedulesWithAndWithoutPagination() throws Exception {
        SchedulesManager manager = manager();
        http.respond(200, ROUTE_SCHEDULE_LIST)
                .respond(200, json("{'data':[{'route_id':'ROUTE-A1'}],'meta':{'total':1},'links':{}}"));

        RouteScheduleList all = manager.getRouteSchedules();
        RouteScheduleListWithPagination page = manager.getRouteSchedulesWithPagination(1, 5);

        http.request(0).assertEndpoint("GET", HOST, BASE + "/route-schedules");
        http.request(1).assertEndpoint("GET", HOST, BASE + "/route-schedules/pagination")
                .assertQuery("page", "1").assertQuery("per_page", "5").assertQuery("with_pagination", "1");
        assertEquals("VEH-1", all.getData().get(0).getVehicleId());
        assertEquals(Integer.valueOf(1), page.getMeta().getTotal());
    }

    @Test
    public void createAndUpdateRouteScheduleSendTheRouteSchedule() throws Exception {
        SchedulesManager manager = manager();
        http.respondToAll(200, ROUTE_SCHEDULE_LIST);

        manager.createRouteSchedule(routeSchedule());
        RouteScheduleList updated = manager.updateRouteSchedule("ROUTE-A1", routeSchedule());

        http.request(0).assertEndpoint("POST", HOST, BASE + "/route-schedules")
                .assertJson("route_id", "ROUTE-A1")
                .assertJson("schedule_uid", "SCH-1")
                .assertJson("member_id", 1001)
                .assertJson("vehicle_id", "VEH-1");
        http.request(1).assertEndpoint("PUT", HOST, BASE + "/route-schedules/ROUTE-A1");
        assertEquals(Long.valueOf(1001), updated.getData().get(0).getMemberId());
    }

    @Test
    public void getRouteScheduleParsesTheScheduleData() throws Exception {
        SchedulesManager manager = manager();
        http.respond(200, ROUTE_SCHEDULE_RESPONSE);

        RouteScheduleResponse response = manager.getRouteSchedule("ROUTE-A1");

        http.request().assertEndpoint("GET", HOST, BASE + "/route-schedules/ROUTE-A1");
        assertEquals("Morning run", response.getData().get(0).getRouteName());
        assertEquals("Weekdays", response.getData().get(0).getSchedule().getName());
    }

    @Test
    public void deleteRouteScheduleAndItems() throws Exception {
        SchedulesManager manager = manager();
        http.respondToAll(200, STATUS_TRUE);

        StatusResponse deleted = manager.deleteRouteSchedule("ROUTE-A1");
        manager.deleteRouteScheduleItems("ROUTE-A1");

        http.request(0).assertEndpoint("DELETE", HOST, BASE + "/route-schedules/ROUTE-A1").assertNoBody();
        http.request(1).assertEndpoint("DELETE", HOST, BASE + "/route-schedules/ROUTE-A1/items");
        assertTrue(deleted.getStatus());
    }

    @Test
    public void getRouteSchedulePreviewSendsTheDateRange() throws Exception {
        SchedulesManager manager = manager();
        http.respond(200, json("{'version':'2','dates':[{'date':'2026-10-22','scheduled':true,"
                + "'created':false,'excluded':false}]}"));

        RouteSchedulePreview preview = manager.getRouteSchedulePreview("ROUTE-A1", "2026-10-22", "2026-10-29");

        http.request().assertEndpoint("GET", HOST, BASE + "/route-schedules/ROUTE-A1/preview")
                .assertQuery("start", "2026-10-22").assertQuery("end", "2026-10-29");
        assertEquals("2026-10-22", preview.getDates().get(0).getDate());
        assertTrue(preview.getDates().get(0).getScheduled());
        assertFalse(preview.getDates().get(0).getExcluded());
    }

    @Test
    public void replaceRouteScheduleSendsTheReplacement() throws Exception {
        SchedulesManager manager = manager();
        http.respond(200, ROUTE_SCHEDULE_RESPONSE);
        ReplaceRouteScheduleRequest request = new ReplaceRouteScheduleRequest();
        request.setScheduleUid("SCH-2");
        request.setName("Weekends");
        request.setMemberId(1001L);
        request.setTimezone("UTC");
        request.setScheduleBlacklist(
                Collections.singletonList(new ScheduleBlacklistItem(true, "2026-12-24", "2026-12-26")));
        request.setScheduleData(Collections.singletonList(Collections.singletonMap("mode", "daily")));
        request.setSync(false);

        manager.replaceRouteSchedule("ROUTE-A1", request);

        http.request().assertEndpoint("PUT", HOST, BASE + "/route-schedules/replace/ROUTE-A1")
                .assertJson("schedule_uid", "SCH-2")
                .assertJson("name", "Weekends")
                .assertJson("member_id", 1001)
                .assertJson("schedule_blacklist[0].enabled", true)
                .assertJson("schedule_blacklist[0].start", "2026-12-24")
                .assertJson("schedule_blacklist[0].end", "2026-12-26")
                .assertJson("schedule[0].mode", "daily")
                .assertJson("sync", false);
    }

    // ------------------------------------------------------------------ master and scheduled routes

    @Test
    public void createMasterRoutePostsTheRequest() throws Exception {
        SchedulesManager manager = manager();
        http.respond(200, json("{'status':true,'data':{'route_id':'ROUTE-M1','schedule_uid':'SCH-1'}}"));
        CreateMasterRouteRequest request = new CreateMasterRouteRequest();
        request.setRouteId("ROUTE-A1");
        request.setRouteName("Master run");
        request.setName("Weekdays");
        request.setTimezone("UTC");
        request.setSync(true);
        request.setScheduleData(Collections.singletonList(Collections.singletonMap("mode", "weekly")));
        request.setScheduleBlacklist(
                Collections.singletonList(new ScheduleBlacklistItem(false, "2026-11-26", "2026-11-27")));

        CreateMasterRouteResponse response = manager.createMasterRoute(request);

        http.request().assertEndpoint("POST", HOST, BASE + "/master-routes")
                .assertJson("route_id", "ROUTE-A1")
                .assertJson("route_name", "Master run")
                .assertJson("sync", true)
                .assertJson("schedule_blacklist[0].enabled", false)
                .assertJson("schedule_blacklist[0].start", "2026-11-26")
                .assertJson("schedule_blacklist[0].end", "2026-11-27");
        assertEquals("ROUTE-M1", response.getData().getRouteId());
    }

    @Test
    public void scheduledRouteCopiesLookups() throws Exception {
        SchedulesManager manager = manager();
        http.respond(200, STATUS_TRUE).respond(200, json("{'parent_route_id':'ROUTE-A1','scheduled_routes':"
                + "[{'schedule_uid':'SCH-1','child_route_id':'ROUTE-C1','route_date':'2026-10-22'}]}"));
        GetRouteScheduleCopiesRequest request = new GetRouteScheduleCopiesRequest();
        request.setRouteId("ROUTE-A1");
        request.setScheduleId("SCH-1");
        request.setRouteDate("2026-10-22");

        StatusResponse copied = manager.isRouteScheduleCopied("ROUTE-C1");
        RouteScheduleCopiesResponse copies = manager.getRouteScheduleCopies(request);

        http.request(0).assertEndpoint("GET", HOST, BASE + "/scheduled-routes/is-copy/ROUTE-C1");
        http.request(1).assertEndpoint("POST", HOST, BASE + "/scheduled-routes/get-copies")
                .assertJson("route_id", "ROUTE-A1")
                .assertJson("schedule_id", "SCH-1")
                .assertJson("route_date", "2026-10-22");
        assertTrue(copied.getStatus());
        assertEquals("ROUTE-C1", copies.getScheduledRoutes().get(0).getChildRouteId());
    }

    // ------------------------------------------------------------------ schedule calendar (V4)

    @Test
    public void getScheduleCalendarSendsTheV4QueryWithFlagsAsOneOrZero() throws Exception {
        SchedulesManager manager = manager();
        http.respondToAll(200, json("{'address_book':{'2026-10-22':3},'orders':{'2026-10-22':5},"
                + "'routes_count':{'2026-10-22':2}}"));
        ScheduleCalendarRequest request = new ScheduleCalendarRequest();
        request.setDateFromString("2026-10-20");
        request.setDateToString("2026-10-26");
        request.setMemberId(1001L);
        request.setTimezoneOffsetMinutes(-240);
        request.setOrders(true);
        request.setAddressBook(false);
        request.setRoutesCount(true);

        ScheduleCalendarResponse calendar = manager.getScheduleCalendar(request);
        manager.getScheduleCalendar(new ScheduleCalendarRequest());

        http.request(0).assertEndpoint("GET", "api.route4me.com", "/api/schedule_calendar_data.php")
                .assertApiKeyInQuery()
                .assertQuery("date_from_string", "2026-10-20")
                .assertQuery("date_to_string", "2026-10-26")
                .assertQuery("member_id", "1001")
                .assertQuery("timezone_offset_minutes", "-240")
                .assertQuery("orders", "1")
                .assertQuery("ab", "0")
                .assertQuery("routes_count", "1")
                .assertNoBody();
        http.request(1).assertNoQuery("date_from_string").assertNoQuery("orders").assertNoQuery("ab");
        assertEquals(Integer.valueOf(3), calendar.getAddressBook().get("2026-10-22"));
        assertEquals(Integer.valueOf(5), calendar.getOrders().get("2026-10-22"));
        assertEquals(Integer.valueOf(2), calendar.getRoutesCount().get("2026-10-22"));
    }

    @Test
    public void requiredFieldsAreCheckedBeforeSending() {
        final SchedulesManager manager = manager();
        final Schedule noTimezone = schedule();
        noTimezone.setTimezone(null);
        final Schedule noRules = schedule();
        noRules.setScheduleData(null);
        final RouteSchedule noSchedule = routeSchedule();
        noSchedule.setScheduleUid(null);
        final CreateMasterRouteRequest noRoute = new CreateMasterRouteRequest();
        noRoute.setScheduleData(Collections.singletonList(Collections.singletonMap("mode", "daily")));
        final CreateMasterRouteRequest noMasterRules = new CreateMasterRouteRequest();
        noMasterRules.setRouteId("ROUTE-A1");

        http.expectThrows(IllegalArgumentException.class, () -> manager.createSchedule(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.createSchedule(noTimezone));
        http.expectThrows(IllegalArgumentException.class, () -> manager.createSchedule(noRules));
        http.expectThrows(IllegalArgumentException.class, () -> manager.createRouteSchedule(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.createRouteSchedule(noSchedule));
        http.expectThrows(IllegalArgumentException.class, () -> manager.createMasterRoute(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.createMasterRoute(noRoute));
        http.expectThrows(IllegalArgumentException.class, () -> manager.createMasterRoute(noMasterRules));
        http.expectThrows(IllegalArgumentException.class,
                () -> manager.replaceRouteSchedule("ROUTE-A1", new ReplaceRouteScheduleRequest()));
        http.expectThrows(IllegalArgumentException.class, () -> manager.replaceRouteSchedule("ROUTE-A1", null));
        http.expectThrows(IllegalArgumentException.class,
                () -> manager.getRouteSchedulePreview("ROUTE-A1", null, "2026-10-31"));
        http.expectThrows(IllegalArgumentException.class,
                () -> manager.getRouteSchedulePreview("ROUTE-A1", "2026-10-01", ""));
        http.expectThrows(IllegalArgumentException.class, () -> manager.getRouteScheduleCopies(null));
        http.expectThrows(IllegalArgumentException.class,
                () -> manager.getRouteScheduleCopies(new GetRouteScheduleCopiesRequest()));
        http.assertNoRequests();
    }

    @Test
    public void emptyPathIdsAreRejectedBeforeSending() {
        final SchedulesManager manager = manager();

        for (final String id : Arrays.asList(null, "")) {
            http.expectThrows(IllegalArgumentException.class, () -> manager.getSchedule(id));
            http.expectThrows(IllegalArgumentException.class, () -> manager.updateSchedule(id, schedule()));
            http.expectThrows(IllegalArgumentException.class, () -> manager.deleteSchedule(id, false));
            http.expectThrows(IllegalArgumentException.class, () -> manager.getRouteSchedule(id));
            http.expectThrows(IllegalArgumentException.class,
                    () -> manager.updateRouteSchedule(id, routeSchedule()));
            http.expectThrows(IllegalArgumentException.class, () -> manager.deleteRouteSchedule(id));
            http.expectThrows(IllegalArgumentException.class, () -> manager.deleteRouteScheduleItems(id));
            http.expectThrows(IllegalArgumentException.class,
                    () -> manager.getRouteSchedulePreview(id, "2026-10-01", "2026-10-31"));
            http.expectThrows(IllegalArgumentException.class,
                    () -> manager.replaceRouteSchedule(id, new ReplaceRouteScheduleRequest()));
            http.expectThrows(IllegalArgumentException.class, () -> manager.isRouteScheduleCopied(id));
        }
        http.assertNoRequests();
    }

    @Test
    public void pathIdsWithASlashStayInOneSegment() throws Exception {
        SchedulesManager manager = manager();
        http.respondToAll(200, STATUS_TRUE);

        manager.updateSchedule("../secret", schedule());
        manager.deleteSchedule("../secret", false);
        manager.getRouteSchedule("../secret");
        manager.updateRouteSchedule("../secret", routeSchedule());
        manager.deleteRouteSchedule("../secret");
        manager.replaceRouteSchedule("../secret", replacement());

        assertEquals(BASE + "/schedules/..%2Fsecret", http.request(0).getUri().getRawPath());
        assertEquals(BASE + "/schedules/..%2Fsecret", http.request(1).getUri().getRawPath());
        assertEquals(BASE + "/route-schedules/..%2Fsecret", http.request(2).getUri().getRawPath());
        assertEquals(BASE + "/route-schedules/..%2Fsecret", http.request(3).getUri().getRawPath());
        assertEquals(BASE + "/route-schedules/..%2Fsecret", http.request(4).getUri().getRawPath());
        assertEquals(BASE + "/route-schedules/replace/..%2Fsecret", http.request(5).getUri().getRawPath());
    }

    @Test
    public void disabledRedirectsAddRedirectZero() throws Exception {
        SchedulesManager manager = http.install(new SchedulesManager(MockHttp.API_KEY, true));
        http.respond(200, SCHEDULE_LIST);

        manager.getSchedules();

        http.request().assertQuery("redirect", "0");
    }

    @Test
    public void errorBecomesApiException() {
        final SchedulesManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getSchedule("SCH-1"));
    }
}
