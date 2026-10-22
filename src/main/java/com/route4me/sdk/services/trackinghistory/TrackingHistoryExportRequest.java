package com.route4me.sdk.services.trackinghistory;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * The body of {@link TrackingHistoryManager#exportTrackingHistory(TrackingHistoryExportRequest)}; fields left
 * {@code null} are not sent.
 */
@Data
@Accessors(chain = true)
public class TrackingHistoryExportRequest {
    /** The device whose history to export ({@code device_id}). */
    @SerializedName("device_id")
    private String deviceId;
    /** The member whose history to export ({@code member_id}). */
    @SerializedName("member_id")
    private Long memberId;
    /** The route whose history to export ({@code route_id}). */
    @SerializedName("route_id")
    private String routeId;
    /** The format of the file: {@code csv}, {@code json} or {@code xml} ({@code format}). */
    @SerializedName("format")
    private String format;
    /** A named period, such as {@code today}, {@code yesterday} or {@code 7days} ({@code time_period}). */
    @SerializedName("time_period")
    private String timePeriod;
    /** The start of the period, as a Unix timestamp ({@code start_date}). */
    @SerializedName("start_date")
    private Long startDate;
    /** The end of the period, as a Unix timestamp ({@code end_date}). */
    @SerializedName("end_date")
    private Long endDate;
    /** Whether to export only the last position ({@code last_position}). */
    @SerializedName("last_position")
    private Boolean lastPosition;
    /** Whether to include the custom data of each position ({@code show_cd}). */
    @SerializedName("show_cd")
    private Boolean showCustomData;
    /** Whether to include the map-matching data ({@code show_mmd}). */
    @SerializedName("show_mmd")
    private Boolean showMapMatchingData;
    /** Whether to answer with a file download ({@code download}). */
    @SerializedName("download")
    private Boolean download;
    /** The time zone of the times in the file, such as {@code America/New_York} ({@code tz}). */
    @SerializedName("tz")
    private String timeZone;
    /** The columns of a CSV file ({@code columns}). */
    @SerializedName("columns")
    private List<String> columns;
}
