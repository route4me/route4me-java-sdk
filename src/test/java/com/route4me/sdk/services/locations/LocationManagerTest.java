package com.route4me.sdk.services.locations;

import com.route4me.sdk.AsyncJob;
import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link LocationManager}: address-book locations, their map views, exports
 * and location types on the V5 API.
 */
public class LocationManagerTest {

    private static final String HOST = "wh.route4me.com";
    private static final String LOCATIONS_EP = "/modules/api/v5.0/address-book/locations";
    private static final String LOCATION_TYPE = json("{'location_type_id':'LT-1','name':'Store',"
            + "'description':'Retail store','is_parent_type':false,'is_child_type':true,'root_member_id':1000}");
    private static final String JOB_ID = "C0FFEE0000000000000000000000000E";
    private static final String EXPORT_STATUS_URL =
            "https://" + HOST + "/modules/api/v5.0/address-book/job-tracker/status/" + JOB_ID;

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private LocationManager manager() {
        return http.install(new LocationManager(MockHttp.API_KEY));
    }

    /** Adds sample filters and paging to a locations request. */
    private static <T extends LocationCombinedRequest> T withFilters(T request) {
        LocationFilters filters = new LocationFilters();
        filters.setSearchQuery("Bakery");
        filters.setIsDepot(Collections.singletonList(false));
        request.setFilters(filters);
        request.setPage(1);
        request.setPerPage(50);
        return request;
    }

    @Test
    public void getLocationsCombinedPostsTheFilters() throws Exception {
        LocationManager manager = manager();
        http.respond(200, json("{'data':{'items':[{'address_id':12345,'address_1':'100 Example St',"
                + "'lat':40.1,'lng':-74.1}],'total_items_count':1,'columns_configurator_key':'locations'},"
                + "'config':{'columns':[{'id':'address_1','label':'Address','sortable':true}]}}"));

        LocationCombinedResource combined = manager.getLocationsCombined(withFilters(new LocationCombinedRequest()));

        http.request().assertEndpoint("POST", HOST, LOCATIONS_EP + "/list/combined").assertApiKeyAsBearer()
                .assertJson("filters.search_query", "Bakery")
                .assertJson("filters.is_depot[0]", false)
                .assertJson("page", 1)
                .assertJson("per_page", 50);
        assertEquals("100 Example St", combined.getData().getItems().get(0).getAddress1());
        assertTrue(combined.getConfig().getColumns().get(0).getSortable());
    }

    @Test
    public void getLocationsListPostsTheFilters() throws Exception {
        LocationManager manager = manager();
        http.respond(200, json("{'items':[{'address_id':12345,'address_alias':'Example Bakery'}],"
                + "'total_items_count':1}"));

        LocationListResource list = manager.getLocationsList(withFilters(new LocationCombinedRequest()));

        http.request().assertEndpoint("POST", HOST, LOCATIONS_EP + "/list").assertJson("filters.search_query",
                "Bakery");
        assertEquals("Example Bakery", list.getItems().get(0).getAddressAlias());
    }

    @Test
    public void getLocationsClusteringSendsTheZoom() throws Exception {
        LocationManager manager = manager();
        http.respond(200, json("{'clusters':[{'cluster':{'lat':40.1,'lng':-74.1,'locations_count':12,"
                + "'bounding_box':{'top':41.0,'left':-75.0,'bottom':40.0,'right':-74.0}}}],'with_clusters':true}"));
        ClusteringParameters clustering = new ClusteringParameters();
        clustering.setZoom(10);
        LocationClusteringRequest request = withFilters(new LocationClusteringRequest());
        request.setClustering(clustering);
        request.setWithClusters(true);

        LocationClusteringResource result = manager.getLocationsClustering(request);

        http.request().assertEndpoint("POST", HOST, LOCATIONS_EP + "/clustering")
                .assertJson("clustering.zoom", 10)
                .assertJson("with_clusters", true)
                .assertJson("filters.search_query", "Bakery");
        assertEquals(Integer.valueOf(12), result.getClusters().get(0).getCluster().getLocationsCount());
        assertEquals(41.0, result.getClusters().get(0).getCluster().getBoundingBox().getTop(), 0.0);
    }

    @Test
    public void getLocationsHeatmapSendsTheZoom() throws Exception {
        LocationManager manager = manager();
        http.respond(200, json("{'data':[[40.1,-74.1,3.0]],'fields':['lat','lng','weight']}"));
        LocationHeatmapRequest request = withFilters(new LocationHeatmapRequest());
        request.setZoom(8);

        LocationHeatmapCollection heatmap = manager.getLocationsHeatmap(request);

        http.request().assertEndpoint("POST", HOST, LOCATIONS_EP + "/heatmap").assertJson("zoom", 8);
        assertEquals(3.0, heatmap.getData().get(0)[2], 0.0);
        assertEquals("weight", heatmap.getFields().get(2));
    }

