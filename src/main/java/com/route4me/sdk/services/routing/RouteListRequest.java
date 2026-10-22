package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * The JSON body of the V5 route lists of {@link RoutingManagerV5}: {@link RoutingManagerV5#getRoutes},
 * {@link RoutingManagerV5#getRoutesList} and {@link RoutingManagerV5#getRouteDatatableWithElasticSearch}. Every
 * field is optional, and a {@code null} field is left out of the body.
 */
@Data
@Accessors(chain = true)
public class RouteListRequest {
    /** Text to search for in the routes ({@code query}). */
    @SerializedName("query")
    private String query;

    /** Text to search for in the routes, as the data table searches ({@code search_query}). */
    @SerializedName("search_query")
    private String searchQuery;

    /**
     * The filters, by field name ({@code filters}). For example {@code schedule_date} or {@code created_date}, a
     * list of two {@code YYYY-MM-DD} dates; {@code route_id}, {@code vehicle_ids} or {@code member_ids}, lists of
     * IDs; {@code route_status}, a list of statuses; and ranges such as {@code trip_distance}, a list of a minimum
     * and a maximum. {@link RoutingManagerV5#getRouteDataTableConfig()} lists the fields the account can filter by.
     */
    @SerializedName("filters")
    private Map<String, Object> filters;

    /** The IDs of routes to leave out ({@code exclude_ids}). */
    @SerializedName("exclude_ids")
    private List<String> excludeIds;

    /** The time zone of the dates of the filters, an IANA name such as {@code America/New_York} ({@code timezone}). */
    @SerializedName("timezone")
    private String timezone;

    /**
     * How to group the routes: {@code start_date}, {@code user}, {@code vehicle}, {@code status}, {@code facility},
     * {@code optimization}, {@code customer} and others ({@code group_by}).
     */
    @SerializedName("group_by")
    private String groupBy;

    /** Only routes in which these fields have a value ({@code exist_fields}). */
    @SerializedName("exist_fields")
    private List<String> existFields;

    /**
     * The sort order: pairs of a field name and {@code asc} or {@code desc}, such as
     * {@code [["route_created_unix", "desc"]]} ({@code order_by}).
     */
    @SerializedName("order_by")
    private List<List<String>> orderBy;

    /** The page number, from 1 ({@code page}). */
    @SerializedName("page")
    private Integer page;

    /** The number of routes per page, at most 500 ({@code per_page}). */
    @SerializedName("per_page")
    private Integer perPage;

    /**
     * The fields to return for each route, such as {@code route_id} or {@code route_data.*}; none returns all of them
     * ({@code fields}).
     */
    @SerializedName("fields")
    private List<String> fields;
}
