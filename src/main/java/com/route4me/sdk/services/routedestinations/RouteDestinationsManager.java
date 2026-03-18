package com.route4me.sdk.services.routedestinations;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

public class RouteDestinationsManager extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String ROUTE_DESTINATIONS_EP = V5_BASE + "/route-destinations";
    private static final String COLUMNS_EP = ROUTE_DESTINATIONS_EP + "/columns";
    private static final String SORTING_EP = ROUTE_DESTINATIONS_EP + "/sorting";
    private static final String LIST_EP = ROUTE_DESTINATIONS_EP + "/list";
    private static final String LIST_FIELDS_EP = ROUTE_DESTINATIONS_EP + "/list/fields";
    private static final String ORDER_EP = ROUTE_DESTINATIONS_EP + "/order";
    private static final String SEQUENCE_EP = ROUTE_DESTINATIONS_EP + "/sequence";

    public RouteDestinationsManager(String apiKey) {
        super(apiKey);
    }

    public RouteDestinationsManager(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    private static URIBuilder v5Builder(String endpoint) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        builder.setPath(endpoint);
        return builder;
    }

    public RouteDestinationResource getDestination(String id) throws APIException {
        URIBuilder builder = v5Builder(ROUTE_DESTINATIONS_EP + "/" + id);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, RouteDestinationResource.class);
    }

    public RouteDestinationColumnsResource getDestinationColumns(String columnsConfiguratorKey) throws APIException {
        URIBuilder builder = v5Builder(COLUMNS_EP);
        if (columnsConfiguratorKey != null) {
            builder.setParameter("columns_configurator_key", columnsConfiguratorKey);
        }
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, RouteDestinationColumnsResource.class);
    }

    public EditDestinationColumnsResponse editDestinationColumns(EditDestinationColumnsRequest request) throws APIException {
        URIBuilder builder = v5Builder(COLUMNS_EP);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(request), EditDestinationColumnsResponse.class);
    }

    public RouteDestinationsListResponse getDestinationsList(GetDestinationsRequest request) throws APIException {
        URIBuilder builder = v5Builder(LIST_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), RouteDestinationsListResponse.class);
    }

    public RouteDestinationFieldsResponse getDestinationFields() throws APIException {
        URIBuilder builder = v5Builder(LIST_FIELDS_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, RouteDestinationFieldsResponse.class);
    }

    public SortingFilteringResource getDestinationByLabelCode(String labelCode) throws APIException {
        URIBuilder builder = v5Builder(SORTING_EP);
        builder.setParameter("label_code", labelCode);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, SortingFilteringResource.class);
    }

    public RouteDestinationsByOrderResponse getDestinationsByOrder(String orderUuid) throws APIException {
        URIBuilder builder = v5Builder(ORDER_EP + "/" + orderUuid);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, RouteDestinationsByOrderResponse.class);
    }

    public DestinationSequenceResponse getDestinationSequence(DestinationSequenceRequest request) throws APIException {
        URIBuilder builder = v5Builder(SEQUENCE_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), DestinationSequenceResponse.class);
    }
}
