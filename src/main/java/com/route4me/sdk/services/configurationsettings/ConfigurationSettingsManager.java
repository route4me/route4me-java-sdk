package com.route4me.sdk.services.configurationsettings;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manages the configuration keys of the member, key-value pairs stored on the account, through the V4 API
 * ({@code https://api.route4me.com/api.v4/configuration-settings.php}). The requests are those of the C# SDK
 * ({@code Route4MeManager}) and of the PHP SDK; no spec documents this endpoint.
 */
public class ConfigurationSettingsManager extends Manager {

    /** The path of the configuration keys, on {@code api.route4me.com}. */
    public static final String CONFIGURATION_SETTINGS_EP = "/api.v4/configuration-settings.php";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as the {@code api_key} query parameter
     */
    public ConfigurationSettingsManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as the {@code api_key} query parameter
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
    public ConfigurationSettingsManager(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    /**
     * Creates a configuration key: {@code POST https://api.route4me.com/api.v4/configuration-settings.php}, with
     * {@code config_key} and {@code config_value} as the JSON body.
     *
     * @param configKey the name of the key
     * @param configValue its value
     * @return {@code result} {@code OK} and the number of keys created
     * @throws IllegalArgumentException if {@code configKey} is {@code null} or empty
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public ConfigurationResponse createNewConfigurationKey(String configKey, String configValue) throws APIException {
        return this.makeRequest(RequestMethod.POST, Manager.defaultBuilder(CONFIGURATION_SETTINGS_EP),
                body(configKey, configValue), ConfigurationResponse.class);
    }

    /**
     * Gets one configuration key, or all of them: {@code GET
     * https://api.route4me.com/api.v4/configuration-settings.php?config_key=...}.
     *
     * @param configKey the name of the key; {@code null} lists every key of the member
     * @return the keys and their values
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public ConfigurationDataResponse getConfigurationData(String configKey) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(CONFIGURATION_SETTINGS_EP);
        if (configKey != null) {
            builder.setParameter("config_key", configKey);
        }
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, ConfigurationDataResponse.class);
    }

    /**
     * Changes the value of a configuration key: {@code PUT
     * https://api.route4me.com/api.v4/configuration-settings.php}, with {@code config_key} and {@code config_value}
     * as the JSON body.
     *
     * @param configKey the name of the key
     * @param configValue its new value
     * @return {@code result} {@code OK} and the number of keys changed
     * @throws IllegalArgumentException if {@code configKey} is {@code null} or empty
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public ConfigurationResponse updateConfigurationKey(String configKey, String configValue) throws APIException {
        return this.makeRequest(RequestMethod.PUT, Manager.defaultBuilder(CONFIGURATION_SETTINGS_EP),
                body(configKey, configValue), ConfigurationResponse.class);
    }

    /**
     * Deletes a configuration key: {@code DELETE https://api.route4me.com/api.v4/configuration-settings.php}, with
     * {@code config_key} as the JSON body.
     *
     * @param configKey the name of the key
     * @return {@code result} {@code OK} and the number of keys deleted
     * @throws IllegalArgumentException if {@code configKey} is {@code null} or empty
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public ConfigurationResponse removeConfigurationKey(String configKey) throws APIException {
        return this.makeRequest(RequestMethod.DELETE, Manager.defaultBuilder(CONFIGURATION_SETTINGS_EP),
                body(configKey, null), ConfigurationResponse.class);
    }

    /** Builds the JSON body of a key, leaving a {@code null} value out; the key itself is required. */
    private String body(String configKey, String configValue) {
        if (configKey == null || configKey.isEmpty()) {
            throw new IllegalArgumentException("The configuration key must not be null or empty");
        }
        Map<String, String> body = new LinkedHashMap<>();
        body.put("config_key", configKey);
        if (configValue != null) {
            body.put("config_value", configValue);
        }
        return this.gson.toJson(body);
    }
}
