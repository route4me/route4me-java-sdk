package com.route4me.sdk.examples.schedules;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.schedules.ScheduleList;
import com.route4me.sdk.services.schedules.SchedulesManager;

/**
 * Deletes a schedule with {@link SchedulesManager#deleteSchedule}, keeping the routes it created. Set
 * {@code SCHEDULE_UID} to a schedule of a test account: the example deletes it.
 */
public class DeleteSchedule {
    public static void main(String[] args) {
        String scheduleUid = System.getenv("SCHEDULE_UID");
        if (scheduleUid == null || scheduleUid.isEmpty()) {
            System.err.println("Set SCHEDULE_UID to the ID of a schedule of a test account; this example deletes it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        SchedulesManager manager = new SchedulesManager(apiKey);
        try {
            ScheduleList result = manager.deleteSchedule(scheduleUid, false);
            System.out.println(result);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
