package com.route4me.sdk.services.territories;

import com.route4me.sdk.testsupport.Fixtures;
import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.awt.Polygon;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link TerritoriesManager}: territories and avoidance zones on the V4
 * {@code territory.php} and {@code avoidance.php} endpoints, and the local polygon helpers.
 */
public class TerritoriesManagerTest {

    private static final String HOST = "api.route4me.com";
    private static final String TERRITORY_EP = "/api.v4/territory.php";
    private static final String AVOIDANCE_EP = "/api.v4/avoidance.php";

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private TerritoriesManager manager() {
        return http.install(new TerritoriesManager(MockHttp.API_KEY));
    }

    /** Builds a sample polygon territory with fake data. */
    private static Territory polygonTerritory(String id) {
        TerritoryData data = new TerritoryData();
        data.setType("poly");
        data.setData(Arrays.asList("40.0,-75.0", "40.0,-74.0", "41.0,-74.0"));
        Territory territory = new Territory();
        territory.setTerritoryId(id);
        territory.setTerritoryName("Downtown");
        territory.setTerritoryColor("ff0000");
        territory.setTerritory(data);
        return territory;
    }

    // ------------------------------------------------------------------ territories

    @Test
    public void addTerritoryPostsTheTerritoryAsJson() throws Exception {
        TerritoriesManager manager = manager();
        http.respondWithFixture(200, "territory.json");

        Territory created = manager.addTerritory(polygonTerritory(null));

        http.request()
                .assertEndpoint("POST", HOST, TERRITORY_EP)
                .assertApiKeyInQuery()
                .assertJson("territory_name", "Downtown")
                .assertJson("territory_color", "ff0000")
                .assertJson("territory.type", "poly")
                .assertJson("territory.data[0]", "40.0,-75.0")
                .assertJsonLacks("territory_id");
        assertEquals("T-POLY", created.getTerritoryId());
        assertEquals("poly", created.getTerritory().getType());
    }

    @Test
    public void getTerritoriesParsesTheList() throws Exception {
        TerritoriesManager manager = manager();
        http.respondWithFixture(200, "territories.json");

        List<Territory> territories = manager.getTerritories();

        http.request().assertEndpoint("GET", HOST, TERRITORY_EP).assertNoBody();
        assertEquals(3, territories.size());
        assertEquals("Airport", territories.get(1).getTerritoryName());
        assertEquals(Arrays.asList(11, 12), territories.get(0).getAddresses());
    }

    @Test
    public void getTerritoryVariantsSetTheIdAndIncludeFlags() throws Exception {
        TerritoriesManager manager = manager();
        http.respondToAll(200, json("{'territory_id':'T-POLY','orders':[21]}"));

        Territory plain = manager.getTerritory("T-POLY");
        manager.getOrdersInTerritory("T-POLY");
        manager.getAddressesInTerritory("T-POLY");
        manager.getTerritory("T-POLY", true, true);

        assertEquals(Collections.singletonList(21), plain.getOrders());
        http.request(0).assertEndpoint("GET", HOST, TERRITORY_EP).assertQuery("territory_id", "T-POLY")
                .assertQuery("orders", "0").assertQuery("addresses", "0");
        http.request(1).assertQuery("orders", "1").assertQuery("addresses", "0");
        http.request(2).assertQuery("orders", "0").assertQuery("addresses", "1");
        http.request(3).assertQuery("orders", "1").assertQuery("addresses", "1");
    }

    @Test
    public void getTerritoryAcceptsNullFlags() throws Exception {
        TerritoriesManager manager = manager();
        http.respondWithFixture(200, "territory.json");

        manager.getTerritory("T-POLY", null, null);

        http.request().assertEndpoint("GET", HOST, TERRITORY_EP).assertQuery("territory_id", "T-POLY")
                .assertNoQuery("orders").assertNoQuery("addresses");
    }

