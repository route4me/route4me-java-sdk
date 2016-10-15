package com.route4me.sdk.services.activities;

import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ActivityRequest {

    @QueryParameter("route_id")
    private String routeId;
    @QueryParameter("device_id")
    private String deviceId;
    @QueryParameter("type")
    private ActivityType type;
    @QueryParameter("member_id")
    private Integer memberId;
    @QueryParameter("team")
    private Boolean team;
    @QueryParameter("limit")
    private Integer limit;
    @QueryParameter("offset")
    private Integer offset;
    @QueryParameter("start")
    private Integer start;
    @QueryParameter("end")
    private Integer end;
}
