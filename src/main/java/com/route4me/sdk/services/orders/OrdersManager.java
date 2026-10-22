package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
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
