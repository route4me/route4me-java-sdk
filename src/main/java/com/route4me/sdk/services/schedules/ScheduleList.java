package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class ScheduleList {
    @SerializedName("data")
    private List<Schedule> data;
}
