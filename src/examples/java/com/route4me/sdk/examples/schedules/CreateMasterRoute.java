package com.route4me.sdk.examples.schedules;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.schedules.CreateMasterRouteRequest;
import com.route4me.sdk.services.schedules.CreateMasterRouteResponse;
import com.route4me.sdk.services.schedules.SchedulesManager;

/** Makes a route the master route of a new schedule with {@link SchedulesManager#createMasterRoute}. */
public class CreateMasterRoute {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        SchedulesManager manager = new SchedulesManager(apiKey);
        try {
            CreateMasterRouteRequest request = new CreateMasterRouteRequest();
            request.setRouteId("ROUTE_ID_HERE");
            request.setRouteName("Master Route");
            request.setName("Weekly Schedule");
            request.setTimezone("America/New_York");
            request.setScheduleData(CreateSchedule.weekdays());
            request.setSync(true);
            CreateMasterRouteResponse result = manager.createMasterRoute(request);
            System.out.println(result);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
