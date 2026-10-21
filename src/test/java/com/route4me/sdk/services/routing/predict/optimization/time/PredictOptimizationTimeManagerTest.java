package com.route4me.sdk.services.routing.predict.optimization.time;

import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.OptimizationParameters;
import com.route4me.sdk.services.routing.Parameters;
import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link PredictOptimizationTimeManager}: predicting how long an
 * optimization will take.
 */
public class PredictOptimizationTimeManagerTest {

    @Rule
    public final MockHttp http = new MockHttp();

    private PredictOptimizationTimeManager manager() {
        return http.install(new PredictOptimizationTimeManager(MockHttp.API_KEY));
    }

    private static OptimizationParameters request() {
        Parameters parameters = new Parameters();
        parameters.setRouteName("Morning run");
        OptimizationParameters request = new OptimizationParameters();
        request.setParameters(parameters);
        request.setAddresses(Arrays.asList(new Address("100 Example St", 40.1, -74.1),
                new Address("200 Sample Ave", 40.2, -74.2)));
        return request;
    }

    @Test
    public void runOptimizationPredictionPostsTheProblem() throws Exception {
        PredictOptimizationTimeManager manager = manager();
        http.respond(200, json("{'time-prediction':[{'model':'linear','value':42,'unit':'seconds'}]}"));

        OptimizationTimePrediction prediction = manager.runOptimizationPrediction(request());

        http.request().assertEndpoint("POST", "api.route4me.com", "/api.v4/predict-optimization-time.php")
                .assertApiKeyInQuery()
                .assertContentType("application/json")
                .assertJson("parameters.route_name", "Morning run")
                .assertJson("addresses[1].address", "200 Sample Ave");
        TimePredictionModel model = prediction.getTimePrediction().get(0);
        assertEquals("linear", model.getModel());
        assertEquals(Long.valueOf(42), model.getValue());
        assertEquals("seconds", model.getUnit());
    }

    @Test
    public void errorBecomesApiException() {
        final PredictOptimizationTimeManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.runOptimizationPrediction(request()));
    }
}
