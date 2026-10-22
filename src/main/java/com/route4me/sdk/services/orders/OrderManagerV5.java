package com.route4me.sdk.services.orders;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 * Manages orders through the V5 order platform ({@code https://wh.route4me.com/modules/api/v5.0/orders-platform}), with
 * order history, archiving and custom user fields.
 */
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

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     */
    public OrderManagerV5(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
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

    /**
     * Creates an order: {@code POST https://wh.route4me.com/modules/api/v5.0/orders-platform/create}, with the order as
     * the JSON body.
     *
     * @param order the order
     * @return the created order
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public OrderV5 createOrder(OrderV5 order) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_CREATE_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(order), OrderV5.class);
    }

    /**
     * Gets one order: {@code GET https://wh.route4me.com/modules/api/v5.0/orders-platform/{orderUuid}}.
     *
     * @param orderUuid the order UUID
     * @return the order
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public OrderV5 getOrder(String orderUuid) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_PLATFORM_EP, orderUuid);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, OrderV5.class);
    }

    /**
     * Updates an order: {@code PUT https://wh.route4me.com/modules/api/v5.0/orders-platform/{orderUuid}}, with the
     * order as the JSON body.
     *
     * @param orderUuid the order UUID
     * @param order the new values
     * @return the updated order
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public OrderV5 updateOrder(String orderUuid, OrderV5 order) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_PLATFORM_EP, orderUuid);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(order), OrderV5.class);
    }

    /**
     * Deletes an order: {@code DELETE https://wh.route4me.com/modules/api/v5.0/orders-platform/{orderUuid}}.
     *
     * @param orderUuid the order UUID
     * @return the status of the request
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public StatusResponse deleteOrder(String orderUuid) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_PLATFORM_EP, orderUuid);
        return this.makeRequest(RequestMethod.DELETE, builder, "", StatusResponse.class);
    }

    /**
     * Searches orders: {@code POST https://wh.route4me.com/modules/api/v5.0/orders-platform}, with the request as the
     * JSON body.
     *
     * @param request the order IDs, text search, filters, sort order, page ({@code limit} and {@code offset}) and
     *                fields
     * @return the matching orders and their total number
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public SearchOrdersResponse searchOrders(SearchOrdersRequest request) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_PLATFORM_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), SearchOrdersResponse.class);
    }

    /**
     * Creates several orders: {@code POST https://wh.route4me.com/modules/api/v5.0/orders-platform/batch-create}, with
     * the orders as the JSON body.
     *
     * @param request the orders
     * @return whether the request was accepted, and whether a background job does it
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public AsyncStatusResponse batchCreateOrders(BatchCreateOrdersRequest request) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_BATCH_CREATE_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), AsyncStatusResponse.class);
    }

    /**
     * Updates several orders with the same values: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/orders-platform/batch-update}, with the order IDs and the new values
     * as the JSON body.
     *
     * @param request the order IDs and the new values
     * @return the updated orders
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public OrderV5[] batchUpdateOrders(BatchUpdateOrdersRequest request) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_BATCH_UPDATE_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), OrderV5[].class);
    }

    /**
     * Deletes several orders: {@code POST https://wh.route4me.com/modules/api/v5.0/orders-platform/batch-delete}, with
     * {@code {"order_ids": [...]}} as the JSON body.
     *
     * @param request the order IDs, as 32-character hex strings
     * @return the status of the request
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public StatusResponse batchDeleteOrders(BatchDeleteOrdersRequest request) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_BATCH_DELETE_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), StatusResponse.class);
    }

    /**
     * Updates the orders that match filters, in the background: {@code PUT
     * https://wh.route4me.com/modules/api/v5.0/orders-platform/batch-update/filter}, with the filters, the text
     * search and the new values as the JSON body.
     *
     * @param request the filters, the text search and the new values
     * @return whether the request was accepted, and whether a background job does it
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public AsyncStatusResponse batchUpdateFilter(BatchUpdateFilterOrderRequest request) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_BATCH_UPDATE_FILTER_EP);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(request), AsyncStatusResponse.class);
    }

    /**
     * Lists the archived orders, one page at a time: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/orders/archive}, with the request as the JSON body.
     *
     * @param request the page cursor, the page size and the filters
     * @return one page of archived orders, with the cursor of the next page
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public ArchiveOrdersResponse archiveOrders(ArchiveOrdersRequest request) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_ARCHIVE_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), ArchiveOrdersResponse.class);
    }

    /**
     * Gets the history of an order: {@code GET https://wh.route4me.com/modules/api/v5.0/orders/history}. Every {@code
     * null} argument is left out of the query.
     *
     * @param orderUuid the order UUID ({@code order_uuid})
     * @param trackingNumber the tracking number of the order ({@code tracking_number})
     * @return the history events
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @see #getOrderHistory(Long, String, String)
     */
    public OrderHistoryResponse getOrderHistory(String orderUuid, String trackingNumber) throws APIException {
        return this.getOrderHistory(null, orderUuid, trackingNumber);
    }

    /**
     * Gets the history of an order: {@code GET https://wh.route4me.com/modules/api/v5.0/orders/history}, with the
     * order given by its ID, its UUID or its tracking number. Every {@code null} argument is left out of the query.
     *
     * @param orderId the order ID ({@code order_id})
     * @param orderUuid the order UUID ({@code order_uuid})
     * @param trackingNumber the tracking number of the order ({@code tracking_number})
     * @return the history events
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public OrderHistoryResponse getOrderHistory(Long orderId, String orderUuid, String trackingNumber)
            throws APIException {
        URIBuilder builder = v5Builder(ORDERS_HISTORY_EP);
        if (orderId != null) {
            builder.setParameter("order_id", String.valueOf(orderId));
        }
        if (orderUuid != null) {
            builder.setParameter("order_uuid", orderUuid);
        }
        if (trackingNumber != null) {
            builder.setParameter("tracking_number", trackingNumber);
        }
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, OrderHistoryResponse.class);
    }

    /**
     * Lists the custom user fields of orders: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/orders-platform/order-custom-user-fields}.
     *
     * @return the fields
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public CustomUserFieldsResponse getCustomUserFields() throws APIException {
        URIBuilder builder = v5Builder(ORDERS_CUSTOM_FIELDS_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, CustomUserFieldsResponse.class);
    }

    /**
     * Creates a custom user field of orders: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/orders-platform/order-custom-user-fields}, with the request as the JSON
     * body.
     *
     * @param request the field
     * @return the created field
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public CustomUserFieldResponse createCustomUserField(CreateCustomUserFieldRequest request) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_CUSTOM_FIELDS_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), CustomUserFieldResponse.class);
    }

    /**
     * Updates a custom user field of orders: {@code PUT
     * https://wh.route4me.com/modules/api/v5.0/orders-platform/order-custom-user-fields/{fieldUuid}}, with the request
     * as the
     * JSON body.
     *
     * @param fieldUuid the field UUID
     * @param request the new values
     * @return the updated field
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public CustomUserFieldResponse updateCustomUserField(String fieldUuid, UpdateCustomUserFieldRequest request)
            throws APIException {
        URIBuilder builder = v5Builder(ORDERS_CUSTOM_FIELDS_EP, fieldUuid);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(request), CustomUserFieldResponse.class);
    }

    /**
     * Deletes a custom user field of orders: {@code DELETE
     * https://wh.route4me.com/modules/api/v5.0/orders-platform/order-custom-user-fields/{fieldUuid}}.
     *
     * @param fieldUuid the field UUID
     * @return the deleted field
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public CustomUserFieldResponse deleteCustomUserField(String fieldUuid) throws APIException {
        URIBuilder builder = v5Builder(ORDERS_CUSTOM_FIELDS_EP, fieldUuid);
        return this.makeRequest(RequestMethod.DELETE, builder, "", CustomUserFieldResponse.class);
    }
}
