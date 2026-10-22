package com.route4me.sdk.services.authhistory;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 * Reads the authentication history of the members of the account (their log-ins) through the V5 API
 * ({@code https://wh.route4me.com/modules/api/v5.0/auth-history}).
 */
public class AuthHistoryManager extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String API_CONFIG_EP = V5_BASE + "/auth-history/apiconf";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     */
    public AuthHistoryManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
    public AuthHistoryManager(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    private static URIBuilder v5Builder(String endpoint) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        builder.setPath(endpoint);
        return builder;
    }

    /**
     * Gets the configuration of the authentication history API: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/auth-history/apiconf}.
     *
     * @return the fields the history can be sorted and filtered by, and how to load it
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public AuthHistoryApiConfig getApiConfig() throws APIException {
        return this.makeRequest(RequestMethod.GET, v5Builder(API_CONFIG_EP), (HttpEntity) null,
                AuthHistoryApiConfig.class);
    }
}
