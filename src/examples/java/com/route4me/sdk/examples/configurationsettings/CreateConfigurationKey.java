package com.route4me.sdk.examples.configurationsettings;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.configurationsettings.ConfigurationResponse;
import com.route4me.sdk.services.configurationsettings.ConfigurationSettingsManager;

/**
 * Creates the configuration key {@code sdk_example_key} with
 * {@link ConfigurationSettingsManager#createNewConfigurationKey}. The key is made up for the examples, so no setting
 * of the account changes; {@link UpdateConfigurationKey} and {@link RemoveConfigurationKey} can then change and
 * delete it.
 */
public class CreateConfigurationKey {
    /** A key no Route4Me feature reads. */
    private static final String EXAMPLE_KEY = "sdk_example_key";

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        ConfigurationSettingsManager manager = new ConfigurationSettingsManager(apiKey);
        try {
            ConfigurationResponse result = manager.createNewConfigurationKey(EXAMPLE_KEY, "32");
            System.out.println(result.getResult() + ", affected: " + result.getAffected());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
