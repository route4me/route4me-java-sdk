package com.route4me.sdk.services.geocoding;

import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.GeoCoordinates;
import com.route4me.sdk.services.routing.Geocodings;
import com.route4me.sdk.testsupport.MockHttp;
import org.apache.http.client.utils.URIBuilder;
import org.junit.Assume;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link GeocodingManager}: single and bulk forward geocoding on
 * {@code /api/address.php}, and the JSON upload that starts a websocket geocoding job.
 *
 * <p>The websocket part itself ({@link GeocoderWebSockets} connecting to
 * {@code validator.route4me.com}) is not exercised: it would open a real connection.
 */
public class GeocodingManagerTest {

    private static final String HOST = "api.route4me.com";
    private static final String ADDRESS_EP = "/api/address.php";
    private static final String DETAILED_RESULT =
            json("[{'name':'100 Example St','type':'street','confidence':'high','coordinates':{'lat':40.1,'lng':-74.1}}]");

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private GeocodingManager manager() {
        return http.install(new GeocodingManager(MockHttp.API_KEY));
    }

    /** Sets the manager's retry count, which only {@code bulkGeocoder} can change. */
    private static void setMaxRetries(GeocodingManager manager, int maxRetries) {
        GeocoderOptions options = new GeocoderOptions(1);
        options.setMaxRetries(maxRetries);
        manager.bulkGeocoder(Collections.<String>emptyList(), options);
    }

