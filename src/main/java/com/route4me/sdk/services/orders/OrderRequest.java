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

    @SerializedName("order_ids")
    @QueryParameter("order_ids")
    private String ids;
    @SerializedName("limit")
    @QueryParameter("limit")
    private Integer limit;
    @SerializedName("offset")
    @QueryParameter("offset")
    private Integer offset;
    @SerializedName("display")
    @QueryParameter("display")
    private DisplayType display;
    @SerializedName("day_added_YYMMDD")
    @QueryParameter("day_added_YYMMDD")
    private String dateInserted;
    @SerializedName("scheduled_for_YYMMDD")
    @QueryParameter("scheduled_for_YYMMDD")
    private String dateScheduled;
    @SerializedName("query")
    @QueryParameter("query")
    private String query;
    @SerializedName("fields")
    @QueryParameter("fields")
    private String fields;
    /** Sent only in the JSON body: a query string cannot carry it. */
    @SerializedName("filter")
    private OrderFilter filter;
    

    public enum DisplayType {
        @SerializedName("all")
        ALL("all"),
        @SerializedName("routed")
        ROUTED("routed"),
        @SerializedName("unrouted")
        UNROUTED("unrouted");

        private String internal;

        DisplayType(String internal) {
            this.internal = internal;
        }

        public String toString() {
            return this.internal;
        }
    }
}
