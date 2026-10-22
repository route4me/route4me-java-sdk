package com.route4me.sdk.services.activities;

import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * The query of {@link ActivityManager#getActivities(ActivityRequest)} and
 * {@link ActivityManager#getActivityFeed(ActivityRequest)}; fields left {@code null} are not sent.
 */
@Data
@Accessors(chain = true)
public class ActivityRequest {

    /** Unique ID of a route ({@code route_id}). */
    @QueryParameter("route_id")
    private String routeId;
    /** Unique ID of a device attached to a vehicle ({@code device_id}). */
    @QueryParameter("device_id")
    private String deviceId;
    /**
     * Only activities of this type ({@code activity_type}, the name the C#, PHP and Node.js SDKs send to both
     * endpoints).
     */
    @QueryParameter("activity_type")
    private ActivityType type;
    /** Unique ID of a member ({@code member_id}). */
    @QueryParameter("member_id")
    private Integer memberId;
    /** If equal to 'true' the response will include team activities ({@code team}). */
    @QueryParameter("team")
    private Boolean team;
    /** Limit the number of records in response ({@code limit}). */
    @QueryParameter("limit")
    private Integer limit;
    /** Only records from that offset will be considered ({@code offset}). */
    @QueryParameter("offset")
    private Integer offset;
    /** Start of the time filter ({@code start}). */
    @QueryParameter("start")
    private Integer start;
    /** End of the time filter ({@code end}). */
    @QueryParameter("end")
    private Integer end;
}
