package com.route4me.sdk.examples.schedules;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.schedules.Schedule;
import com.route4me.sdk.services.schedules.ScheduleList;
import com.route4me.sdk.services.schedules.SchedulesManager;

/** Creates a schedule of recurring routes with {@link SchedulesManager#createSchedule}. */
public class CreateSchedule {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        SchedulesManager manager = new SchedulesManager(apiKey);
        try {
            Schedule schedule = new Schedule();
            schedule.setName("Test Schedule");
            schedule.setTimezone("America/New_York");
            ScheduleList result = manager.createSchedule(schedule);
            System.out.println(result);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
