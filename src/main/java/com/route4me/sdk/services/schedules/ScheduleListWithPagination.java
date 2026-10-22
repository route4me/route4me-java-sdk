package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ScheduleListWithPagination extends ScheduleList {
    @SerializedName("links")
    private PaginationLinks links;

    @SerializedName("meta")
    private PaginationMeta meta;
}
