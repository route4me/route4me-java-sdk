package com.route4me.sdk.integration;

import com.route4me.sdk.services.routing.predict.optimization.time.*;
import com.route4me.sdk.services.routing.*;
import com.route4me.sdk.exception.APIException;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class PredictOptimizationTimeManagerIntegrationTest extends BaseIntegrationTest {
    private PredictOptimizationTimeManager manager;

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new PredictOptimizationTimeManager(API_KEY);
    }

    @Test
    public void testPredictOptimizationTime() throws APIException {
        OptimizationParameters params = new OptimizationParameters();
        Parameters routeParams = new Parameters();
        routeParams.setRouteName("SDK Predict Test");
        routeParams.setAlgorithmType("1");
        routeParams.setDistanceUnit("mi");
        routeParams.setTravelMode("Driving");
        params.setParameters(routeParams);

        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address("754 5th Ave New York, NY 10019", true, "Depot", 40.7636197, -73.9744060, 0));
        addresses.add(new Address("717 5th Ave New York, NY 10022", false, "Stop 1", 40.7669692, -73.9693864, 300));
        addresses.add(new Address("888 Madison Ave New York, NY 10014", false, "Stop 2", 40.7715154, -73.9669241, 300));
        params.setAddresses(addresses);

        OptimizationTimePrediction prediction = manager.runOptimizationPrediction(params);
        assertNotNull(prediction);
    }
}
