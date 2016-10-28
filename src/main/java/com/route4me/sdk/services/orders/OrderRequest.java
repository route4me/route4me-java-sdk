package com.route4me.sdk.services.orders;

import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderRequest {

    @QueryParameter("order_ids")
    private String ids;
    @QueryParameter("limit")
    private Integer limit;
    @QueryParameter("offset")
    private Integer offset;
    @QueryParameter("display")
    private DisplayType display;
    @QueryParameter("day_added_YYMMDD")
    private String dateInserted;
    @QueryParameter("scheduled_for_YYMMDD")
    private String dateScheduled;
    @QueryParameter("query")
    private String query;
    @QueryParameter("fields")
    private String fields;

    public enum DisplayType {
        ALL("all"), ROUTED("routed"), UNROUTED("unrouted");

        private String internal;

        DisplayType(String internal) {
            this.internal = internal;
        }

        public String toString() {
            return this.internal;
        }
    }
}