    @Test
    public void deleteTerritorySendsTheIdAndReadsTheStatus() throws Exception {
        TerritoriesManager manager = manager();
        http.respond(200, json("{'status':true}"));

        assertTrue(manager.deleteTerritory("T-POLY"));

        http.request().assertEndpoint("DELETE", HOST, TERRITORY_EP).assertQuery("territory_id",
                "T-POLY").assertNoBody();
    }

    @Test
    public void updateTerritorySendsTheTerritoryAsJson() throws Exception {
        TerritoriesManager manager = manager();
        http.respondWithFixture(200, "territory.json");

        Territory updated = manager.updateTerritory(polygonTerritory("T-POLY"));

        http.request()
                .assertPath(TERRITORY_EP)
                .assertContentType("application/json")
                .assertJson("territory_id", "T-POLY")
                .assertJson("territory_name", "Downtown");
        assertEquals("Downtown", updated.getTerritoryName());
    }

    @Test
    public void updateTerritorySendsPut() throws Exception {
        TerritoriesManager manager = manager();
        http.respondWithFixture(200, "territory.json");

        manager.updateTerritory(polygonTerritory("T-POLY"));

        http.request().assertMethod("PUT");
    }

    @Test
    public void updateTerritoryPutsTheIdOnTheUrl() throws Exception {
        TerritoriesManager manager = manager();
        http.respondWithFixture(200, "territory.json");

        manager.updateTerritory(polygonTerritory("T-POLY"));

        http.request().assertQuery("territory_id", "T-POLY");
    }

    @Test
    public void updatesWithoutAnIdSendNoTerritoryIdParameter() throws Exception {
        TerritoriesManager manager = manager();
        http.respondToAll(200, Fixtures.read("territory.json"));

        manager.updateTerritory(polygonTerritory(null));
        manager.updateAvoidanceZone(polygonTerritory(null));

        http.request(0).assertEndpoint("PUT", HOST, TERRITORY_EP).assertNoQuery("territory_id");
        http.request(1).assertEndpoint("PUT", HOST, AVOIDANCE_EP).assertNoQuery("territory_id");
    }

    @Test
    public void deletesWithAnEmptyResponseReportFalse() throws Exception {
        TerritoriesManager manager = manager();
        http.respondWithoutBody(200).respondWithoutBody(200);

        assertFalse(manager.deleteTerritory("T-POLY"));
        assertFalse(manager.deleteAvoidanceZone("T-POLY"));
    }

    @Test
    public void territoryErrorBecomesApiException() {
        final TerritoriesManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(manager::getTerritories);
    }

    // ------------------------------------------------------------------ avoidance zones

    @Test
    public void addAvoidanceZonePostsTheZoneAsJson() throws Exception {
        TerritoriesManager manager = manager();
        http.respondWithFixture(200, "territory.json");

        Territory created = manager.addAvoidanceZone(polygonTerritory(null));

        http.request().assertEndpoint("POST", HOST, AVOIDANCE_EP).assertJson("territory.type", "poly");
        assertEquals("T-POLY", created.getTerritoryId());
    }

    @Test
    public void getAvoidanceZonesAndZoneById() throws Exception {
        TerritoriesManager manager = manager();
        http.respondWithFixture(200, "territories.json").respondWithFixture(200, "territory.json");

        List<Territory> zones = manager.getAvoidanceZones();
        Territory zone = manager.getAvoidanceZone("T-POLY");

        assertEquals(3, zones.size());
        assertEquals("T-POLY", zone.getTerritoryId());
        http.request(0).assertEndpoint("GET", HOST, AVOIDANCE_EP).assertNoQuery("territory_id");
        http.request(1).assertEndpoint("GET", HOST, AVOIDANCE_EP).assertQuery("territory_id", "T-POLY");
    }

    @Test
    public void deleteAvoidanceZoneSendsTheIdAndReadsTheStatus() throws Exception {
        TerritoriesManager manager = manager();
        http.respond(200, json("{'status':false}"));

        assertFalse(manager.deleteAvoidanceZone("T-POLY"));

        http.request().assertEndpoint("DELETE", HOST, AVOIDANCE_EP).assertQuery("territory_id", "T-POLY");
    }

