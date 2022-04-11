package com.route4me.sdk.services.routing;

import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RoutesRequest {
    @QueryParameter("route_id")
    private String id;
    @QueryParameter("directions")
    private Boolean directions;
    @QueryParameter("device_tracking_history")
    private Boolean deviceTrackingHistory;
    @QueryParameter("limit")
    private Integer limit;
    @QueryParameter("offset")
    private Integer offset;
    @QueryParameter("original")
    private Boolean original;
    @QueryParameter("notes")
    private Boolean notes;
    @QueryParameter("query")
    private String query;
    @QueryParameter("reoptimize")
    private Boolean reoptimize;
    @QueryParameter("recompute_directions")
    private Boolean recomputeDirections;
    @QueryParameter("start_date")
    private String startDate;
    @QueryParameter("end_date")
    private String endDate;
}
