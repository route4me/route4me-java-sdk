package com.route4me.sdk.integration;

import com.route4me.sdk.services.territories.*;
import com.route4me.sdk.exception.APIException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class TerritoriesManagerIntegrationTest extends BaseIntegrationTest {
    private TerritoriesManager manager;
    private List<String> createdTerritoryIds = new ArrayList<>();
    private List<String> createdAvoidanceZoneIds = new ArrayList<>();

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new TerritoriesManager(API_KEY);
    }

    @After
    public void tearDown() {
        for (String id : createdTerritoryIds) {
            try { manager.deleteTerritory(id); } catch (Exception e) { }
        }
        for (String id : createdAvoidanceZoneIds) {
            try { manager.deleteAvoidanceZone(id); } catch (Exception e) { }
        }
    }

    private Territory buildTerritory(String name) {
        Territory territory = new Territory();
        territory.setTerritoryName(name);
        territory.setTerritoryColor("ff0000");
        TerritoryData data = new TerritoryData();
        data.setType("circle");
        data.setData(java.util.Arrays.asList("37.7749,-122.4194", "5000"));
        territory.setTerritory(data);
        return territory;
    }

    @Test
    public void testAddAndGetTerritory() throws APIException {
        Territory created = manager.addTerritory(buildTerritory("SDK Test Territory " + System.currentTimeMillis()));
        assertNotNull(created);
        assertNotNull(created.getTerritoryId());
        createdTerritoryIds.add(created.getTerritoryId());

        Territory fetched = manager.getTerritory(created.getTerritoryId());
        assertNotNull(fetched);
        assertEquals(created.getTerritoryId(), fetched.getTerritoryId());
    }

    @Test
    public void testGetTerritories() throws APIException {
        Territory created = manager.addTerritory(buildTerritory("SDK List Territory " + System.currentTimeMillis()));
        createdTerritoryIds.add(created.getTerritoryId());

        List<Territory> territories = manager.getTerritories();
        assertNotNull(territories);
        assertFalse(territories.isEmpty());
    }

    @Test
    public void testUpdateTerritory() throws APIException {
        Territory created = manager.addTerritory(buildTerritory("SDK Update Territory " + System.currentTimeMillis()));
        createdTerritoryIds.add(created.getTerritoryId());

        created.setTerritoryName("SDK Updated Territory");
        Territory updated = manager.updateTerritory(created);
        assertNotNull(updated);
    }

    @Test
    public void testDeleteTerritory() throws APIException {
        Territory created = manager.addTerritory(buildTerritory("SDK Delete Territory " + System.currentTimeMillis()));
        assertNotNull(created.getTerritoryId());
        boolean deleted = manager.deleteTerritory(created.getTerritoryId());
        assertTrue(deleted);
    }

    @Test
    public void testAddAndDeleteAvoidanceZone() throws APIException {
        Territory zone = buildTerritory("SDK Test Avoidance " + System.currentTimeMillis());
        Territory created = manager.addAvoidanceZone(zone);
        assertNotNull(created);
        assertNotNull(created.getTerritoryId());

        boolean deleted = manager.deleteAvoidanceZone(created.getTerritoryId());
        assertTrue(deleted);
    }

    @Test
    public void testGetAvoidanceZones() throws APIException {
        List<Territory> zones = manager.getAvoidanceZones();
        assertNotNull(zones);
    }
}
