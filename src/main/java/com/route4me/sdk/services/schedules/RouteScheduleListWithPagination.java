package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RouteScheduleListWithPagination extends RouteScheduleList {
    @SerializedName("links")
    private PaginationLinks links;

    @SerializedName("meta")
    private PaginationMeta meta;
}
