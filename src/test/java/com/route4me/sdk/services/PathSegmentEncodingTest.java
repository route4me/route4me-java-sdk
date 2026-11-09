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
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;
import com.route4me.sdk.testsupport.CapturedRequest;
import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Checks that every manager that puts an ID into the URL path keeps it in one path segment:
 * an ID with a {@code /}, {@code \} or {@code ;}, such as {@code ../secret} or {@code ..;x}, is
 * refused before anything is sent, so it cannot send the request, with the API key, to another
 * endpoint; other reserved characters are percent-encoded.
 */
public class PathSegmentEncodingTest {

    /** An ID that would climb out of its endpoint on a front end that decodes {@code %2F}. */
    private static final String ESCAPE = "../secret";
    /** The same with backslashes, which some servers read as slashes. */
    private static final String BACKSLASH_ESCAPE = "..\\secret";
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

    /** Asserts that the call is refused before anything is sent. */
    private void assertRefused(MockHttp.Call call) {
        int before = http.requestCount();
        IllegalArgumentException error = http.expectThrows(IllegalArgumentException.class, call);
        assertTrue(error.getMessage(), error.getMessage().startsWith("Invalid value for a URL path segment"));
        assertEquals("nothing is sent", before, http.requestCount());
    }

    @Test
    @SuppressWarnings("deprecation") // the deprecated V4 VehiclesManager must refuse them too
    public void vehicleIdsWithASlashAreRefused() throws Exception {
        final VehiclesManagerV5 v5 = http.install(new VehiclesManagerV5(MockHttp.API_KEY));
        // Fully qualified: Java 8 warns on an import of a deprecated class, and no annotation can suppress that.
        final com.route4me.sdk.services.vehicles.VehiclesManager v4 =
                http.install(new com.route4me.sdk.services.vehicles.VehiclesManager(MockHttp.API_KEY));
        final Vehicles vehicle = new Vehicles();
        vehicle.setVehicleId(ESCAPE);

        assertRefused(() -> v5.getVehicleById(ESCAPE));
        assertRefused(() -> v5.deleteVehicle(ESCAPE));
        assertRefused(() -> v5.getVehicleJobStatus(ESCAPE));
        assertRefused(() -> v5.getVehicleProfileById(ESCAPE));
        assertRefused(() -> v5.deleteVehicleCapacityProfile(ESCAPE));
        assertRefused(() -> v4.getVehicle(ESCAPE));
        assertRefused(() -> v4.updateVehicle(vehicle));
    }

    @Test
    public void routeIdsWithASlashAreRefused() throws Exception {
        final RouteStatusManager status = http.install(new RouteStatusManager(MockHttp.API_KEY));
        final RouteCustomDataManager customData = http.install(new RouteCustomDataManager(MockHttp.API_KEY));
        final SchedulesManager schedules = http.install(new SchedulesManager(MockHttp.API_KEY));
        final RouteDestinationsManager destinations = http.install(new RouteDestinationsManager(MockHttp.API_KEY));

        assertRefused(() -> status.getRouteStatus(ESCAPE));
        assertRefused(() -> status.getRouteStatusHistory(ESCAPE, null, null, null));
        assertRefused(() -> status.rollbackRouteStatus(ESCAPE));
        assertRefused(() -> customData.getRouteCustomData(ESCAPE));
        assertRefused(() -> schedules.getSchedule(ESCAPE));
        assertRefused(() -> schedules.deleteRouteScheduleItems(ESCAPE));
        assertRefused(() -> schedules.getRouteSchedulePreview(ESCAPE, "2026-10-01", "2026-10-31"));
        assertRefused(() -> schedules.isRouteScheduleCopied(ESCAPE));
        assertRefused(() -> destinations.getDestination(ESCAPE));
    }

    @Test
    public void otherIdsWithASlashAreRefused() throws Exception {
        final CustomerManager customers = http.install(new CustomerManager(MockHttp.API_KEY));
        final DriverReviewManager reviews = http.install(new DriverReviewManager(MockHttp.API_KEY));
        final FacilityManager facilities = http.install(new FacilityManager(MockHttp.API_KEY));
        final LocationManager locations = http.install(new LocationManager(MockHttp.API_KEY));
        final NotesManagerV5 notes = http.install(new NotesManagerV5(MockHttp.API_KEY));
        final OptimizationProfileManager profiles = http.install(new OptimizationProfileManager(MockHttp.API_KEY));
        final OrderManagerV5 orders = http.install(new OrderManagerV5(MockHttp.API_KEY));
        final PodWorkflowManager workflows = http.install(new PodWorkflowManager(MockHttp.API_KEY));
        final AddressBookManagerV5 addressBook = http.install(new AddressBookManagerV5(MockHttp.API_KEY));

        assertRefused(() -> customers.getCustomerById(ESCAPE));
        assertRefused(() -> reviews.getDriverReviewById(ESCAPE));
        assertRefused(() -> facilities.getFacility(ESCAPE));
        assertRefused(() -> locations.getLocationTypeById(ESCAPE));
        assertRefused(() -> notes.getNote(ESCAPE));
        assertRefused(() -> notes.getNotesByRoute(ESCAPE, null, null));
        assertRefused(() -> notes.getNotesByDestination(ESCAPE, null, null));
        assertRefused(() -> profiles.getOptimizationProfileById(ESCAPE));
        assertRefused(() -> orders.getOrder(ESCAPE));
        assertRefused(() -> workflows.getPodWorkflow(ESCAPE));
        assertRefused(() -> addressBook.getJobStatus(ESCAPE));
    }

    @Test
    public void otherReservedCharactersAreEncodedToo() throws Exception {
        final OrderManagerV5 orders = http.install(new OrderManagerV5(MockHttp.API_KEY));

        CapturedRequest request = send(() -> orders.getOrder("a b?c#d%2Fé"));

        assertEquals(V5 + "/orders-platform/a%20b%3Fc%23d%252F%C3%A9", request.getUri().getRawPath());
        request.assertNoQuery("c");
    }

    /**
     * A {@code %2F} typed as text is left alone: its {@code %} is encoded, so it is sent as
     * {@code %252F}. A front end that decodes the path once reads the text {@code ..%2Fsecret}, one
     * segment, not {@code ../secret}; only a front end that decodes twice would see a slash.
     */
    @Test
    public void anEncodedSlashTypedAsTextIsSentAsOneHarmlessSegment() throws Exception {
        final OrderManagerV5 orders = http.install(new OrderManagerV5(MockHttp.API_KEY));

        CapturedRequest request = send(() -> orders.getOrder("..%2Fsecret"));

        assertEquals(V5 + "/orders-platform/..%252Fsecret", request.getUri().getRawPath());
        assertEquals(V5 + "/orders-platform/..%2Fsecret", request.getUri().getPath());
    }

    @Test
    public void emptyDotAndSeparatorSegmentsAreRejectedBeforeSending() {
        final CustomerManager customers = http.install(new CustomerManager(MockHttp.API_KEY));

        for (final String id : Arrays.asList(null, "", ".", "..", "/", "\\", "a/b", "a\\b", ESCAPE, BACKSLASH_ESCAPE,
                ";", "a;b", "..;x", ".;", "..;")) {
            http.expectThrows(IllegalArgumentException.class, () -> customers.getCustomerById(id));
        }
        http.assertNoRequests();
    }
}
