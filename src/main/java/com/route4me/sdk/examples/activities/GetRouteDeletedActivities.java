// codebeat:disable[SIMILARITY]
package com.route4me.sdk.examples.activities;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.activities.Activities;
import com.route4me.sdk.services.activities.ActivityManager;
import com.route4me.sdk.services.activities.ActivityRequest;
import com.route4me.sdk.services.activities.ActivityType;

public class GetRouteDeletedActivities {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        ActivityManager manager = new ActivityManager(apiKey);
        try {
            Activities activities = manager.getActivities(new ActivityRequest().setType(ActivityType.ROUTE_DELETE));
            System.out.println(activities.getResults());
        }catch(APIException e) {
            e.printStackTrace();
        }
    }
}
// codebeat:enable[SIMILARITY]
