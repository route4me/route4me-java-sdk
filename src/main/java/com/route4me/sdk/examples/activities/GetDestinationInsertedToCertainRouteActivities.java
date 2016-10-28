package com.route4me.sdk.examples.activities;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.activities.Activities;
import com.route4me.sdk.services.activities.ActivityManager;
import com.route4me.sdk.services.activities.ActivityRequest;
import com.route4me.sdk.services.activities.ActivityType;

public class GetDestinationInsertedToCertainRouteActivities {
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        ActivityManager manager = new ActivityManager(apiKey);
        try {
            Activities activities = manager.getActivities(new ActivityRequest().setType(ActivityType.INSERT_DESTINATION).setRouteId("5C15E83A4BE005BCD1537955D28D51D7"));
            System.out.println(activities.getResults());
        }catch(APIException e) {
            e.printStackTrace();
        }
    }
}