    @Test
    public void getLocationsTerritoriesSendsTerritoryPaging() throws Exception {
        LocationManager manager = manager();
        http.respond(200, json("{'items':[{'territory_id':'T-1','territory_name':'Downtown','locations_count':4}],"
                + "'current_page_index':1,'total_items_count':1}"));
        LocationTerritoriesRequest request = withFilters(new LocationTerritoriesRequest());
        request.setTerritoriesPage(2);
        request.setTerritoriesPerPage(5);
        request.setTerritoriesSearchQuery("Down");

        LocationTerritoriesCollection territories = manager.getLocationsTerritories(request);

        http.request().assertEndpoint("POST", HOST, LOCATIONS_EP + "/territories")
                .assertJson("territories_page", 2)
                .assertJson("territories_per_page", 5)
                .assertJson("territories_search_query", "Down");
        assertEquals("Downtown", territories.getItems().get(0).getTerritoryName());
    }

    @Test
    public void exportColumnsAndExport() throws Exception {
        LocationManager manager = manager();
        http.respond(200, json("[{'field_name':'address_1','field_title':'Address','group':'main','allowed':true}]"))
                .respondWithHeaders(202, null, "X-Job-Id", JOB_ID, "Location", EXPORT_STATUS_URL);
        LocationExportRequest export = withFilters(new LocationExportRequest());
        export.setColumns(Arrays.asList("address_1", "address_alias"));
        export.setFilename("locations.csv");
        export.setFormat("csv");
        export.setTimezone("UTC");
        export.setAddressIds(Collections.singletonList(12345));

        LocationExportColumnsResource[] columns = manager.getLocationsExportColumns(new LocationCombinedRequest());
        AsyncJob job = manager.exportLocations(export);

        http.request(0).assertEndpoint("POST", HOST, LOCATIONS_EP + "/export/columns");
        http.request(1).assertEndpoint("POST", HOST, LOCATIONS_EP + "/export")
                .assertJson("columns[1]", "address_alias")
                .assertJson("filename", "locations.csv")
                .assertJson("format", "csv")
                .assertJson("tz", "UTC")
                .assertJson("address_ids[0]", 12345);
        assertEquals("Address", columns[0].getFieldTitle());
        assertEquals(202, job.getStatusCode());
        assertEquals(JOB_ID, job.getJobId());
        assertEquals(EXPORT_STATUS_URL, job.getLocation());
        assertEquals("", job.getBody());
    }

    @Test
    public void locationTypesCrud() throws Exception {
        LocationManager manager = manager();
        http.respond(200, "{\"data\":[" + LOCATION_TYPE + "]}").respond(200, json("{'data':[]}"))
                .respond(200, LOCATION_TYPE).respond(200, LOCATION_TYPE).respond(200, LOCATION_TYPE)
                .respond(200, json("{'status':true}"));
        StoreLocationTypeRequest store = new StoreLocationTypeRequest();
        store.setName("Store");
        store.setDescription("Retail store");
        store.setIsParentType(false);
        store.setIsChildType(true);

        LocationTypeCollection types = manager.getLocationTypes(1, 20);
        manager.getLocationTypes(null, null);
        LocationTypeResource type = manager.getLocationTypeById("LT-1");
        manager.createLocationType(store);
        manager.updateLocationType("LT-1", store);
        DeleteLocationTypeResponse deleted = manager.deleteLocationType("LT-1");

        http.request(0).assertEndpoint("GET", HOST, LOCATIONS_EP + "/types")
                .assertQuery("page", "1").assertQuery("per_page", "20");
        http.request(1).assertNoQuery("page").assertNoQuery("per_page");
        http.request(2).assertEndpoint("GET", HOST, LOCATIONS_EP + "/types/LT-1");
        http.request(3).assertEndpoint("POST", HOST, LOCATIONS_EP + "/types")
                .assertJson("name", "Store").assertJson("is_parent_type", false).assertJson("is_child_type", true);
        http.request(4).assertEndpoint("PUT", HOST, LOCATIONS_EP + "/types/LT-1").assertJson("description",
                "Retail store");
        http.request(5).assertEndpoint("DELETE", HOST, LOCATIONS_EP + "/types/LT-1").assertNoBody();
        assertEquals("Store", types.getData().get(0).getName());
        assertTrue(type.getIsChildType());
        assertTrue(deleted.getStatus());
    }

