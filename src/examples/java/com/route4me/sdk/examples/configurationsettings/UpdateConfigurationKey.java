package com.route4me.sdk.examples.configurationsettings;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.configurationsettings.ConfigurationResponse;
import com.route4me.sdk.services.configurationsettings.ConfigurationSettingsManager;

/** Changes the value of a configuration key with {@link ConfigurationSettingsManager#updateConfigurationKey}. */
public class UpdateConfigurationKey {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        ConfigurationSettingsManager manager = new ConfigurationSettingsManager(apiKey);
        try {
            ConfigurationResponse result = manager.updateConfigurationKey("destination_icon_width", "48");
            System.out.println(result.getResult() + ", affected: " + result.getAffected());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
