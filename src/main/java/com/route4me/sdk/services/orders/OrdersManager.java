package com.route4me.sdk.services.orders;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.responses.StatusResponse;
import java.util.HashMap;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

import java.util.List;
import java.util.Map;

/**
 * Manages orders through the V4 API ({@code https://api.route4me.com/api.v4/order.php}). {@link OrderManagerV5} covers
 * the V5 order
 * platform.
 */
public class OrdersManager extends Manager {

    /** The path of the orders, on {@code api.route4me.com}. */
    public static final String ORDERS_EP = "/api.v4/order.php";
    private static final String ORDER_CUSTOM_FIELDS_EP = "/api.v4/order_custom_user_fields.php";
    private static final String ORDERS_UPDATES_EP = "/api.v4/orders/changes.php";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as the {@code api_key} query parameter
     */
    public OrdersManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates an order: {@code POST https://api.route4me.com/api.v4/order.php}, with the order as the JSON body.
     *
     * @param order the order
     * @return the created order
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Order addOrder(Order order) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ORDERS_EP);
        return this.makeJSONRequest(RequestMethod.POST, builder, order, Order.class);
    }

    /**
     * Gets one order: {@code GET https://api.route4me.com/api.v4/order.php?order_id=...}.
     *
     * @param orderId the order ID
     * @return the order
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Order getOrder(long orderId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ORDERS_EP);
        builder.setParameter("order_id", Long.toString(orderId));
        return this.makeJSONRequest(RequestMethod.GET, builder, "", Order.class);
    }

    /**
     * Searches orders: {@code GET https://api.route4me.com/api.v4/order.php}, or {@code POST} to the same URL when the
     * request has a filter.
     *
     * <p>Without a filter the parameters go in the query. With a filter they go, filter included, in the JSON body,
     * which is how the API takes a filter.
     *
     * @param request the search parameters
     * @return the matching orders
     * @throws APIException if the request fails
     */
    public List<Order> getOrders(OrderRequest request) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ORDERS_EP);
        RequestMethod method = request.getFilter() != null ? RequestMethod.POST : RequestMethod.GET;
        GetOrdersResponse response = this.makeJSONRequest(method, builder, request, GetOrdersResponse.class);
        return response == null ? null : response.getResults();
    }

    /**
     * Searches orders by scheduled date: {@code POST /api.v4/order.php} with a filter body, 30 at a time.
     *
     * @param scheduledForYYMMDD the dates, as {@code YYYY-MM-DD}
     * @return the matching orders, or {@code null} when the answer has no results
     * @throws APIException if the request fails
     */
    public List<Order> getOrdersByScheduledDate(List<String> scheduledForYYMMDD) throws APIException {
        return this.getOrdersByScheduledDate(scheduledForYYMMDD, 30, 0);
    }

    /**
     * Searches orders by scheduled date: {@code POST /api.v4/order.php} with a filter body.
     *
     * @param scheduledForYYMMDD the dates, as {@code YYYY-MM-DD}
     * @param limit              the page size
     * @param offset             the number of orders to skip
     * @return the matching orders, or {@code null} when the answer has no results
     * @throws APIException if the request fails
     */
    public List<Order> getOrdersByScheduledDate(List<String> scheduledForYYMMDD, Integer limit, Integer offset)
            throws APIException {
        OrderFilter filter = new OrderFilter();
        filter.setScheduledForYYMMDD(scheduledForYYMMDD);
        return this.orderByFilter(filter, limit, offset);
    }

    /**
     * Searches orders by custom field value: {@code POST /api.v4/order.php} with a filter body, 30 at a time.
     *
     * @param customFieldKey   the custom field name
     * @param customFieldValue the value to match
     * @return the matching orders, or {@code null} when the answer has no results
     * @throws APIException if the request fails
     */
    public List<Order> getOrdersByCustomField(String customFieldKey, String customFieldValue) throws APIException {
        OrderFilter filter = new OrderFilter();
        filter.setDisplay("all");
        Map<String, Object> terms = new HashMap<>();
        terms.put("custom_data." + customFieldKey, customFieldValue);
        filter.setTerms(terms);
        return this.orderByFilter(filter);
    }

    /**
     * Searches orders by status: {@code POST /api.v4/order.php} with a filter body, 30 at a time.
     *
     * @param orderStatus the status codes, see {@link OrderStatus#getValue()}
     * @return the matching orders, or {@code null} when the answer has no results
     * @throws APIException if the request fails
     */
    public List<Order> getOrdersByStatus(List<Integer> orderStatus) throws APIException {
        OrderFilter filter = new OrderFilter();
        filter.setStatuses(orderStatus);
        return this.orderByFilter(filter);
    }

    /**
     * Searches orders by tracking number: {@code POST /api.v4/order.php} with a filter body, 30 at a time.
     *
     * @param trackingNumbers the tracking numbers
     * @return the matching orders, or {@code null} when the answer has no results
     * @throws APIException if the request fails
     */
    public List<Order> getOrdersByTrackingNumber(List<String> trackingNumbers) throws APIException {
        return this.getOrdersByTrackingNumber(trackingNumbers, 30, 0);
    }

    /**
     * Searches orders by tracking number: {@code POST /api.v4/order.php} with a filter body.
     *
     * @param trackingNumbers the tracking numbers
     * @param limit           the page size
     * @param offset          the number of orders to skip
     * @return the matching orders, or {@code null} when the answer has no results
     * @throws APIException if the request fails
     */
    public List<Order> getOrdersByTrackingNumber(List<String> trackingNumbers, Integer limit, Integer offset)
            throws APIException {
        OrderFilter filter = new OrderFilter();
        filter.setTrackingNumbers(trackingNumbers);
        return this.orderByFilter(filter, limit, offset);
    }

    private List<Order> orderByFilter(OrderFilter filter, Integer limit, Integer offset) throws APIException {
        OrderRequest request = new OrderRequest();
        request.setFilter(filter);
        request.setLimit(limit);
        request.setOffset(offset);
        URIBuilder builder = Manager.defaultBuilder(ORDERS_EP);
        GetOrdersResponse response = this.makeJSONRequest(RequestMethod.POST, builder, request,
                GetOrdersResponse.class);
        return response == null ? null : response.getResults();
    }



    private List<Order> orderByFilter(OrderFilter filter) throws APIException {
        return this.orderByFilter(filter, 30, 0);
    }

    /**
     * Updates an order: {@code PUT https://api.route4me.com/api.v4/order.php}, with the order as the JSON body.
     *
     * @param order the order, with its order ID
     * @return the updated order
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Order updateOrder(Order order) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ORDERS_EP);
        return this.makeJSONRequest(RequestMethod.PUT, builder, order, Order.class);
    }

    /**
     * Deletes orders: {@code DELETE https://api.route4me.com/api.v4/order.php}, with {@code {"order_ids": [...]}} as
     * the JSON body.
     *
     * @param orderIds the order IDs
     * @return the status of the request
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public StatusResponse deleteOrders(long... orderIds) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ORDERS_EP);
        return this.makeJSONRequest(RequestMethod.DELETE, builder, new DeleteOrdersRequest(orderIds),
                StatusResponse.class);
    }

    /**
     * Lists the custom user fields of orders: {@code GET https://api.route4me.com/api.v4/order_custom_user_fields.php}.
     *
     * <p>The V4 spec describes the answer as {@code {"data": [...]}}, while the C# SDK reads a bare array; both are
     * accepted.
     *
     * @return the fields, or {@code null} when the answer holds no list
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public List<OrderCustomUserField> getOrderCustomUserFields() throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ORDER_CUSTOM_FIELDS_EP);
        JsonElement reply = this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, JsonElement.class);
        JsonElement list = reply != null && reply.isJsonObject() ? reply.getAsJsonObject().get("data") : reply;
        if (list == null || !list.isJsonArray()) {
            return null;
        }
        try {
            return this.gson.fromJson(list, new TypeToken<List<OrderCustomUserField>>() {
            }.getType());
        } catch (JsonParseException | IllegalStateException ex) {
            throw new APIException("Could not read the order custom user fields", ex);
        }
    }

    /**
     * Creates a custom user field of orders: {@code POST
     * https://api.route4me.com/api.v4/order_custom_user_fields.php}, with the field as the JSON body.
     *
     * @param field the field: its name, label, type, and optionally its value and type info
     * @return the result, with the created field
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public OrderCustomUserFieldResponse createOrderCustomUserField(OrderCustomUserField field) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ORDER_CUSTOM_FIELDS_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(field),
                OrderCustomUserFieldResponse.class);
    }

    /**
     * Updates a custom user field of orders: {@code PUT
     * https://api.route4me.com/api.v4/order_custom_user_fields.php}, with the field as the JSON body.
     *
     * @param field the field, with its {@code order_custom_field_id} and the new values
     * @return the result, with the updated field
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public OrderCustomUserFieldResponse updateOrderCustomUserField(OrderCustomUserField field) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ORDER_CUSTOM_FIELDS_EP);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(field),
                OrderCustomUserFieldResponse.class);
    }

    /**
     * Removes a custom user field of orders: {@code DELETE
     * https://api.route4me.com/api.v4/order_custom_user_fields.php}, with {@code {"order_custom_field_id": ...}} as
     * the JSON body.
     *
     * @param orderCustomFieldId the ID of the field
     * @return the result
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public OrderCustomUserFieldResponse removeOrderCustomUserField(long orderCustomFieldId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ORDER_CUSTOM_FIELDS_EP);
        OrderCustomUserField field = new OrderCustomUserField();
        field.setOrderCustomFieldId(orderCustomFieldId);
        return this.makeRequest(RequestMethod.DELETE, builder, this.gson.toJson(field),
                OrderCustomUserFieldResponse.class);
    }

    /**
     * Lists the orders changed since a time: {@code GET
     * https://api.route4me.com/api.v4/orders/changes.php?last_known_ts=...}.
     *
     * @param lastKnownTs the time of the last known change, as a Unix timestamp ({@code last_known_ts}, required)
     * @return the changed orders
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public OrdersUpdatesResponse getOrdersUpdates(long lastKnownTs) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ORDERS_UPDATES_EP);
        builder.setParameter("last_known_ts", Long.toString(lastKnownTs));
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, OrdersUpdatesResponse.class);
    }

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private static class GetOrdersResponse {

        /** An array of the Order type objects ({@code results}). */
        @SerializedName("results")
        private List<Order> results;
        /** Total number of the returned orders ({@code total}). */
        @SerializedName("total")
        private Integer total;
    }

    @Data
    @RequiredArgsConstructor
    private static class DeleteOrdersRequest {

        /** The IDs of the orders to delete ({@code order_ids}). */
        @SerializedName("order_ids")
        private final long[] orderIds;
    }
}