    @Test
    public void getLocationTypesSendsTheFiltersInBrackets() throws Exception {
        LocationManager manager = manager();
        http.respondToAll(200, json("{'data':[]}"));
        LocationTypeFilters filters = new LocationTypeFilters();
        filters.setIsParentType(true);
        filters.setOnlyCustomTypes(false);
        filters.setSearchQuery("Store");

        manager.getLocationTypes(filters, 2, 10);
        manager.getLocationTypes(new LocationTypeFilters(), null, null);

        http.request(0).assertEndpoint("GET", HOST, LOCATIONS_EP + "/types").assertNoBody()
                .assertQuery("filters[is_parent_type]", "1")
                .assertQuery("filters[only_custom_types]", "0")
                .assertQuery("filters[search_query]", "Store")
                .assertNoQuery("filters[is_child_type]")
                .assertQuery("page", "2")
                .assertQuery("per_page", "10");
        assertTrue(http.request(1).getQueryParameters().isEmpty());
    }

    @Test
    public void locationTypeRequiredFieldsAreCheckedBeforeSending() {
        final LocationManager manager = manager();
        final StoreLocationTypeRequest noName = new StoreLocationTypeRequest();
        noName.setIsParentType(true);
        noName.setIsChildType(false);
        final StoreLocationTypeRequest noParent = new StoreLocationTypeRequest();
        noParent.setName("Store");
        noParent.setIsChildType(false);
        final StoreLocationTypeRequest noChild = new StoreLocationTypeRequest();
        noChild.setName("Store");
        noChild.setIsParentType(true);

        http.expectThrows(IllegalArgumentException.class, () -> manager.createLocationType(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.createLocationType(noName));
        http.expectThrows(IllegalArgumentException.class, () -> manager.createLocationType(noParent));
        http.expectThrows(IllegalArgumentException.class, () -> manager.updateLocationType("LT-1", noChild));
        http.assertNoRequests();
    }

    @Test
    public void locationTypeIdsAreRejectedWhenEmptyAndStayInOneSegment() throws Exception {
        final LocationManager manager = manager();
        final StoreLocationTypeRequest store = new StoreLocationTypeRequest();
        store.setName("Store");
        store.setIsParentType(false);
        store.setIsChildType(true);
        http.expectThrows(IllegalArgumentException.class, () -> manager.getLocationTypeById(""));
        http.expectThrows(IllegalArgumentException.class, () -> manager.updateLocationType(null, store));
        http.expectThrows(IllegalArgumentException.class, () -> manager.deleteLocationType(""));
        http.assertNoRequests();
        http.respondToAll(200, LOCATION_TYPE);

        manager.updateLocationType("LT/1", store);
        manager.deleteLocationType("LT/1");

        assertEquals(LOCATIONS_EP + "/types/LT%2F1", http.request(0).getUri().getRawPath());
        assertEquals(LOCATIONS_EP + "/types/LT%2F1", http.request(1).getUri().getRawPath());
    }

    @Test
    public void exportLocationsAcceptsAnEmptyAcceptedReply() throws Exception {
        LocationManager manager = manager();
        http.respondWithoutBody(202);

        AsyncJob job = manager.exportLocations(withFilters(new LocationExportRequest()));

        http.request().assertEndpoint("POST", HOST, LOCATIONS_EP + "/export").assertJson("page", 1);
        assertEquals(202, job.getStatusCode());
        assertNull(job.getJobId());
        assertNull(job.getLocation());
        assertEquals("", job.getBody());
    }

    @Test
    public void combinedFieldsAndClusterThresholdAreSent() throws Exception {
        LocationManager manager = manager();
        http.respondToAll(200, "{}");
        LocationCombinedRequest list = new LocationCombinedRequest();
        list.setFields(Arrays.asList("address_id", "address_1"));
        list.setOrderBy(Collections.singletonList(new String[] {"address_1", "asc"}));
        ClusteringParameters clustering = new ClusteringParameters();
        clustering.setZoom(5);
        clustering.setCountThreshold(3);
        LocationClusteringRequest clusters = new LocationClusteringRequest();
        clusters.setClustering(clustering);

        manager.getLocationsList(list);
        manager.getLocationsClustering(clusters);

        http.request(0).assertJson("fields[1]", "address_1").assertJson("order_by[0][1]", "asc");
        http.request(1).assertJson("clustering.count_threshold", 3).assertJson("clustering.zoom", 5);
    }

    @Test
    public void disabledRedirectsAddRedirectZero() throws Exception {
        LocationManager manager = http.install(new LocationManager(MockHttp.API_KEY, true));
        http.respond(200, LOCATION_TYPE);

        manager.getLocationTypeById("LT-1");

        http.request().assertQuery("redirect", "0");
    }

    @Test
    public void errorBecomesApiException() {
        final LocationManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getLocationsList(new LocationCombinedRequest()));
    }
}
