package com.route4me.sdk.examples.schedules;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.schedules.RouteSchedule;
import com.route4me.sdk.services.schedules.RouteScheduleList;
import com.route4me.sdk.services.schedules.SchedulesManager;

public class GetRouteSchedules {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        SchedulesManager manager = new SchedulesManager(apiKey);
        try {
            RouteScheduleList list = manager.getRouteSchedules();
            if (list.getData() != null) {
                for (RouteSchedule rs : list.getData()) {
                    System.out.println(rs);
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
