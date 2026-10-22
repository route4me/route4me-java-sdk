package com.route4me.sdk.services.tracking;

import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * The query of {@link TrackingManager#getDeviceLocationHistory(DeviceLocationHistoryRequest)}; fields left {@code null}
 * are not sent.
 */
@Data
@Accessors(chain = true)
public class DeviceLocationHistoryRequest {

    /** The route whose devices to read ({@code route_id}). */
    @QueryParameter("route_id")
    private String routeId;
    /** The member whose device to read ({@code member_id}). */
    @QueryParameter("member_id")
    private Long memberId;
    /**
     * The period: {@code today}, {@code yesterday}, {@code thismonth}, {@code 7days}, {@code 14days}, {@code 30days},
     * {@code 60days}, {@code 90days}, {@code all_time}, or {@code custom} with {@code start_date} and {@code end_date}
     * ({@code time_period}).
     */
    @QueryParameter("time_period")
    private String timePeriod;
    /** The start of a {@code custom} period, as a Unix timestamp ({@code start_date}). */
    @QueryParameter("start_date")
    private Long startDate;
    /** The end of a {@code custom} period, as a Unix timestamp ({@code end_date}). */
    @QueryParameter("end_date")
    private Long endDate;
    /** Whether to return only the last position ({@code last_position}, sent as 1 or 0). */
    @QueryParameter("last_position")
    private Boolean lastPosition;
}
