package com.route4me.sdk.services.accountprofile;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 * Reads the profile of the account through the V5 API
 * ({@code https://wh.route4me.com/modules/api/v5.0/profile-api}).
 */
public class AccountProfileManager extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String ACCOUNT_PROFILE_EP = V5_BASE + "/profile-api";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     */
    public AccountProfileManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey           the Route4Me API key, sent as a bearer token
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
    public AccountProfileManager(String apiKey, boolean disableRedirects) {
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
     * Gets the profile of the account that owns the API key:
     * {@code GET https://wh.route4me.com/modules/api/v5.0/profile-api}.
     *
     * <p>The answer can contain the API key of the account; do not log it.
     *
     * @return the profile
     * @throws APIException if the request fails or the answer cannot be parsed
     */
    public AccountProfile getAccountProfile() throws APIException {
        URIBuilder builder = v5Builder(ACCOUNT_PROFILE_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, AccountProfile.class);
    }
}
