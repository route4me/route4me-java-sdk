package com.route4me.sdk.services;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;
import com.route4me.sdk.services.customers.CustomerManager;
import com.route4me.sdk.services.driverreview.DriverReviewManager;
import com.route4me.sdk.services.facilities.FacilityManager;
import com.route4me.sdk.services.locations.LocationManager;
import com.route4me.sdk.services.notes.NotesManagerV5;
import com.route4me.sdk.services.optimizationprofile.OptimizationProfileManager;
import com.route4me.sdk.services.orders.OrderManagerV5;
import com.route4me.sdk.services.podworkflow.PodWorkflowManager;
import com.route4me.sdk.services.routecustomdata.RouteCustomDataManager;
import com.route4me.sdk.services.routedestinations.RouteDestinationsManager;
import com.route4me.sdk.services.routestatus.RouteStatusManager;
import com.route4me.sdk.services.schedules.SchedulesManager;
import com.route4me.sdk.services.vehicles.Vehicles;
import com.route4me.sdk.services.vehicles.VehiclesManager;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;
import com.route4me.sdk.testsupport.CapturedRequest;
import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Checks that every manager that puts an ID into the URL path encodes it as one path
 * segment, so that an ID such as {@code ../secret} cannot send the request, with the API
 * key, to another endpoint.
 */
public class PathSegmentEncodingTest {

    /** An ID that would climb out of its endpoint if it were not encoded. */
    private static final String ESCAPE = "../secret";
    private static final String V5 = "/modules/api/v5.0";

    @Rule
    public final MockHttp http = new MockHttp();

    /** Calls a manager method; a response that does not parse is fine, only the request matters. */
    private CapturedRequest send(MockHttp.Call call) throws Exception {
        http.respondToAll(200, "{}");
        int before = http.requestCount();
        try {
            call.run();
        } catch (APIException ignored) {
            // "{}" is not the right shape for every method; the request was still sent.
        }
        assertEquals("one request expected", before + 1, http.requestCount());
        return http.lastRequest();
    }

    /** Asserts that the ID stayed one encoded segment of the expected endpoint. */
    private void assertEncoded(String prefix, String suffix, MockHttp.Call call) throws Exception {
        CapturedRequest request = send(call);
        assertEquals(request.toString(), prefix + "/..%2Fsecret" + suffix, request.getUri().getRawPath());
        assertEquals(prefix + "/" + ESCAPE + suffix, request.getPath());
    }

    @Test
    public void vehicleIdsStayInOneSegment() throws Exception {
        final VehiclesManagerV5 v5 = http.install(new VehiclesManagerV5(MockHttp.API_KEY));
        final VehiclesManager v4 = http.install(new VehiclesManager(MockHttp.API_KEY));
        final Vehicles vehicle = new Vehicles();
        vehicle.setVehicleId(ESCAPE);

        assertEncoded(V5 + "/vehicles", "", () -> v5.getVehicleById(ESCAPE));
        assertEncoded(V5 + "/vehicles", "", () -> v5.deleteVehicle(ESCAPE));
        assertEncoded(V5 + "/vehicles/job-tracker/status", "", () -> v5.getVehicleJobStatus(ESCAPE));
        assertEncoded(V5 + "/vehicle-profiles", "", () -> v5.getVehicleProfileById(ESCAPE));
        assertEncoded(V5 + "/vehicle-capacity-profiles", "", () -> v5.deleteVehicleCapacityProfile(ESCAPE));
        assertEncoded("/modules/api/vehicles", "", () -> v4.getVehicle(ESCAPE));
        assertEncoded("/modules/api/vehicles", "", () -> v4.updateVehicle(vehicle));
    }

