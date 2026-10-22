package com.route4me.sdk.services.orders;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

public class OrderManagerV5 extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String ORDERS_PLATFORM_EP = V5_BASE + "/orders-platform";
    private static final String ORDERS_CREATE_EP = ORDERS_PLATFORM_EP + "/create";
    private static final String ORDERS_BATCH_UPDATE_FILTER_EP = ORDERS_PLATFORM_EP + "/batch-update/filter";
    private static final String ORDERS_BATCH_DELETE_EP = ORDERS_PLATFORM_EP + "/batch-delete";
    private static final String ORDERS_BATCH_UPDATE_EP = ORDERS_PLATFORM_EP + "/batch-update";
    private static final String ORDERS_BATCH_CREATE_EP = ORDERS_PLATFORM_EP + "/batch-create";
    private static final String ORDERS_CUSTOM_FIELDS_EP = ORDERS_PLATFORM_EP + "/order-custom-user-fields";
    private static final String ORDERS_ARCHIVE_EP = V5_BASE + "/orders/archive";
    private static final String ORDERS_HISTORY_EP = V5_BASE + "/orders/history";

    public OrderManagerV5(String apiKey) {
        super(apiKey);
    }

    public OrderManagerV5(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    private static URIBuilder v5Builder(String endpoint, Object... segments) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        Manager.withPathSegments(builder, endpoint, segments);
        return builder;
    }

    public OrderV5 createOrder(OrderV5 order) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_CREATE_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(order), OrderV5.class);
    }

    public OrderV5 getOrder(String orderUuid) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_PLATFORM_EP, orderUuid);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, OrderV5.class);
    }

    public OrderV5 updateOrder(String orderUuid, OrderV5 order) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_PLATFORM_EP, orderUuid);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(order), OrderV5.class);
    }

    public StatusResponse deleteOrder(String orderUuid) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_PLATFORM_EP, orderUuid);
        return this.makeRequest(RequestMethod.DELETE, builder, "", StatusResponse.class);
    }

    public SearchOrdersResponse searchOrders(SearchOrdersRequest request) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_PLATFORM_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), SearchOrdersResponse.class);
    }

    public AsyncStatusResponse batchCreateOrders(BatchCreateOrdersRequest request) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_BATCH_CREATE_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), AsyncStatusResponse.class);
    }

    public OrderV5[] batchUpdateOrders(BatchUpdateOrdersRequest request) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_BATCH_UPDATE_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), OrderV5[].class);
    }

    public StatusResponse batchDeleteOrders(BatchDeleteOrdersRequest request) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_BATCH_DELETE_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), StatusResponse.class);
    }

    public AsyncStatusResponse batchUpdateFilter(BatchUpdateFilterOrderRequest request) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_BATCH_UPDATE_FILTER_EP);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(request), AsyncStatusResponse.class);
    }

    public ArchiveOrdersResponse archiveOrders(ArchiveOrdersRequest request) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_ARCHIVE_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), ArchiveOrdersResponse.class);
    }

    public OrderHistoryResponse getOrderHistory(String orderUuid, String trackingNumber) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_HISTORY_EP);
        if (orderUuid != null) {
            builder.setParameter("order_uuid", orderUuid);
        }
        if (trackingNumber != null) {
            builder.setParameter("tracking_number", trackingNumber);
        }
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, OrderHistoryResponse.class);
    }

    public CustomUserFieldsResponse getCustomUserFields() throws APIException {
        URIBuilder builder = v5Builder(ORDERS_CUSTOM_FIELDS_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, CustomUserFieldsResponse.class);
    }

    public CustomUserFieldResponse createCustomUserField(CreateCustomUserFieldRequest request) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_CUSTOM_FIELDS_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), CustomUserFieldResponse.class);
    }

    public CustomUserFieldResponse updateCustomUserField(String fieldUuid, UpdateCustomUserFieldRequest request) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_CUSTOM_FIELDS_EP, fieldUuid);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(request), CustomUserFieldResponse.class);
    }

    public CustomUserFieldResponse deleteCustomUserField(String fieldUuid) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_CUSTOM_FIELDS_EP, fieldUuid);
        return this.makeRequest(RequestMethod.DELETE, builder, "", CustomUserFieldResponse.class);
    }
}
