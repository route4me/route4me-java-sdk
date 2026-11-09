package com.route4me.sdk.examples.configurationsettings;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.configurationsettings.ConfigurationResponse;
import com.route4me.sdk.services.configurationsettings.ConfigurationSettingsManager;

/**
 * Changes the value of a configuration key with {@link ConfigurationSettingsManager#updateConfigurationKey}. Set
 * {@code CONFIG_KEY} to the key to change, such as {@code sdk_example_key} from {@link CreateConfigurationKey}.
 */
public class UpdateConfigurationKey {
    public static void main(String[] args) {
        String configKey = System.getenv("CONFIG_KEY");
        if (configKey == null || configKey.isEmpty()) {
            System.err.println("Set CONFIG_KEY to the configuration key to change, such as sdk_example_key from "
                    + "CreateConfigurationKey; this example sets its value to 48.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        ConfigurationSettingsManager manager = new ConfigurationSettingsManager(apiKey);
        try {
            ConfigurationResponse result = manager.updateConfigurationKey(configKey, "48");
            System.out.println(result.getResult() + ", affected: " + result.getAffected());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
