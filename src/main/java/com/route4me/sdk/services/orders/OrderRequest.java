package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Search parameters of {@link OrdersManager#getOrders(OrderRequest)}.
 *
 * <p>Without a filter they are sent as query parameters of {@code GET order.php}. With a
 * filter they are sent as the JSON body of {@code POST order.php}; the
 * {@code @SerializedName} names are the API names in both cases.
 */
@Data
@Accessors(chain = true)
public class OrderRequest {

    /** The order IDs, separated by commas ({@code order_ids}). */
    @SerializedName("order_ids")
    @QueryParameter("order_ids")
    private String ids;
    /** Limit per page, if you use 0 you will get all records ({@code limit}). */
    @SerializedName("limit")
    @QueryParameter("limit")
    private Integer limit;
    /** Only records from that offset will be considered ({@code offset}). */
    @SerializedName("offset")
    @QueryParameter("offset")
    private Integer offset;
    /** Filter routed/unrouted. enum(all,routed,unrouted) ({@code display}). */
    @SerializedName("display")
    @QueryParameter("display")
    private DisplayType display;
    /** Date an order was inserted ({@code day_added_YYMMDD}). */
    @SerializedName("day_added_YYMMDD")
    @QueryParameter("day_added_YYMMDD")
    private String dateInserted;
    /** Date an order was scheduled for ({@code scheduled_for_YYMMDD}). */
    @SerializedName("scheduled_for_YYMMDD")
    @QueryParameter("scheduled_for_YYMMDD")
    private String dateScheduled;
    /**
     * If a query is an array, the search engine will search by fields, if a query is a string - will search by all text
     * fields. Array / string ({@code query}).
     */
    @SerializedName("query")
    @QueryParameter("query")
    private String query;
    /** Use it for get specific fields. String / coma separated ({@code fields}). */
    @SerializedName("fields")
    @QueryParameter("fields")
    private String fields;
    /** Sent only in the JSON body: a query string cannot carry it. */
    @SerializedName("filter")
    private OrderFilter filter;


    /** Which orders to list: all of them, the routed ones or the unrouted ones. */
    public enum DisplayType {
        /** All orders ({@code all}). */
        @SerializedName("all")
        ALL("all"),
        /** Orders on a route ({@code routed}). */
        @SerializedName("routed")
        ROUTED("routed"),
        /** Orders on no route ({@code unrouted}). */
        @SerializedName("unrouted")
        UNROUTED("unrouted");

        private final String internal;

        DisplayType(String internal) {
            this.internal = internal;
        }

        /**
         * Returns the API value of the display type.
         *
         * @return the value, such as {@code all}
         */
        public String toString() {
            return this.internal;
        }
    }
}