    @Test
    public void updateAvoidanceZoneSendsTheZoneAsJson() throws Exception {
        TerritoriesManager manager = manager();
        http.respondWithFixture(200, "territory.json");

        assertNotNull(manager.updateAvoidanceZone(polygonTerritory("T-POLY")));

        http.request().assertPath(AVOIDANCE_EP).assertJson("territory_id", "T-POLY");
    }

    @Test
    public void updateAvoidanceZoneSendsPut() throws Exception {
        TerritoriesManager manager = manager();
        http.respondWithFixture(200, "territory.json");

        manager.updateAvoidanceZone(polygonTerritory("T-POLY"));

        http.request().assertMethod("PUT");
    }

    @Test
    public void updateAvoidanceZonePutsTheIdOnTheUrl() throws Exception {
        TerritoriesManager manager = manager();
        http.respondWithFixture(200, "territory.json");

        manager.updateAvoidanceZone(polygonTerritory("T-POLY"));

        http.request().assertQuery("territory_id", "T-POLY");
    }

    @Test
    public void avoidanceZoneErrorBecomesApiException() {
        final TerritoriesManager manager = manager();
        http.respond(500, json("{'errors':['server error']}"));

        http.expectApiException(() -> manager.getAvoidanceZone("T-POLY"));
    }

    // ------------------------------------------------------------------ polygon helpers

    @Test
    public void polygonsAreBuiltOnlyForPolyTerritories() {
        TerritoriesManager manager = manager();
        http.respondWithFixture(200, "territories.json");

        List<TerritoryPolygon> polygons = manager.getPolygonsFromTerritories();

        http.request().assertEndpoint("GET", HOST, TERRITORY_EP);
        assertEquals(1, polygons.size());
        assertEquals("T-POLY", polygons.get(0).getTerritory().getTerritoryId());
        assertEquals(4, polygons.get(0).getPolygon().npoints);
    }

    @Test
    public void polygonLookupReturnsNothingWhenTheRequestFails() {
        TerritoriesManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        assertTrue(manager.getPolygonsFromTerritories().isEmpty());
    }

    @Test
    public void allocateTerritoriesFindsThePolygonsThatContainThePoint() {
        TerritoriesManager manager = manager();
        http.respondWithFixture(200, "territories.json");
        List<TerritoryPolygon> polygons = manager.getPolygonsFromTerritories();
        TerritoryAllocated inside = new TerritoryAllocated();
        inside.setLatitude(40.5);
        inside.setLongitude(-74.5);
        TerritoryAllocated outside = new TerritoryAllocated();
        outside.setLatitude(10.0);
        outside.setLongitude(10.0);

        List<Territory> found = manager.allocateTerritories(inside, polygons);

        assertEquals(1, found.size());
        assertEquals("Downtown", found.get(0).getTerritoryName());
        assertTrue(manager.allocateTerritories(outside, polygons).isEmpty());
        assertTrue(manager.allocateTerritories(null, polygons).isEmpty());
    }

    @Test
    public void territoryPolygonAccessors() {
        Territory territory = polygonTerritory("T1");
        TerritoryPolygon polygon = new TerritoryPolygon(territory, null);
        polygon.setPolygon(new Polygon(new int[] {0, 1, 1}, new int[] {0, 0, 1}, 3));
        polygon.setTerritory(polygonTerritory("T2"));

        assertEquals(3, polygon.getPolygon().npoints);
        assertEquals("T2", polygon.getTerritory().getTerritoryId());
    }

    @Test
    public void territoryAllocatedHasDefaultsForMissingValues() {
        TerritoryAllocated empty = new TerritoryAllocated();

        assertEquals(0.0, empty.getLatitude(), 0.0);
        assertEquals(0.0, empty.getLongitude(), 0.0);
        assertEquals("0", empty.getConfidence());
        assertEquals("invalid", empty.getType());
        assertTrue(empty.getTerritories().isEmpty());
    }
}
