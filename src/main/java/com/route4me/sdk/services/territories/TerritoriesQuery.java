package com.route4me.sdk.services.territories;

import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * The query of {@link TerritoriesManagerV5#getTerritories(TerritoriesQuery)}; fields left {@code null} are not sent,
 * and booleans are sent as 1 or 0.
 */
@Data
@Accessors(chain = true)
public class TerritoriesQuery {
    /** Only the territories that contain this point: its latitude, given with {@code lng} ({@code lat}). */
    @QueryParameter("lat")
    private Double lat;
    /** Only the territories that contain this point: its longitude, given with {@code lat} ({@code lng}). */
    @QueryParameter("lng")
    private Double lng;
    /** The date for filtering the orders, as {@code YYYY-MM-DD} ({@code date}). */
    @QueryParameter("date")
    private String date;
    /** The first scheduled date of the orders, as {@code YYYY-MM-DD} ({@code scheduled_start}). */
    @QueryParameter("scheduled_start")
    private String scheduledStart;
    /** The last scheduled date of the orders, as {@code YYYY-MM-DD} ({@code scheduled_end}). */
    @QueryParameter("scheduled_end")
    private String scheduledEnd;
    /** Text to search the territories for ({@code search_query}). */
    @QueryParameter("search_query")
    private String searchQuery;
    /** Whether to return the address IDs of each territory ({@code addresses}). */
    @QueryParameter("addresses")
    private Boolean addresses;
    /** Whether to return the order IDs of each territory ({@code orders}). */
    @QueryParameter("orders")
    private Boolean orders;
    /** Whether to return the number of addresses of each territory ({@code addresses_count}). */
    @QueryParameter("addresses_count")
    private Boolean addressesCount;
    /** Whether to return the number of orders of each territory ({@code orders_count}). */
    @QueryParameter("orders_count")
    private Boolean ordersCount;
}
