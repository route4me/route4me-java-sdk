package com.route4me.sdk.examples.optimizations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.OptimizationParameters;
import com.route4me.sdk.services.routing.RoutingManager;

import java.util.Arrays;

/**
 * Adds two stops to an existing optimization and re-optimizes it with {@link RoutingManager#updateOptimization}:
 * the optimization ID and {@code reoptimize=1} go in the query, the new addresses in the body.
 */
public class UpdateOptimization {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        OptimizationParameters request = new OptimizationParameters();
        request.setProblemId("OPTIMIZATION_PROBLEM_ID_HERE");
        request.setReoptimize(true);
        request.setAddresses(Arrays.asList(
                new Address("318 SO. 39TH STREET, Louisville, KY, 40212", 38.259335, -85.815094, 300),
                new Address("1324 BLUEGRASS AVE, Louisville, KY, 40215", 38.179253, -85.785118, 300)));
        try {
            DataObject optimization = manager.updateOptimization(request);
            System.out.println("Optimization Problem ID: " + optimization.getOptimizationProblemId());
            if (optimization.getAddresses() != null) {
                for (Address address : optimization.getAddresses()) {
                    System.out.println(address.getAddress());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
