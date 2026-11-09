package com.route4me.sdk.services.schedules;

import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;

/**
 * The query of {@link SchedulesManager#getScheduleCalendar(ScheduleCalendarRequest)}: the period, and what to count
 * on each day. Every {@code null} field is left out of the query; a flag is sent as {@code 1} or {@code 0}.
 */
@Data
public class ScheduleCalendarRequest {
    /** The first day of the period, as {@code YYYY-MM-DD} ({@code date_from_string}). */
    @QueryParameter("date_from_string")
    private String dateFromString;

    /** The last day of the period, as {@code YYYY-MM-DD} ({@code date_to_string}). */
    @QueryParameter("date_to_string")
    private String dateToString;

    /** Only the routes, orders and contacts of this member ({@code member_id}). */
    @QueryParameter("member_id")
    private Long memberId;

    /** The offset of the local time zone from UTC, in minutes ({@code timezone_offset_minutes}). */
    @QueryParameter("timezone_offset_minutes")
    private Integer timezoneOffsetMinutes;

    /** {@code true} to count the scheduled orders of each day ({@code orders}). */
    @QueryParameter("orders")
    private Boolean orders;

    /** {@code true} to count the scheduled address book contacts of each day ({@code ab}). */
    @QueryParameter("ab")
    private Boolean addressBook;

    /** {@code true} to count the routes of each day ({@code routes_count}). */
    @QueryParameter("routes_count")
    private Boolean routesCount;
}
