package com.route4me.sdk.integration;

import com.route4me.sdk.services.optimizationprofile.*;
import com.route4me.sdk.exception.APIException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class OptimizationProfileManagerIntegrationTest extends BaseIntegrationTest {
    private OptimizationProfileManager manager;
    private List<String> createdIds = new ArrayList<>();

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new OptimizationProfileManager(API_KEY);
    }

    @After
    public void tearDown() {
        for (String id : createdIds) {
            try { manager.deleteOptimizationProfile(id); } catch (Exception e) { }
        }
    }

    @Test
    public void testGetOptimizationProfiles() throws APIException {
        OptimizationProfilesResponse profiles = manager.getOptimizationProfiles();
        assertNotNull(profiles);
    }

    @Test
    public void testCreateAndGetOptimizationProfile() throws APIException {
        OptimizationProfileCreateRequest request = new OptimizationProfileCreateRequest();
        request.setProfileName("SDK Test Profile " + System.currentTimeMillis());
        OptimizationProfile created = manager.createOptimizationProfile(request);
        assertNotNull(created);
        assertNotNull(created.getOptimizationProfileId());
        createdIds.add(created.getOptimizationProfileId());

        OptimizationProfile fetched = manager.getOptimizationProfileById(created.getOptimizationProfileId());
        assertNotNull(fetched);
    }

    @Test
    public void testUpdateOptimizationProfile() throws APIException {
        OptimizationProfileCreateRequest request = new OptimizationProfileCreateRequest();
        request.setProfileName("SDK Update Profile " + System.currentTimeMillis());
        OptimizationProfile created = manager.createOptimizationProfile(request);
        createdIds.add(created.getOptimizationProfileId());

        OptimizationProfileCreateRequest update = new OptimizationProfileCreateRequest();
        update.setProfileName("SDK Updated Profile");
        OptimizationProfile updated = manager.updateOptimizationProfile(created.getOptimizationProfileId(), update);
        assertNotNull(updated);
    }

    @Test
    public void testDeleteOptimizationProfile() throws APIException {
        OptimizationProfileCreateRequest request = new OptimizationProfileCreateRequest();
        request.setProfileName("SDK Delete Profile " + System.currentTimeMillis());
        OptimizationProfile created = manager.createOptimizationProfile(request);
        assertNotNull(created.getOptimizationProfileId());
        boolean deleted = manager.deleteOptimizationProfile(created.getOptimizationProfileId());
        assertTrue(deleted);
    }

    @Test
    public void testGetEntitiesTemplate() throws APIException {
        OptimizationProfileSaveEntities template = manager.getEntitiesTemplate();
        assertNotNull(template);
    }
}