    @Test
    public void routeIdsStayInOneSegment() throws Exception {
        final RouteStatusManager status = http.install(new RouteStatusManager(MockHttp.API_KEY));
        final RouteCustomDataManager customData = http.install(new RouteCustomDataManager(MockHttp.API_KEY));
        final SchedulesManager schedules = http.install(new SchedulesManager(MockHttp.API_KEY));
        final RouteDestinationsManager destinations = http.install(new RouteDestinationsManager(MockHttp.API_KEY));

        assertEncoded(V5 + "/route-status", "", () -> status.getRouteStatus(ESCAPE));
        assertEncoded(V5 + "/route-status", "/history", () -> status.getRouteStatusHistory(ESCAPE, null, null, null));
        assertEncoded(V5 + "/route-status", "/rollback", () -> status.rollbackRouteStatus(ESCAPE));
        assertEncoded(V5 + "/routes", "/custom-data", () -> customData.getRouteCustomData(ESCAPE));
        assertEncoded(V5 + "/recurring-routes/schedules", "", () -> schedules.getSchedule(ESCAPE));
        assertEncoded(V5 + "/recurring-routes/route-schedules", "/items",
                () -> schedules.deleteRouteScheduleItems(ESCAPE));
        assertEncoded(V5 + "/recurring-routes/route-schedules", "/preview",
                () -> schedules.getRouteSchedulePreview(ESCAPE, "2026-10-01", "2026-10-31"));
        assertEncoded(V5 + "/recurring-routes/scheduled-routes/is-copy", "",
                () -> schedules.isRouteScheduleCopied(ESCAPE));
        assertEncoded(V5 + "/route-destinations", "", () -> destinations.getDestination(ESCAPE));
    }

    @Test
    public void otherIdsStayInOneSegment() throws Exception {
        final CustomerManager customers = http.install(new CustomerManager(MockHttp.API_KEY));
        final DriverReviewManager reviews = http.install(new DriverReviewManager(MockHttp.API_KEY));
        final FacilityManager facilities = http.install(new FacilityManager(MockHttp.API_KEY));
        final LocationManager locations = http.install(new LocationManager(MockHttp.API_KEY));
        final NotesManagerV5 notes = http.install(new NotesManagerV5(MockHttp.API_KEY));
        final OptimizationProfileManager profiles = http.install(new OptimizationProfileManager(MockHttp.API_KEY));
        final OrderManagerV5 orders = http.install(new OrderManagerV5(MockHttp.API_KEY));
        final PodWorkflowManager workflows = http.install(new PodWorkflowManager(MockHttp.API_KEY));
        final AddressBookManagerV5 addressBook = http.install(new AddressBookManagerV5(MockHttp.API_KEY));

        assertEncoded(V5 + "/customers", "", () -> customers.getCustomerById(ESCAPE));
        assertEncoded(V5 + "/driver-reviews", "", () -> reviews.getDriverReviewById(ESCAPE));
        assertEncoded(V5 + "/facilities", "", () -> facilities.getFacility(ESCAPE));
        assertEncoded(V5 + "/address-book/locations/types", "", () -> locations.getLocationTypeById(ESCAPE));
        assertEncoded(V5 + "/notes", "", () -> notes.getNote(ESCAPE));
        assertEncoded(V5 + "/notes/route", "", () -> notes.getNotesByRoute(ESCAPE, null, null));
        assertEncoded(V5 + "/notes/destination", "", () -> notes.getNotesByDestination(ESCAPE, null, null));
        assertEncoded(V5 + "/optimization-profiles", "", () -> profiles.getOptimizationProfileById(ESCAPE));
        assertEncoded(V5 + "/orders-platform", "", () -> orders.getOrder(ESCAPE));
        assertEncoded(V5 + "/workflows", "", () -> workflows.getPodWorkflow(ESCAPE));
        assertEncoded(V5 + "/address-book/job-tracker/status", "", () -> addressBook.getJobStatus(ESCAPE));
    }

    @Test
    public void otherReservedCharactersAreEncodedToo() throws Exception {
        final OrderManagerV5 orders = http.install(new OrderManagerV5(MockHttp.API_KEY));

        CapturedRequest request = send(() -> orders.getOrder("a b?c#d%2Fé"));

        assertEquals(V5 + "/orders-platform/a%20b%3Fc%23d%252F%C3%A9", request.getUri().getRawPath());
        request.assertNoQuery("c");
    }

    @Test
    public void emptyAndDotSegmentsAreRejectedBeforeSending() {
        final CustomerManager customers = http.install(new CustomerManager(MockHttp.API_KEY));

        for (final String id : Arrays.asList(null, "", ".", "..")) {
            http.expectThrows(IllegalArgumentException.class, () -> customers.getCustomerById(id));
        }
        http.assertNoRequests();
    }
}
