package com.route4me.sdk.examples.schedules;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.schedules.ScheduleList;
import com.route4me.sdk.services.schedules.SchedulesManager;

public class DeleteSchedule {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        SchedulesManager manager = new SchedulesManager(apiKey);
        try {
            String scheduleUid = "SCHEDULE_UID_HERE";
            ScheduleList result = manager.deleteSchedule(scheduleUid, false);
            System.out.println(result);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
