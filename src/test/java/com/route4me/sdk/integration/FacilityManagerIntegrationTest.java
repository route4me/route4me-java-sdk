package com.route4me.sdk.integration;

import com.route4me.sdk.services.facilities.*;
import com.route4me.sdk.exception.APIException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class FacilityManagerIntegrationTest extends BaseIntegrationTest {
    private FacilityManager manager;
    private List<String> createdIds = new ArrayList<>();

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new FacilityManager(API_KEY);
    }

    @After
    public void tearDown() {
        for (String id : createdIds) {
            try { manager.deleteFacility(id); } catch (Exception e) { }
        }
    }

    private FacilityCreateRequest buildRequest(String alias) {
        FacilityCreateRequest request = new FacilityCreateRequest();
        request.setFacilityAlias(alias);
        request.setAddress("123 Test St, New York, NY 10001");
        FacilityCoordinates coords = new FacilityCoordinates();
        coords.setLat(40.7128);
        coords.setLng(-74.0060);
        request.setCoordinates(coords);
        request.setStatus(1);
        return request;
    }

    @Test
    public void testCreateAndGetFacility() throws APIException {
        FacilityResource created = manager.createFacility(buildRequest("SDK Test Facility"));
        assertNotNull(created);
        assertNotNull(created.getFacilityId());
        createdIds.add(created.getFacilityId());

        FacilityResource fetched = manager.getFacility(created.getFacilityId());
        assertNotNull(fetched);
        assertEquals(created.getFacilityId(), fetched.getFacilityId());
    }

    @Test
    public void testGetFacilities() throws APIException {
        FacilityResource created = manager.createFacility(buildRequest("SDK Facilities List"));
        createdIds.add(created.getFacilityId());

        FacilitiesPaginateResource facilities = manager.getFacilities(1, 10, null, null, null);
        assertNotNull(facilities);
    }

    @Test
    public void testUpdateFacility() throws APIException {
        FacilityResource created = manager.createFacility(buildRequest("SDK Update Facility"));
        createdIds.add(created.getFacilityId());

        FacilityUpdateRequest update = new FacilityUpdateRequest();
        update.setFacilityAlias("SDK Updated Facility");
        FacilityResource updated = manager.updateFacility(created.getFacilityId(), update);
        assertNotNull(updated);
    }

    @Test
    public void testDeleteFacility() throws APIException {
        FacilityResource created = manager.createFacility(buildRequest("SDK Delete Facility"));
        manager.deleteFacility(created.getFacilityId());
    }

    @Test
    public void testGetFacilityTypes() throws APIException {
        FacilityTypeCollectionResource types = manager.getFacilityTypes();
        assertNotNull(types);
    }
}
