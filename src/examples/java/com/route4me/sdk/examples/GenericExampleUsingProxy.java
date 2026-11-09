package com.route4me.sdk.examples;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.RoutingManager;

import java.io.IOException;
import java.util.List;

/**
 * Lists optimizations through an HTTP proxy: a {@link RoutingManager} built with a proxy host and port calls
 * {@link RoutingManager#getOptimizations}.
 */
public class GenericExampleUsingProxy {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("Set R4M_API_KEY to your Route4Me API key.");
            return;
        }
        String proxyHost = "YOUR_PROXY_HOST";
        int proxyPort = 8080;
        String proxySchema = "https";
        // A manager holds a pool of HTTP connections: close it when you are done.
        try (RoutingManager manager = new RoutingManager(apiKey, proxyHost, proxyPort, proxySchema)) {
            List<DataObject> responseObject = manager.getOptimizations(0, 5);
            if (responseObject == null || responseObject.isEmpty()) {
                System.out.println("No optimizations found");
                return;
            }
            DataObject opt = responseObject.get(0);
            System.out.println("Optimization ID: " + opt.getOptimizationProblemId());
            if (opt.getAddresses() != null) {
                for (Address address : opt.getAddresses()) {
                    System.out.println(address);
                }
            }
        } catch (APIException | IOException e) {
            //handle exception
            e.printStackTrace();
        }

    }
}
