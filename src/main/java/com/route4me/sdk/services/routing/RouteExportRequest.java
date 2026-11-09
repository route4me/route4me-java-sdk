package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * The JSON body of {@link RoutingManagerV5#exportRoute(String, RouteExportRequest)}: the format and what to include.
 * Every field is optional, and a {@code null} field is left out of the body.
 */
@Data
@Accessors(chain = true)
public class RouteExportRequest {
    /** The export format, such as {@code csv}, {@code json}, {@code xml} or {@code html} ({@code format}). */
    @SerializedName("format")
    private String format;

    /** The export format, under its older name ({@code strExportFormat}). */
    @SerializedName("strExportFormat")
    private String strExportFormat;

    /** {@code true} to include the addresses of the route ({@code with_addresses}). */
    @SerializedName("with_addresses")
    private Boolean withAddresses;

    /** {@code true} to include the notes of the route ({@code with_notes}). */
    @SerializedName("with_notes")
    private Boolean withNotes;

    /** {@code true} to include the actual data, as driven ({@code with_actual_data}). */
    @SerializedName("with_actual_data")
    private Boolean withActualData;

    /** {@code true} to include all the custom fields ({@code all_custom_fields}). */
    @SerializedName("all_custom_fields")
    private Boolean allCustomFields;

    /** The export profile to use ({@code export_profile_id}). */
    @SerializedName("export_profile_id")
    private Integer exportProfileId;

    /** {@code true} to get the export as a file ({@code download}). */
    @SerializedName("download")
    private Boolean download;

    /** The names of the columns to export ({@code columns}). */
    @SerializedName("columns")
    private String columns;

    /** The time zone of the exported times, an IANA name ({@code tz}). */
    @SerializedName("tz")
    private String tz;

    /** How to group the exported rows ({@code group_by}). */
    @SerializedName("group_by")
    private String groupBy;

    /** The first day of the exported period, as {@code YYYY-MM-DD} ({@code start_date}). */
    @SerializedName("start_date")
    private String startDate;

    /** The last day of the exported period, as {@code YYYY-MM-DD} ({@code end_date}). */
    @SerializedName("end_date")
    private String endDate;

    /** Only the routes of these members ({@code member_id}). */
    @SerializedName("member_id")
    private List<Long> memberId;

    /** The IDs of more routes to export ({@code route_id}). */
    @SerializedName("route_id")
    private List<String> routeId;

    /** Text to search for in the routes ({@code query}). */
    @SerializedName("query")
    private String query;

    /** Text to search for in the routes, as the data table searches ({@code search_query}). */
    @SerializedName("search_query")
    private String searchQuery;

    /** The route filters, as in {@link RouteListRequest#getFilters()} ({@code filters}). */
    @SerializedName("filters")
    private Map<String, Object> filters;

    /** The IDs of routes to leave out ({@code exclude_ids}). */
    @SerializedName("exclude_ids")
    private List<String> excludeIds;

    /** The time zone of the dates of the filters, an IANA name ({@code timezone}). */
    @SerializedName("timezone")
    private String timezone;
}
