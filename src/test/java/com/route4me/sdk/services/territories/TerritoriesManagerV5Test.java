package com.route4me.sdk.services.territories;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link TerritoriesManagerV5}: V5 territories, avoidance zones and automatic territory jobs.
 */
public class TerritoriesManagerV5Test {

    private static final String HOST = "wh.route4me.com";
    private static final String V5 = "/modules/api/v5.0";
    private static final String JOB_ID = "C0FFEE0000000000000000000000000A";
    private static final String ZONE = json("{'territory_id':'C0FFEE00000000000000000000000001',"
            + "'territory_name':'Downtown','territory_color':'ff0000','member_id':1001,"
            + "'territory':{'type':'circle','data':['40.5,-74.5','5000']},'addresses_count':3,'orders_count':2}");

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private TerritoriesManagerV5 manager() {
        return http.install(new TerritoriesManagerV5(MockHttp.API_KEY));
    }

    @Test
    public void getTerritoriesSendsTheQueryWithBooleansAsOneOrZero() throws Exception {
        TerritoriesManagerV5 manager = manager();
        http.respondToAll(200, "{\"results\":[" + ZONE + "]}");
        TerritoriesQuery query = new TerritoriesQuery().setLat(40.5).setLng(-74.5).setDate("2026-10-22")
                .setScheduledStart("2026-10-01").setScheduledEnd("2026-10-31").setSearchQuery("Down")
                .setAddresses(true).setOrders(false).setAddressesCount(true).setOrdersCount(true);

        TerritoryResults territories = manager.getTerritories(query);
        manager.getTerritories(null);

        http.request(0).assertEndpoint("GET", HOST, V5 + "/territories").assertApiKeyAsBearer()
                .assertQuery("lat", "40.5")
                .assertQuery("lng", "-74.5")
                .assertQuery("date", "2026-10-22")
                .assertQuery("scheduled_start", "2026-10-01")
                .assertQuery("scheduled_end", "2026-10-31")
                .assertQuery("search_query", "Down")
                .assertQuery("addresses", "1")
                .assertQuery("orders", "0")
                .assertQuery("addresses_count", "1")
                .assertQuery("orders_count", "1")
                .assertNoBody();
        http.request(1).assertNoQuery("lat").assertNoQuery("orders");
        Territory territory = territories.getResults().get(0);
        assertEquals("Downtown", territory.getTerritoryName());
        assertEquals("circle", territory.getTerritory().getType());
        assertEquals(Integer.valueOf(3), territory.getAddressesCount());
        assertEquals(Integer.valueOf(2), territory.getOrdersCount());
    }

    @Test
    public void getTerritoriesNeedsLatAndLngTogether() {
        final TerritoriesManagerV5 manager = manager();

        http.expectThrows(IllegalArgumentException.class,
                () -> manager.getTerritories(new TerritoriesQuery().setLat(40.5)));
        http.expectThrows(IllegalArgumentException.class,
                () -> manager.getAvoidanceZones(null, -74.5, null, null));
        http.assertNoRequests();
    }

    @Test
    public void getAvoidanceZonesSendsOnlyTheGivenFilters() throws Exception {
        TerritoriesManagerV5 manager = manager();
        http.respondToAll(200, "{\"results\":[" + ZONE + "]}");

        TerritoryResults zones = manager.getAvoidanceZones(40.5, -74.5, 1000.0, "Down");
        manager.getAvoidanceZones(null, null, null, null);

        http.request(0).assertEndpoint("GET", HOST, V5 + "/avoidance").assertApiKeyAsBearer()
                .assertQuery("position_lat", "40.5")
                .assertQuery("position_lng", "-74.5")
                .assertQuery("distance", "1000.0")
                .assertQuery("search_query", "Down")
                .assertNoBody();
        http.request(1).assertNoQuery("position_lat").assertNoQuery("position_lng").assertNoQuery("distance")
                .assertNoQuery("search_query");
        assertEquals("ff0000", zones.getResults().get(0).getTerritoryColor());
    }

    @Test
    public void createAutomaticTerritoriesJobPostsTheAddresses() throws Exception {
        TerritoriesManagerV5 manager = manager();
        http.respond(202, "{}").respondWithoutBody(202);
        AutomaticTerritoriesJobRequest request = new AutomaticTerritoriesJobRequest();
        request.setAddresses(Arrays.asList(new AutomaticTerritoriesAddress("A1", 40.5, -74.5),
                new AutomaticTerritoriesAddress(2, 40.6, -74.6)));
        request.setMode(1);
        request.setParams(Collections.singletonList("4"));
        AutomaticTerritoriesJobRequest byStorage = new AutomaticTerritoriesJobRequest();
        byStorage.setTemporaryAddressesStorageId("STORAGE-1");

        String body = manager.createAutomaticTerritoriesJob(request);
        String empty = manager.createAutomaticTerritoriesJob(byStorage);

        http.request(0).assertEndpoint("POST", HOST, V5 + "/automatic-territories").assertApiKeyAsBearer()
                .assertJson("addresses[0].id", "A1")
                .assertJson("addresses[0].lat", 40.5)
                .assertJson("addresses[1].id", 2)
                .assertJson("mode", 1)
                .assertJson("params[0]", "4")
                .assertJsonLacks("temporary_addresses_storage_id");
        http.request(1).assertJson("temporary_addresses_storage_id", "STORAGE-1").assertJsonLacks("addresses");
        assertEquals("{}", body);
        assertEquals("", empty);
    }

