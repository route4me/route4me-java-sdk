package com.route4me.sdk.examples.configurationsettings;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.configurationsettings.ConfigurationData;
import com.route4me.sdk.services.configurationsettings.ConfigurationDataResponse;
import com.route4me.sdk.services.configurationsettings.ConfigurationSettingsManager;

/**
 * Lists the configuration keys of the member with {@link ConfigurationSettingsManager#getConfigurationData}; pass a
 * key name instead of {@code null} to get one key. The values are not printed: a setting can hold a secret, such as
 * the token of an integration.
 */
public class GetConfigurationData {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        ConfigurationSettingsManager manager = new ConfigurationSettingsManager(apiKey);
        try {
            ConfigurationDataResponse keys = manager.getConfigurationData(null);
            if (keys != null && keys.getData() != null) {
                for (ConfigurationData key : keys.getData()) {
                    System.out.println(key.getConfigKey());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
