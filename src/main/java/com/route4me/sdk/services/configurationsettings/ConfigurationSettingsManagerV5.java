package com.route4me.sdk.services.configurationsettings;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 * Reads the configuration settings of the member through the V5 API
 * ({@code https://wh.route4me.com/modules/api/v5.0/member-configuration-settings}). The V4 keys are in
 * {@link ConfigurationSettingsManager}.
 */
public class ConfigurationSettingsManagerV5 extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String MEMBER_CONFIGURATION_SETTINGS_EP = V5_BASE + "/member-configuration-settings";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     */
    public ConfigurationSettingsManagerV5(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
    public ConfigurationSettingsManagerV5(String apiKey, boolean disableRedirects) {
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
     * Lists the configuration settings of the member: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/member-configuration-settings}. Every {@code null} argument is left
     * out of the query.
     *
     * <p>The spec puts {@code organization_api_key} in the query. It is a credential: the SDK hides it in its
     * exception messages, but proxies and server logs may keep URLs, so send it only when you need the settings of
     * every member of an organization.
     *
     * @param mcs the configuration settings key ({@code mcs})
     * @param configKey only the setting with this key, up to 64 characters ({@code config_key})
     * @param organizationApiKey the API key of an organization, to list the settings of all its members
     *     ({@code organization_api_key})
     * @return the settings
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public MemberConfigurationSettingsResponse getMemberConfigurationSettings(String mcs, String configKey,
            String organizationApiKey) throws APIException {
        URIBuilder builder = v5Builder(MEMBER_CONFIGURATION_SETTINGS_EP);
        if (mcs != null) {
            builder.setParameter("mcs", mcs);
        }
        if (configKey != null) {
            builder.setParameter("config_key", configKey);
        }
        if (organizationApiKey != null) {
            builder.setParameter("organization_api_key", organizationApiKey);
        }
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null,
                MemberConfigurationSettingsResponse.class);
    }
}
