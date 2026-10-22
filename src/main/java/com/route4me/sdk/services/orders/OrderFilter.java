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
    @QueryParameter("display")
    @SerializedName("display")
    private String display;
    @SerializedName("scheduled_for_YYMMDD")
    @QueryParameter("scheduled_for_YYMMDD")
    private List<String> scheduledForYYMMDD;
    @SerializedName("terms")
    @QueryParameter("terms")
    private Map<String, Object> terms;
    @SerializedName("statuses")
    @QueryParameter("statuses")
    private List<Integer> statuses;
    @SerializedName("tracking_numbers")
    @QueryParameter("tracking_numbers")
    private List<String> trackingNumbers;
}
