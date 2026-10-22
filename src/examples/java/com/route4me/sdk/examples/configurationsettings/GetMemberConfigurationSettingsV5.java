package com.route4me.sdk.examples.configurationsettings;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.configurationsettings.ConfigurationSettingsManagerV5;
import com.route4me.sdk.services.configurationsettings.MemberConfigurationSetting;
import com.route4me.sdk.services.configurationsettings.MemberConfigurationSettingsResponse;

/**
 * Lists the configuration settings of the member through the V5 API with
 * {@link ConfigurationSettingsManagerV5#getMemberConfigurationSettings}. It prints the keys, not the values, which can
 * hold secrets.
 */
public class GetMemberConfigurationSettingsV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        ConfigurationSettingsManagerV5 manager = new ConfigurationSettingsManagerV5(apiKey);
        try {
            MemberConfigurationSettingsResponse settings = manager.getMemberConfigurationSettings(null, null, null);
            if (settings.getData() != null) {
                for (MemberConfigurationSetting setting : settings.getData()) {
                    System.out.println(setting.getConfigKey() + " (hidden: " + setting.getIsHiddenValue() + ")");
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
