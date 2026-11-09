package com.route4me.sdk.examples.schedules;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.schedules.Schedule;
import com.route4me.sdk.services.schedules.ScheduleBlacklistItem;
import com.route4me.sdk.services.schedules.ScheduleList;
import com.route4me.sdk.services.schedules.SchedulesManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Creates a schedule of recurring routes, every Monday, Wednesday and Friday until the end of the year, except over
 * Christmas, with {@link SchedulesManager#createSchedule}.
 */
public class CreateSchedule {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        SchedulesManager manager = new SchedulesManager(apiKey);
        try {
            Schedule schedule = new Schedule();
            schedule.setName("Test Schedule");
            schedule.setTimezone("America/New_York");
            schedule.setAdvanceInterval(7);
            schedule.setAdvanceScheduleIntervalDays(14);
            schedule.setScheduleData(weekdays());
            schedule.setScheduleBlacklist(
                    Collections.singletonList(new ScheduleBlacklistItem(true, "2026-12-24", "2026-12-26")));
            ScheduleList result = manager.createSchedule(schedule);
            System.out.println(result);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }

    /** Builds the recurrence rules: every Monday, Wednesday and Friday, from November 2 to December 31, 2026. */
    static List<Map<String, Object>> weekdays() {
        Map<String, Object> weekly = new HashMap<>();
        weekly.put("every", 1);
        weekly.put("weekdays", Arrays.asList(1, 3, 5));
        Map<String, Object> until = new HashMap<>();
        until.put("mode", "date");
        until.put("untilDate", "2026-12-31");
        Map<String, Object> rule = new HashMap<>();
        rule.put("enabled", true);
        rule.put("mode", "weekly");
        rule.put("weekly", weekly);
        rule.put("from", "2026-11-02");
        rule.put("to", until);
        return Collections.singletonList(rule);
    }
}
