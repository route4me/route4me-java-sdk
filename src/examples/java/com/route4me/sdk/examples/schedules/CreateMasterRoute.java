package com.route4me.sdk.examples.schedules;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.schedules.CreateMasterRouteRequest;
import com.route4me.sdk.services.schedules.CreateMasterRouteResponse;
import com.route4me.sdk.services.schedules.SchedulesManager;

/**
 * Makes a route the master route of a new weekly schedule with {@link SchedulesManager#createMasterRoute}. Set
 * {@code ROUTE_ID} to a route of a test account: the example schedules it.
 */
public class CreateMasterRoute {
    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of a route of a test account; this example makes it the master"
                    + " route of a new schedule.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        SchedulesManager manager = new SchedulesManager(apiKey);
        try {
            CreateMasterRouteRequest request = new CreateMasterRouteRequest();
            request.setRouteId(routeId);
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
