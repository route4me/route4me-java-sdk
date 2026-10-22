package com.route4me.sdk.services.locations;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

public class LocationManager extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0/address-book";
    private static final String LOCATIONS_LIST_COMBINED_EP = V5_BASE + "/locations/list/combined";
    private static final String LOCATIONS_LIST_EP = V5_BASE + "/locations/list";
    private static final String LOCATIONS_CLUSTERING_EP = V5_BASE + "/locations/clustering";
    private static final String LOCATIONS_HEATMAP_EP = V5_BASE + "/locations/heatmap";
    private static final String LOCATIONS_TERRITORIES_EP = V5_BASE + "/locations/territories";
    private static final String LOCATIONS_EXPORT_COLUMNS_EP = V5_BASE + "/locations/export/columns";
    private static final String LOCATIONS_EXPORT_EP = V5_BASE + "/locations/export";
    private static final String LOCATION_TYPES_EP = V5_BASE + "/locations/types";

    public LocationManager(String apiKey) {
        super(apiKey);
    }

    public LocationManager(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    private static URIBuilder v5Builder(String endpoint) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        builder.setPath(endpoint);
        return builder;
    }

    public LocationCombinedResource getLocationsCombined(LocationCombinedRequest request) throws APIException {
        URIBuilder builder = v5Builder(LOCATIONS_LIST_COMBINED_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), LocationCombinedResource.class);
    }

    public LocationListResource getLocationsList(LocationCombinedRequest request) throws APIException {
        URIBuilder builder = v5Builder(LOCATIONS_LIST_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), LocationListResource.class);
    }

    public LocationClusteringResource getLocationsClustering(LocationClusteringRequest request) throws APIException {
        URIBuilder builder = v5Builder(LOCATIONS_CLUSTERING_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), LocationClusteringResource.class);
    }

    public LocationHeatmapCollection getLocationsHeatmap(LocationHeatmapRequest request) throws APIException {
        URIBuilder builder = v5Builder(LOCATIONS_HEATMAP_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), LocationHeatmapCollection.class);
    }

    public LocationTerritoriesCollection getLocationsTerritories(LocationTerritoriesRequest request) throws APIException {
        URIBuilder builder = v5Builder(LOCATIONS_TERRITORIES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), LocationTerritoriesCollection.class);
    }

    public LocationExportColumnsResource[] getLocationsExportColumns(LocationCombinedRequest request) throws APIException {
        URIBuilder builder = v5Builder(LOCATIONS_EXPORT_COLUMNS_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), LocationExportColumnsResource[].class);
    }

    public StatusResponse exportLocations(LocationExportRequest request) throws APIException {
        URIBuilder builder = v5Builder(LOCATIONS_EXPORT_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), StatusResponse.class);
    }

    public LocationTypeCollection getLocationTypes(Integer page, Integer perPage) throws APIException {
        URIBuilder builder = v5Builder(LOCATION_TYPES_EP);
        if (page != null) builder.setParameter("page", String.valueOf(page));
        if (perPage != null) builder.setParameter("per_page", String.valueOf(perPage));
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, LocationTypeCollection.class);
    }

    public LocationTypeResource getLocationTypeById(String locationTypeId) throws APIException {
        URIBuilder builder = v5Builder(LOCATION_TYPES_EP + "/" + locationTypeId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, LocationTypeResource.class);
    }

    public LocationTypeResource createLocationType(StoreLocationTypeRequest request) throws APIException {
        URIBuilder builder = v5Builder(LOCATION_TYPES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), LocationTypeResource.class);
    }

    public LocationTypeResource updateLocationType(String locationTypeId, StoreLocationTypeRequest request) throws APIException {
        URIBuilder builder = v5Builder(LOCATION_TYPES_EP + "/" + locationTypeId);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(request), LocationTypeResource.class);
    }

    public DeleteLocationTypeResponse deleteLocationType(String locationTypeId) throws APIException {
        URIBuilder builder = v5Builder(LOCATION_TYPES_EP + "/" + locationTypeId);
        return this.makeRequest(RequestMethod.DELETE, builder, "", DeleteLocationTypeResponse.class);
    }
}
