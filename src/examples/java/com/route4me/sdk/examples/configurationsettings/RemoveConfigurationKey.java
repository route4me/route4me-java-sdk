package com.route4me.sdk.examples.configurationsettings;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.configurationsettings.ConfigurationResponse;
import com.route4me.sdk.services.configurationsettings.ConfigurationSettingsManager;

/**
 * Deletes a configuration key with {@link ConfigurationSettingsManager#removeConfigurationKey}. Set
 * {@code CONFIG_KEY} to the key to delete, such as {@code sdk_example_key} from {@link CreateConfigurationKey}.
 */
public class RemoveConfigurationKey {
    public static void main(String[] args) {
        String configKey = System.getenv("CONFIG_KEY");
        if (configKey == null || configKey.isEmpty()) {
            System.err.println("Set CONFIG_KEY to the configuration key to delete, such as sdk_example_key from "
                    + "CreateConfigurationKey; this example deletes it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        ConfigurationSettingsManager manager = new ConfigurationSettingsManager(apiKey);
        try {
            ConfigurationResponse result = manager.removeConfigurationKey(configKey);
            System.out.println(result.getResult() + ", affected: " + result.getAffected());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
