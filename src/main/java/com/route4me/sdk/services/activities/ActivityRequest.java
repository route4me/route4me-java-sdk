package com.route4me.sdk.services.activities;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ActivityRequest {

    private String routeId;
    private String deviceId;
    private ActivityType type;
    private Integer memberId;
    private boolean team;
    private Integer limit;
    private Integer offset;
    private Integer start;
    private Integer end;
}