    /** Builds a geocoder URL for {@code address}, as {@code ThreadBasedGeocoding} does. */
    private static URIBuilder addressBuilder(String address) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost(HOST);
        builder.setPath(ADDRESS_EP);
        builder.setParameter("address", address);
        builder.setParameter("format", "json");
        return builder;
    }

    // ------------------------------------------------------------------ single geocoding

    @Test
    public void geocodeSendsTheAddressAndParsesTheMatches() throws Exception {
        GeocodingManager manager = manager();
        http.respond(200, DETAILED_RESULT);

        Geocodings[] matches = manager.geocode("100 Example St");

        http.request().assertEndpoint("GET", HOST, ADDRESS_EP)
                .assertQuery("address", "100 Example St")
                .assertQuery("format", "json")
                .assertQuery("detailed", "true")
                .assertApiKeyInQuery()
                .assertNoBody();
        assertEquals(1, matches.length);
        assertEquals("street", matches[0].getType());
        assertEquals(40.1, matches[0].getCoordinates().getLatitude(), 0.0);
    }

    @Test
    public void geocodeErrorBecomesApiException() {
        final GeocodingManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.geocode("100 Example St"));
    }

    @Test
    public void forwardGeocodeRetriesAfterAnErrorAndReturnsTheResult() {
        GeocodingManager manager = manager();
        setMaxRetries(manager, 3);
        http.respond(500, json("{'errors':['temporary failure']}")).respond(200, DETAILED_RESULT);

        Geocodings[] matches = manager.forwardGeocode(addressBuilder("100 Example St"), Geocodings[].class);

        assertEquals(2, http.requestCount());
        assertEquals("high", matches[0].getConfidence());
    }

    @Test
    public void forwardGeocodeGivesUpAfterMaxRetries() {
        GeocodingManager manager = manager();
        setMaxRetries(manager, 2);
        http.respondToAll(500, json("{'errors':['temporary failure']}"));

        assertNull(manager.forwardGeocode(addressBuilder("100 Example St"), Geocodings[].class));
        assertEquals(2, http.requestCount());
    }

    @Test
    public void forwardGeocodeSendsTheApiKeyOncePerRetry() {
        GeocodingManager manager = manager();
        setMaxRetries(manager, 3);
        http.respond(500, json("{'errors':['temporary failure']}")).respond(200, DETAILED_RESULT);

        manager.forwardGeocode(addressBuilder("100 Example St"), Geocodings[].class);

        http.request(0).assertApiKeyInQuery();
        http.request(1).assertApiKeyInQuery();
    }

    @Ignore("bug (unfiled): with the default maxRetries of 0, forwardGeocode never sends a request")
    @Test
    public void forwardGeocodeWithDefaultOptionsSendsOneRequest() {
        GeocodingManager manager = manager();
        http.respond(200, DETAILED_RESULT);

        Geocodings[] matches = manager.forwardGeocode(addressBuilder("100 Example St"), Geocodings[].class);

        assertEquals(1, http.requestCount());
        assertNotNull(matches);
    }

    // ------------------------------------------------------------------ bulk geocoding

    @Test
    public void bulkGeocoderGeocodesValidAddressesAndMarksShortOnesInvalid() {
        GeocodingManager manager = manager();
        http.respondToAll(200, DETAILED_RESULT);
        GeocoderOptions options = new GeocoderOptions(2);
        options.setMaxRetries(1);
        options.setMaxTimeout(5000);

        List<Address> results = manager.bulkGeocoder(Arrays.asList("100 Example St", "ab", null), options);

        assertEquals(3, results.size());
        assertEquals(1, http.requestCount());
        http.request().assertEndpoint("GET", HOST, ADDRESS_EP)
                .assertQuery("address", "100 Example St")
                .assertQuery("format", "json")
                .assertQuery("detailed", "true");
        Address geocoded = findByAddress(results, "100 Example St");
        assertEquals(40.1, geocoded.getLatitude(), 0.0);
        assertEquals(-74.1, geocoded.getLongitude(), 0.0);
        assertEquals("street", geocoded.getGeocodings().get(0).getType());
        assertEquals("invalid", findByAddress(results, "ab").getGeocodings().get(0).getType());
    }

    @Test
    public void bulkGeocoderSimpleModeReadsCoordinatesOnly() {
        GeocodingManager manager = manager();
        http.respondToAll(200, json("{'lat':41.5,'lng':-73.5}"));
        GeocoderOptions options = new GeocoderOptions(1);
        options.setMaxRetries(1);
        options.setDetailed(GeocoderOptions.GeocoderDetails.SIMPLE);
        options.setResponseFormat(GeocoderOptions.GeocoderResponseFormat.JSON);

        List<Address> results = manager.bulkGeocoder(Collections.singletonList("300 Demo Rd"), options);

        http.request().assertQuery("detailed", "false");
        assertEquals(41.5, results.get(0).getLatitude(), 0.0);
        assertEquals(-73.5, results.get(0).getLongitude(), 0.0);
    }

    @Test
    public void bulkGeocoderKeepsTheDefaultWhenGeocodingFails() {
        GeocodingManager manager = manager();
        http.respondToAll(500, json("{'errors':['temporary failure']}"));
        GeocoderOptions options = new GeocoderOptions(1);
        options.setMaxRetries(1);

        List<Address> results = manager.bulkGeocoder(Collections.singletonList("300 Demo Rd"), options);

        assertEquals(1, results.size());
        assertEquals(0.0, results.get(0).getLatitude(), 0.0);
        assertEquals("invalid", results.get(0).getGeocodings().get(0).getType());
    }

    @Ignore("bug (unfiled): bulkGeocoder drops an address when the geocoder returns no match")
    @Test
    public void bulkGeocoderReturnsOneResultPerAddress() {
        GeocodingManager manager = manager();
        http.respondToAll(200, "[]");
        GeocoderOptions options = new GeocoderOptions(1);
        options.setMaxRetries(1);

        List<Address> results = manager.bulkGeocoder(Collections.singletonList("300 Demo Rd"), options);

        assertEquals(1, results.size());
    }

    @Ignore("bug (unfiled): with the default GeocoderOptions (maxRetries 0), bulkGeocoder never sends a request")
    @Test
    public void bulkGeocoderWithDefaultOptionsGeocodes() {
        GeocodingManager manager = manager();
        http.respondToAll(200, DETAILED_RESULT);

        List<Address> results = manager.bulkGeocoder(Collections.singletonList("100 Example St"), new GeocoderOptions());

        assertEquals(1, http.requestCount());
        assertEquals(40.1, results.get(0).getLatitude(), 0.0);
    }

    @Ignore("bug #32: bulkGeocoder busy-waits for its worker threads and burns a CPU core")
    @Test
    public void bulkGeocoderWaitsWithoutSpinning() {
        ThreadMXBean threads = ManagementFactory.getThreadMXBean();
        Assume.assumeTrue("thread CPU time is not available", threads.isCurrentThreadCpuTimeSupported());
        GeocodingManager manager = manager();
        http.respondToAll(200, DETAILED_RESULT).delayResponses(600);
        GeocoderOptions options = new GeocoderOptions(1);
        options.setMaxRetries(1);
        options.setMaxTimeout(5000);

        long wallStart = System.nanoTime();
        long cpuStart = threads.getCurrentThreadCpuTime();
        manager.bulkGeocoder(Collections.singletonList("100 Example St"), options);
        long cpu = threads.getCurrentThreadCpuTime() - cpuStart;
        long wall = System.nanoTime() - wallStart;

        assertTrue("the calling thread used " + cpu / 1_000_000 + " ms of CPU in " + wall / 1_000_000
                + " ms; it should block, not spin", cpu < wall / 4);
    }

    // ------------------------------------------------------------------ JSON upload / websockets

    @Test
    public void jsonUploadGeocoderPostsTheAddressRows() throws Exception {
        GeocodingManager manager = manager();
        http.respond(200, json("{'optimization_problem_id':'OPT-1','temporary_addresses_storage_id':'TMP-1',"
                + "'address_count':2,'status':true}"));
        List<Address> addresses = Arrays.asList(new Address("100 Example St"), new Address("200 Sample Ave"));

        GeocoderResponse response = manager.jsonUploadGeocoder(addresses);

        http.request().assertEndpoint("POST", HOST, "/actions/upload/json-geocode.php")
                .assertContentType("application/json")
                .assertJson("rows[0].address", "100 Example St")
                .assertJson("rows[1].address", "200 Sample Ave");
        assertEquals("TMP-1", response.getTemporaryAddressesStorageID());
        assertEquals(Integer.valueOf(2), response.getAddressCount());
        assertTrue(response.getStatus());
    }

    @Test
    public void websocketsGeocoderReturnsAnIdleGeocoderWhenTheUploadFails() {
        GeocodingManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        GeocoderWebSockets geocoder = manager.websocketsGeocoder(new ArrayList<Address>());

        http.request().assertPath("/actions/upload/json-geocode.php");
        assertNull("no socket may be opened in a unit test", GeocoderWebSockets.geocoderWebSocket);
        assertNull(geocoder.getAddresses());
        assertEquals(0, geocoder.getAddressesCount());
        assertEquals(0, geocoder.getTotalAddresses());
        assertTrue(geocoder.isCompleted());
        assertEquals("Addresses Processed: 0", geocoder.getProgress());
        geocoder.closeGeoCodeSocketIO();
    }

    @Test
    public void optionsAndWorkerExposeTheirSettings() {
        GeocoderOptions options = new GeocoderOptions();
        options.setMaxThreads(3);
        options.setMaxTimeout(750L);

        assertEquals(3, options.getMaxThreads());
        assertEquals(750L, options.getMaxTimeout());
        assertEquals(0, options.getMaxRetries());
        assertEquals("true", GeocoderOptions.GeocoderDetails.DETAILED.toString());
        assertEquals("json", GeocoderOptions.GeocoderResponseFormat.JSON.toString());
        assertEquals("100 Example St", new ThreadBasedGeocoding(manager(), "100 Example St", options).toString());
        GeoCoordinates coordinates = new GeoCoordinates();
        coordinates.setLatitude(1.0);
        assertEquals(1.0, coordinates.getLatitude(), 0.0);
    }

    /** Returns the result for {@code address}, failing when there is none. */
    private static Address findByAddress(List<Address> addresses, String address) {
        for (Address candidate : addresses) {
            if (address.equals(candidate.getAddress())) {
                return candidate;
            }
        }
        throw new AssertionError("No result for " + address + " in " + addresses.size() + " results");
    }
}
