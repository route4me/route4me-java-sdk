package com.route4me.sdk.examples.schedules;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.schedules.ScheduleCalendarRequest;
import com.route4me.sdk.services.schedules.ScheduleCalendarResponse;
import com.route4me.sdk.services.schedules.SchedulesManager;

/**
 * Counts the scheduled contacts, orders and routes of each day of a week with
 * {@link SchedulesManager#getScheduleCalendar}.
 */
public class GetScheduleCalendar {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        SchedulesManager manager = new SchedulesManager(apiKey);
        ScheduleCalendarRequest request = new ScheduleCalendarRequest();
        request.setDateFromString("2026-10-19");
        request.setDateToString("2026-10-25");
        request.setTimezoneOffsetMinutes(-240);
        request.setOrders(true);
        request.setAddressBook(true);
        request.setRoutesCount(true);
        try {
            ScheduleCalendarResponse calendar = manager.getScheduleCalendar(request);
            System.out.println("Contacts per day: " + calendar.getAddressBook());
            System.out.println("Orders per day: " + calendar.getOrders());
            System.out.println("Routes per day: " + calendar.getRoutesCount());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
