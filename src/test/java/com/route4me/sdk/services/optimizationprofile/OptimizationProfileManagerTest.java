package com.route4me.sdk.services.optimizationprofile;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link OptimizationProfileManager}: optimization profiles and their
 * entities on the V5 API.
 */
public class OptimizationProfileManagerTest {

    private static final String HOST = "wh.route4me.com";
    private static final String PROFILES_EP = "/modules/api/v5.0/optimization-profiles";
    private static final String PROFILE = json("{'optimization_profile_id':'PROF-1','profile_name':'Default',"
            + "'is_default':true,'avoid_tolls':true,'max_stops_count':40,'speed_cap':55.5}");
    private static final String ENTITIES = json("{'items':[{'guid':'G-1','id':'E-1','parts':"
            + "[{'guid':'P-1','data':{'max_stops':40},'config':{'visible':true}}]}]}");

    @Rule
    public final MockHttp http = new MockHttp();

    private OptimizationProfileManager manager() {
        return http.install(new OptimizationProfileManager(MockHttp.API_KEY));
    }

    private static OptimizationProfileCreateRequest createRequest() {
        OptimizationProfileCreateRequest request = new OptimizationProfileCreateRequest();
        request.setProfileName("Default");
        request.setIsDefault(true);
        request.setAvoidTolls(true);
        request.setMaxStopsCount(40);
        request.setAvoidanceZonesIds(Collections.singletonList("ZONE-1"));
        request.setOptimizationType("distance");
        return request;
    }

    @Test
    public void getOptimizationProfilesReadsTheDataList() throws Exception {
        OptimizationProfileManager manager = manager();
        http.respond(200, "{\"items\":[" + PROFILE + "]}");

        OptimizationProfilesResponse profiles = manager.getOptimizationProfiles();

        http.request().assertEndpoint("GET", HOST, PROFILES_EP + "/data-list").assertApiKeyInQuery();
        assertEquals("Default", profiles.getItems().get(0).getProfileName());
        assertEquals(55.5, profiles.getItems().get(0).getSpeedCap(), 0.0);
    }

    @Test
    public void getOptimizationProfileByIdPutsTheIdInThePath() throws Exception {
        OptimizationProfileManager manager = manager();
        http.respond(200, PROFILE);

        OptimizationProfile profile = manager.getOptimizationProfileById("PROF-1");

        http.request().assertEndpoint("GET", HOST, PROFILES_EP + "/PROF-1").assertNoBody();
        assertTrue(profile.getIsDefault());
        assertEquals(Integer.valueOf(40), profile.getMaxStopsCount());
    }

    @Test
    public void createAndUpdateSendTheProfileSettings() throws Exception {
        OptimizationProfileManager manager = manager();
        http.respondToAll(200, PROFILE);

        OptimizationProfile created = manager.createOptimizationProfile(createRequest());
        manager.updateOptimizationProfile("PROF-1", createRequest());

        http.request(0).assertEndpoint("POST", HOST, PROFILES_EP)
                .assertJson("profile_name", "Default")
                .assertJson("is_default", true)
                .assertJson("avoid_tolls", true)
                .assertJson("max_stops_count", 40)
                .assertJson("avoidance_zones_ids[0]", "ZONE-1")
                .assertJson("optimization_type", "distance");
        http.request(1).assertEndpoint("PUT", HOST, PROFILES_EP + "/PROF-1").assertJson("profile_name", "Default");
        assertEquals("PROF-1", created.getOptimizationProfileId());
    }

    @Test
    public void deleteOptimizationProfileSendsDelete() throws Exception {
        OptimizationProfileManager manager = manager();
        http.respond(200, json("{'status':true}"));

        assertTrue(manager.deleteOptimizationProfile("PROF-1"));

        http.request().assertEndpoint("DELETE", HOST, PROFILES_EP + "/PROF-1").assertNoBody();
    }

    @Test
    public void saveAndDeleteEntitiesPostTheItems() throws Exception {
        OptimizationProfileManager manager = manager();
        http.respondToAll(200, ENTITIES);
        OptimizationProfileSaveEntitiesItemPart part = new OptimizationProfileSaveEntitiesItemPart();
        part.setGuid("P-1");
        part.setData(Collections.<String, Object>singletonMap("max_stops", 40));
        OptimizationProfileSaveEntitiesItem item = new OptimizationProfileSaveEntitiesItem();
        item.setGuid("G-1");
        item.setId("E-1");
        item.setParts(Collections.singletonList(part));
        OptimizationProfileSaveEntities save = new OptimizationProfileSaveEntities();
        save.setItems(Collections.singletonList(item));
        OptimizationProfileDeleteEntitiesRequestItem deleteItem = new OptimizationProfileDeleteEntitiesRequestItem();
        deleteItem.setId("E-1");
        OptimizationProfileDeleteEntitiesRequest delete = new OptimizationProfileDeleteEntitiesRequest();
        delete.setItems(Arrays.asList(deleteItem));

        OptimizationProfileSaveEntities saved = manager.saveEntities(save);
        manager.deleteEntities(delete);

        http.request(0).assertEndpoint("POST", HOST, PROFILES_EP + "/save-entities")
                .assertJson("items[0].guid", "G-1")
                .assertJson("items[0].parts[0].data.max_stops", 40);
        http.request(1).assertEndpoint("POST", HOST, PROFILES_EP + "/delete-entities").assertJson("items[0].id", "E-1");
        assertEquals("P-1", saved.getItems().get(0).getParts().get(0).getGuid());
    }

    @Test
    public void getEntitiesAndTemplate() throws Exception {
        OptimizationProfileManager manager = manager();
        http.respondToAll(200, ENTITIES);

        OptimizationProfileSaveEntities entities = manager.getEntities("PROF-1");
        manager.getEntitiesTemplate();

        http.request(0).assertEndpoint("GET", HOST, PROFILES_EP + "/get-entities")
                .assertQuery("optimization_profile_id", "PROF-1");
        http.request(1).assertEndpoint("GET", HOST, PROFILES_EP + "/get-entities-template");
        assertEquals(Boolean.TRUE, entities.getItems().get(0).getParts().get(0).getConfig().get("visible"));
    }

    @Test
    public void disabledRedirectsAddRedirectZero() throws Exception {
        OptimizationProfileManager manager = http.install(new OptimizationProfileManager(MockHttp.API_KEY, true));
        http.respond(200, PROFILE);

        manager.getOptimizationProfileById("PROF-1");

        http.request().assertQuery("redirect", "0");
    }

    @Test
    public void errorBecomesApiException() {
        final OptimizationProfileManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(manager::getOptimizationProfiles);
    }
}
