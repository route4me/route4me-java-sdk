package com.route4me.sdk.examples.configurationsettings;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.configurationsettings.ConfigurationResponse;
import com.route4me.sdk.services.configurationsettings.ConfigurationSettingsManager;

/** Deletes a configuration key with {@link ConfigurationSettingsManager#removeConfigurationKey}. */
public class RemoveConfigurationKey {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        ConfigurationSettingsManager manager = new ConfigurationSettingsManager(apiKey);
        try {
            ConfigurationResponse result = manager.removeConfigurationKey("destination_icon_width");
            System.out.println(result.getResult() + ", affected: " + result.getAffected());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
