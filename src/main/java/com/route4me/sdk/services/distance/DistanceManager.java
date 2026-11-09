package com.route4me.sdk.services.distance;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 * Calculates the driving distance between two addresses through the V4 API ({@code
 * https://api.route4me.com/api/distance.php}).
 *
 * <p>No OpenAPI spec and no other SDK documents this endpoint, so its parameter names are not known; the query is
 * passed through as given, as the Python SDK does.
 */
public class DistanceManager extends Manager {

    private static final String DISTANCE_EP = "/api/distance.php";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as the {@code api_key} query parameter
     */
    public DistanceManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Calculates the distance between a start and an end address: {@code GET
     * https://api.route4me.com/api/distance.php}, with the parameters as the query. Without a valid start and end
     * address the API answers with the plain text {@code Missing or invalid start and end address.}
     *
     * @param parameters the query parameters, sent as given; {@code null} sends none. The API key is added by the
     *                   manager.
     * @return the answer, as text
     * @throws APIException if the request fails or the server answers with an error status
     * @throws IllegalArgumentException if the parameters hold an {@code api_key}
     */
    public String calculateDistance(Map<String, String> parameters) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(DISTANCE_EP);
        if (parameters != null) {
            for (Map.Entry<String, String> parameter : parameters.entrySet()) {
                if ("api_key".equalsIgnoreCase(parameter.getKey())) {
                    throw new IllegalArgumentException("The manager adds the API key; do not pass api_key");
                }
                builder.addParameter(parameter.getKey(), parameter.getValue());
            }
        }
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, String.class);
    }
}
