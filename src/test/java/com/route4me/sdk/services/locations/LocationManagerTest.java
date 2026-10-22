package com.route4me.sdk.services.locations;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
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

        http.request().assertEndpoint("POST", HOST, LOCATIONS_EP + "/list/combined").assertApiKeyInQuery()
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

        http.request().assertEndpoint("POST", HOST, LOCATIONS_EP + "/list").assertJson("filters.search_query", "Bakery");
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
                .respond(200, json("{'status':true}"));
        LocationExportRequest export = withFilters(new LocationExportRequest());
        export.setColumns(Arrays.asList("address_1", "address_alias"));
        export.setFilename("locations.csv");
        export.setFormat("csv");
        export.setTimezone("UTC");
        export.setAddressIds(Collections.singletonList(12345));

        LocationExportColumnsResource[] columns = manager.getLocationsExportColumns(new LocationCombinedRequest());
        StatusResponse status = manager.exportLocations(export);

        http.request(0).assertEndpoint("POST", HOST, LOCATIONS_EP + "/export/columns");
        http.request(1).assertEndpoint("POST", HOST, LOCATIONS_EP + "/export")
                .assertJson("columns[1]", "address_alias")
                .assertJson("filename", "locations.csv")
                .assertJson("format", "csv")
                .assertJson("tz", "UTC")
                .assertJson("address_ids[0]", 12345);
        assertEquals("Address", columns[0].getFieldTitle());
        assertTrue(status.getStatus());
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
                .assertJson("name", "Store").assertJson("is_child_type", true);
        http.request(4).assertEndpoint("PUT", HOST, LOCATIONS_EP + "/types/LT-1").assertJson("description", "Retail store");
        http.request(5).assertEndpoint("DELETE", HOST, LOCATIONS_EP + "/types/LT-1").assertNoBody();
        assertEquals("Store", types.getData().get(0).getName());
        assertTrue(type.getIsChildType());
        assertTrue(deleted.getStatus());
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