    @Test
    public void createAutomaticTerritoriesJobChecksTheRequiredFields() {
        final TerritoriesManagerV5 manager = manager();
        final AutomaticTerritoriesJobRequest empty = new AutomaticTerritoriesJobRequest();
        final AutomaticTerritoriesJobRequest noId = new AutomaticTerritoriesJobRequest();
        noId.setAddresses(Collections.singletonList(new AutomaticTerritoriesAddress(null, 40.5, -74.5)));

        http.expectThrows(IllegalArgumentException.class, () -> manager.createAutomaticTerritoriesJob(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.createAutomaticTerritoriesJob(empty));
        http.expectThrows(IllegalArgumentException.class, () -> manager.createAutomaticTerritoriesJob(noId));
        http.assertNoRequests();
    }

    @Test
    public void automaticTerritoriesJobStatusAndResultPutTheIdInThePath() throws Exception {
        TerritoriesManagerV5 manager = manager();
        http.respond(200, json("{'status':'processing'}"))
                .respond(200, json("{'job_id':'" + JOB_ID + "','clusters':[{'cluster_id':'CL-1',"
                        + "'addresses_count':2,'centroid':{'lat':40.55,'lng':-74.55},"
                        + "'shape':{'type':'Feature','geometry':{'type':'Polygon'}}}]}"));

        AutomaticTerritoriesJobStatus status = manager.getAutomaticTerritoriesJobStatus(JOB_ID);
        AutomaticTerritoriesJobResult result = manager.getAutomaticTerritoriesJobResult(JOB_ID);

        http.request(0).assertEndpoint("GET", HOST, V5 + "/automatic-territories/job-tracker/status/" + JOB_ID)
                .assertApiKeyAsBearer().assertNoBody();
        http.request(1).assertEndpoint("GET", HOST, V5 + "/automatic-territories/job-tracker/result/" + JOB_ID);
        assertEquals("processing", status.getStatus());
        assertEquals(JOB_ID, result.getJobId());
        AutomaticTerritoriesJobResult.Cluster cluster = result.getClusters().get(0);
        assertEquals("CL-1", cluster.getClusterId());
        assertEquals(Integer.valueOf(2), cluster.getAddressesCount());
        assertEquals(40.55, cluster.getCentroid().getLat(), 0.0);
        assertEquals("Feature", cluster.getShape().getType());
        assertEquals("Polygon", cluster.getShape().getGeometry().get("type"));
    }

    @Test
    public void jobIdsAreRejectedWhenEmptyAndEncodedWhenTheyHaveASlash() throws Exception {
        final TerritoriesManagerV5 manager = manager();

        for (final String id : Arrays.asList(null, "", ".", "..")) {
            http.expectThrows(IllegalArgumentException.class, () -> manager.getAutomaticTerritoriesJobStatus(id));
            http.expectThrows(IllegalArgumentException.class, () -> manager.getAutomaticTerritoriesJobResult(id));
        }
        http.assertNoRequests();

        http.respondToAll(200, "{}");
        manager.getAutomaticTerritoriesJobStatus("JOB/../x");
        manager.getAutomaticTerritoriesJobResult("JOB/1");
        assertEquals(V5 + "/automatic-territories/job-tracker/status/JOB%2F..%2Fx", http.request(0).getUri()
                .getRawPath());
        assertEquals(V5 + "/automatic-territories/job-tracker/result/JOB%2F1", http.request(1).getUri()
                .getRawPath());
    }

    @Test
    public void disabledRedirectsAddRedirectZero() throws Exception {
        TerritoriesManagerV5 manager = http.install(new TerritoriesManagerV5(MockHttp.API_KEY, true));
        http.respond(200, json("{'results':[]}"));

        manager.getTerritories(null);

        http.request().assertQuery("redirect", "0");
    }

    @Test
    public void errorBecomesApiException() {
        final TerritoriesManagerV5 manager = manager();
        http.respondWithFixture(401, "error_400.json");

        http.expectApiException(() -> manager.getAvoidanceZones(null, null, null, null));
    }
}
