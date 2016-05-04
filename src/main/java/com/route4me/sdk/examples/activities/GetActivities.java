/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.activities;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.managers.ActivityManager;
import com.route4me.sdk.managers.RouteManager;
import com.route4me.sdk.model.Activities;
import com.route4me.sdk.model.Activity;
import com.route4me.sdk.model.Routes;
import java.util.List;

/**
 *
 * @author juan
 */
public class GetActivities {
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        ActivityManager activityManager = route4me.getActivityManager();
        RouteManager routeManager = route4me.getRouteManager();
        List<Routes> routes = routeManager.getRoutes(10, 5);
        String routeID = routes.get(0).getRoute_id();
        System.out.println("Getting Activities for Route ID: " + routeID);
        Activities responseObject = activityManager.getActivities(10, 5, routeID);
        System.out.println("Total Activities: " + responseObject.getTotal());
        for (Activity activity : responseObject.getResults()) {
            System.out.println("Activity ID: " + activity.getActivity_id());
            System.out.println("Activity Type: " + activity.getActivity_type());
            System.out.println("Activity TimeStamp: " + activity.getActivity_timestamp());
            System.out.println("Activity Message: " + activity.getActivity_message());
            System.out.println("Route ID: " + activity.getRoute_id());
            System.out.println("Route Name: " + activity.getRoute_name());
            System.out.println("Route Destination ID: " + activity.getRoute_destination_id());
            System.out.println("Note ID: " + activity.getNote_id());
            System.out.println("Note Type: " + activity.getNote_type());
            System.out.println("Note Contents: " + activity.getNote_contents());
            System.out.println("Note File: " + activity.getNote_file());
            System.out.println("Member ID: " + activity.getMember().getMember_id());
            System.out.println("Member First Name: " + activity.getMember().getMember_first_name());
            System.out.println("Member Last Name: " + activity.getMember().getMember_last_name());
            System.out.println("Member Email: " + activity.getMember().getMember_email());
        }
    }    
}
