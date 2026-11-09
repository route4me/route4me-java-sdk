package com.route4me.sdk.services.drivers;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;

/**
 * Reads the drivers of the account through the V4 API ({@code https://api.route4me.com/api/drivers}). The V5 API
 * lists the team members, with a filter for drivers ({@code drivers_only}), in
 * {@link com.route4me.sdk.services.users.TeamManagementManagerV5}.
 */
public class DriversManager extends Manager {

    /** The path that lists the drivers, on {@code api.route4me.com}. */
    public static final String VIEW_DRIVERS_EP = "/api/drivers/view_drivers.php";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as the {@code api_key} query parameter
     */
    public DriversManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as the {@code api_key} query parameter
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
    public DriversManager(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    /**
     * Lists the drivers of the account: {@code GET https://api.route4me.com/api/drivers/view_drivers.php}. Only the
     * cURL examples and the Python SDK call this endpoint, and neither documents its answer, so it is returned as
     * text.
     *
     * @return the answer body, as the API sends it (JSON)
     * @throws APIException if the request fails or the server answers with an error status
     */
    public String getDrivers() throws APIException {
        return this.makeRequest(RequestMethod.GET, Manager.defaultBuilder(VIEW_DRIVERS_EP), (HttpEntity) null,
                String.class);
    }
}
