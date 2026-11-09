/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * The {@code filter} of an order search, set with {@code OrderRequest.setFilter}.
 *
 * <p>With a filter, {@link OrdersManager#getOrders(OrderRequest)} sends
 * {@code POST /api.v4/order.php} with the search, filter included, as the JSON body. Create
 * one with {@code new OrderFilter()} and the chained setters; fields left {@code null} are
 * not sent.
 */
@Data
@Accessors(chain = true)
public class OrderFilter {
    /** Available values: "all", "routed", "unrouted" ({@code display}). */
    @QueryParameter("display")
    @SerializedName("display")
    private String display;
    /**
     * Start and end dates to filter the orders by schedule. e.g. ["2019-06-01", "2019-06-18"] ({@code
     * scheduled_for_YYMMDD}).
     */
    @SerializedName("scheduled_for_YYMMDD")
    @QueryParameter("scheduled_for_YYMMDD")
    private List<String> scheduledForYYMMDD;
    /** The field name, field value pairs for filtering ({@code terms}). */
    @SerializedName("terms")
    @QueryParameter("terms")
    private Map<String, Object> terms;
    /** An array of the order status codes ({@code statuses}). */
    @SerializedName("statuses")
    @QueryParameter("statuses")
    private List<Integer> statuses;
    /** An array of the tracking numbers to filter the orders by tracking numbers ({@code tracking_numbers}). */
    @SerializedName("tracking_numbers")
    @QueryParameter("tracking_numbers")
    private List<String> trackingNumbers;
}
