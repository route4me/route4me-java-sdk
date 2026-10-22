package com.route4me.sdk.examples.distance;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.distance.DistanceManager;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Asks for the distance between two addresses with {@link DistanceManager#calculateDistance}. The endpoint's
 * parameter names are not documented: pass them as {@code name=value} arguments.
 */
public class CalculateDistance {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        DistanceManager manager = new DistanceManager(apiKey);
        Map<String, String> query = new LinkedHashMap<>();
        for (String arg : args) {
            int equals = arg.indexOf('=');
            if (equals > 0) {
                query.put(arg.substring(0, equals), arg.substring(equals + 1));
            }
        }
        try {
            System.out.println(manager.calculateDistance(query));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
