package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Map;

/**
 * The answer of {@link SchedulesManager#getScheduleCalendar(ScheduleCalendarRequest)}: for each day, as
 * {@code YYYY-MM-DD}, the number of scheduled contacts, orders and routes. A count is present only if the request
 * asked for it.
 */
@Data
public class ScheduleCalendarResponse {
    /** The number of scheduled address book contacts of each day ({@code address_book}). */
    @SerializedName("address_book")
    private Map<String, Integer> addressBook;

    /** The number of scheduled orders of each day ({@code orders}). */
    @SerializedName("orders")
    private Map<String, Integer> orders;

    /** The number of routes of each day ({@code routes_count}). */
    @SerializedName("routes_count")
    private Map<String, Integer> routesCount;
}
