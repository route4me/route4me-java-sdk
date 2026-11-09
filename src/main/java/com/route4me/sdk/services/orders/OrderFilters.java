package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/**
 * The {@code filters} of a V5 order search ({@link OrderManagerV5#searchOrders}) or of a batch update by filters
 * ({@link OrderManagerV5#batchUpdateFilter}); filters left {@code null} are not sent.
 *
 * <p>The date filters ({@code updated_timestamp}, {@code created_timestamp}, {@code scheduled_for}, {@code day_added}
 * and {@code sorted_on}) take a Unix timestamp, an ISO 8601 date string, or a range: a map with {@code start} and
 * {@code end}.
 */
@Data
public class OrderFilters {
    /** Only these orders, by their 32-character hex IDs ({@code order_ids}). */
    @SerializedName("order_ids")
    private List<String> orderIds;

    /** Only these orders, by their UUIDs ({@code order_uuids}). */
    @SerializedName("order_uuids")
    private List<String> orderUuids;

    /** Leave out these orders, by their 32-character hex IDs ({@code excluded_ids}). */
    @SerializedName("excluded_ids")
    private List<String> excludedIds;

    /** Only the orders with these tracking numbers ({@code tracking_numbers}). */
    @SerializedName("tracking_numbers")
    private List<String> trackingNumbers;

    /** Only geocoded orders ({@code only_geocoded}). */
    @SerializedName("only_geocoded")
    private Boolean onlyGeocoded;

    /** Last update time: a timestamp, a date string or a {@code start}/{@code end} map. */
    @SerializedName("updated_timestamp")
    private Object updatedTimestamp;

    /** Creation time: a timestamp, a date string or a {@code start}/{@code end} map. */
    @SerializedName("created_timestamp")
    private Object createdTimestamp;

    /** Scheduled date: a timestamp, a date string or a {@code start}/{@code end} map. */
    @SerializedName("scheduled_for")
    private Object scheduledFor;

    /** Only orders that are not scheduled ({@code only_unscheduled}). */
    @SerializedName("only_unscheduled")
    private Boolean onlyUnscheduled;

    /** The date the orders were added: a timestamp, a date string or a {@code start}/{@code end} map. */
    @SerializedName("day_added")
    private Object dayAdded;

    /** Sorting date: a timestamp, a date string or a {@code start}/{@code end} map. */
    @SerializedName("sorted_on")
    private Object sortedOn;

    /**
     * Only these stop types, such as {@code DELIVERY}, {@code PICKUP}, {@code BREAK}, {@code MEETUP}, {@code SERVICE},
     * {@code VISIT} or {@code DRIVEBY} ({@code address_stop_types}).
     */
    @SerializedName("address_stop_types")
    private List<String> addressStopTypes;

    /** Only the orders whose last status is one of these ({@code last_statuses}). */
    @SerializedName("last_statuses")
    private List<Integer> lastStatuses;

    /** Only the orders in these territories ({@code territory_ids}). */
    @SerializedName("territory_ids")
    private List<Integer> territoryIds;

    /** Only the orders done on this day ({@code done_day}). */
    @SerializedName("done_day")
    private String doneDay;

    /** Only the orders in possession on this day ({@code possession_day}). */
    @SerializedName("possession_day")
    private String possessionDay;

    /** Only the orders of these groups ({@code groups}). */
    @SerializedName("groups")
    private List<String> groups;

    /** Which orders by route count: {@code routed}, {@code unrouted} or {@code all} ({@code display}). */
    @SerializedName("display")
    private String display;
}
