package com.route4me.sdk.examples.schedules;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.schedules.SchedulesManager;
import com.route4me.sdk.services.schedules.StatusResponse;

/** Tells whether a route was created by a schedule, with {@link SchedulesManager#isRouteScheduleCopied}. */
public class IsRouteScheduleCopied {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        SchedulesManager manager = new SchedulesManager(apiKey);
        try {
            StatusResponse copied = manager.isRouteScheduleCopied("ROUTE_ID_HERE");
            System.out.println("Created by a schedule: " + copied.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
