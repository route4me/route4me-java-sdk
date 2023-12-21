package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.responses.StatusResponse;
import java.util.HashMap;
import lombok.*;
import org.apache.http.client.utils.URIBuilder;

import java.util.List;
import java.util.Map;

public class OrdersManager extends Manager {

    public static final String ORDERS_EP = "/api.v4/order.php";

    public OrdersManager(String apiKey) {
        super(apiKey);
    }

    public Order addOrder(Order order) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ORDERS_EP);
        return this.makeJSONRequest(RequestMethod.POST, builder, order, Order.class);
    }

    public Order getOrder(long orderId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ORDERS_EP);
        builder.setParameter("order_id", Long.toString(orderId));
        return this.makeJSONRequest(RequestMethod.GET, builder, "", Order.class);
    }

    public List<Order> getOrders(OrderRequest request) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ORDERS_EP);
        return this.makeJSONRequest(RequestMethod.GET, builder, request, GetOrdersResponse.class).getResults();
    }

    public List<List<Long>> getOrdersByCustomFields(OrderRequest request) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ORDERS_EP);
        return this.makeJSONRequest(RequestMethod.GET, builder, request, GetOrdersResponseByCustomFields.class).getResults();
    }

    public List<Order> getOrdersByScheduledDate(List<String> scheduledForYYMMDD) throws APIException {
        return this.getOrdersByScheduledDate(scheduledForYYMMDD, 30, 0);
    }

    public List<Order> getOrdersByScheduledDate(List<String> scheduledForYYMMDD, Integer limit, Integer offset) throws APIException {
        OrderFilter filter = new OrderFilter();
        filter.setScheduled_for_YYMMDD(scheduledForYYMMDD);
        return this.OrderByFilter(filter, limit, offset);
    }

    public List<Order> getOrdersByCustomField(String customFieldKey, String customFieldValue) throws APIException {
        OrderFilter filter = new OrderFilter();
        filter.setDisplay("all");
        Map<String, Object> terms = new HashMap<>();
        terms.put("custom_data." + customFieldKey, customFieldValue);
        filter.setTerms(terms);
        return this.OrderByFilter(filter);
    }

    public List<Order> getOrdersByStatus(List<Integer> orderStatus) throws APIException {
        OrderFilter filter = new OrderFilter();
        filter.setStatuses(orderStatus);
        return this.OrderByFilter(filter);
    }

    public List<Order> getOrdersByTrackingNumber(List<String> trackingNumbers) throws APIException {
        return this.getOrdersByTrackingNumber(trackingNumbers, 30, 0);
    }

    public List<Order> getOrdersByTrackingNumber(List<String> trackingNumbers, Integer limit, Integer offset) throws APIException {
        OrderFilter filter = new OrderFilter();
        filter.setTrackingNumbers(trackingNumbers);
        return this.OrderByFilter(filter, limit, offset);
    }
    
    private List<Order> OrderByFilter(OrderFilter filter, Integer limit, Integer offset) throws APIException {
        OrderRequest request = new OrderRequest();
        request.setFilter(filter);
        request.setLimit(limit);
        request.setOffset(offset);
        URIBuilder builder = Manager.defaultBuilder(ORDERS_EP);
        return this.makeJSONRequest(RequestMethod.POST, builder, request, GetOrdersResponse.class).getResults();
    }


    
    private List<Order> OrderByFilter(OrderFilter filter) throws APIException {
        return this.OrderByFilter(filter, 30, 0);
    }

    public Order updateOrder(Order order) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ORDERS_EP);
        return this.makeJSONRequest(RequestMethod.PUT, builder, order, Order.class);
    }

    public StatusResponse deleteOrders(long... orderIds) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ORDERS_EP);
        return this.makeJSONRequest(RequestMethod.DELETE, builder, new DeleteOrdersRequest(orderIds), StatusResponse.class);
    }

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private static class GetOrdersResponse {

        @SerializedName("results")
        private List<Order> results;
        @SerializedName("total")
        private Integer total;
    }

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private static class GetOrdersResponseByCustomFields {
        @SerializedName("results")
        private List<List<Long>> results;
        @SerializedName("total")
        private Integer total;
        @SerializedName("fields")
        private List<String> fields;
    }

    @Data
    @RequiredArgsConstructor
    private static class DeleteOrdersRequest {
        @SerializedName("order_ids")
        private final long[] orderIds;
    }
}
