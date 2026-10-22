package com.route4me.sdk.examples.schedules;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.schedules.Schedule;
import com.route4me.sdk.services.schedules.ScheduleList;
import com.route4me.sdk.services.schedules.SchedulesManager;

/** Lists the schedules with {@link SchedulesManager#getSchedules}. */
public class GetSchedules {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        SchedulesManager manager = new SchedulesManager(apiKey);
        try {
            ScheduleList schedules = manager.getSchedules();
            if (schedules.getData() != null) {
                for (Schedule schedule : schedules.getData()) {
                    System.out.println(schedule);
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
