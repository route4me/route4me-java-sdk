package com.route4me.sdk.services.configurationsettings;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link ConfigurationSettingsManagerV5}: the member configuration settings on the V5 API.
 */
public class ConfigurationSettingsManagerV5Test {

    private static final String HOST = "wh.route4me.com";
    private static final String EP = "/modules/api/v5.0/member-configuration-settings";
    /** A made-up organization key; only its place in the request matters. */
    private static final String ORGANIZATION_KEY = "example-organization-key";

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private ConfigurationSettingsManagerV5 manager() {
        return http.install(new ConfigurationSettingsManagerV5(MockHttp.API_KEY));
    }

    @Test
    public void getMemberConfigurationSettingsSendsOnlyTheGivenFilters() throws Exception {
        ConfigurationSettingsManagerV5 manager = manager();
        http.respondToAll(200, json("{'status':'success','data':[{'member_id':1001,'config_key':'map_style',"
                + "'config_value':'dark','is_hidden_value':false,'can_unhide_value':true}]}"));

        MemberConfigurationSettingsResponse settings = manager.getMemberConfigurationSettings("ui", "map_style",
                ORGANIZATION_KEY);
        manager.getMemberConfigurationSettings(null, null, null);

        http.request(0).assertEndpoint("GET", HOST, EP).assertApiKeyAsBearer().assertNoBody()
                .assertQuery("mcs", "ui")
                .assertQuery("config_key", "map_style")
                .assertQuery("organization_api_key", ORGANIZATION_KEY);
        http.request(1).assertEndpoint("GET", HOST, EP).assertNoQuery("mcs").assertNoQuery("config_key")
                .assertNoQuery("organization_api_key");
        MemberConfigurationSetting setting = settings.getData().get(0);
        assertEquals("success", settings.getStatus());
        assertEquals("map_style", setting.getConfigKey());
        assertEquals("dark", setting.getConfigValue());
        assertFalse(setting.getIsHiddenValue());
        assertTrue(setting.getCanUnhideValue());
        assertFalse(setting.toString().contains("dark"));
    }

    @Test
    public void anErrorDoesNotEchoTheOrganizationKey() {
        final ConfigurationSettingsManagerV5 manager = manager();
        http.respondWithFixture(400, "error_400.json");

        APIException error = http.expectApiException(
                () -> manager.getMemberConfigurationSettings(null, null, ORGANIZATION_KEY));

        assertFalse(error.getMessage().contains(ORGANIZATION_KEY));
    }

    @Test
    public void disabledRedirectsAddRedirectZero() throws Exception {
        ConfigurationSettingsManagerV5 manager = http.install(new ConfigurationSettingsManagerV5(MockHttp.API_KEY,
                true));
        http.respond(200, json("{'status':'success','data':[]}"));

        manager.getMemberConfigurationSettings(null, null, null);

        http.request().assertQuery("redirect", "0");
    }
}
