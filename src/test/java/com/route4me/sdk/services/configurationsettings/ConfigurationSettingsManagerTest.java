package com.route4me.sdk.services.configurationsettings;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link ConfigurationSettingsManager}: the configuration keys of the member on the V4 API.
 */
public class ConfigurationSettingsManagerTest {

    private static final String HOST = "api.route4me.com";
    private static final String EP = "/api.v4/configuration-settings.php";

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private ConfigurationSettingsManager manager() {
        return http.install(new ConfigurationSettingsManager(MockHttp.API_KEY));
    }

    @Test
    public void createNewConfigurationKeyPostsTheKeyAndTheValue() throws Exception {
        ConfigurationSettingsManager manager = manager();
        http.respond(200, json("{'result':'OK','affected':1}"));

        ConfigurationResponse created = manager.createNewConfigurationKey("destination_icon_width", "32");

        http.request().assertEndpoint("POST", HOST, EP).assertApiKeyInQuery()
                .assertContentType("application/json")
                .assertJson("config_key", "destination_icon_width")
                .assertJson("config_value", "32");
        assertEquals("OK", created.getResult());
        assertEquals(Integer.valueOf(1), created.getAffected());
    }

    @Test
    public void getConfigurationDataSendsTheKeyOnlyWhenGiven() throws Exception {
        ConfigurationSettingsManager manager = manager();
        http.respondToAll(200, json("{'result':'OK','affected':1,'data':[{'member_id':1001,"
                + "'config_key':'destination_icon_width','config_value':'32'}]}"));

        ConfigurationDataResponse one = manager.getConfigurationData("destination_icon_width");
        manager.getConfigurationData(null);

        http.request(0).assertEndpoint("GET", HOST, EP).assertApiKeyInQuery()
                .assertQuery("config_key", "destination_icon_width").assertNoBody();
        http.request(1).assertEndpoint("GET", HOST, EP).assertNoQuery("config_key");
        assertEquals(Long.valueOf(1001), one.getData().get(0).getMemberId());
        assertEquals("32", one.getData().get(0).getConfigValue());
        assertFalse(one.getData().get(0).toString().contains("32"));
    }

    @Test
    public void updateConfigurationKeyPutsTheNewValue() throws Exception {
        ConfigurationSettingsManager manager = manager();
        http.respond(200, json("{'result':'OK','affected':1}"));

        manager.updateConfigurationKey("destination_icon_width", "48");

        http.request().assertEndpoint("PUT", HOST, EP)
                .assertJson("config_key", "destination_icon_width")
                .assertJson("config_value", "48");
    }

    @Test
    public void removeConfigurationKeySendsOnlyTheKey() throws Exception {
        ConfigurationSettingsManager manager = manager();
        http.respond(200, json("{'result':'OK','affected':1}"));

        ConfigurationResponse removed = manager.removeConfigurationKey("destination_icon_width");

        http.request().assertEndpoint("DELETE", HOST, EP).assertNoQuery("config_key")
                .assertJson("config_key", "destination_icon_width")
                .assertJsonLacks("config_value");
        assertEquals("OK", removed.getResult());
    }

    @Test
    public void aMissingKeyIsRejectedBeforeSending() {
        final ConfigurationSettingsManager manager = manager();

        http.expectThrows(IllegalArgumentException.class, () -> manager.createNewConfigurationKey(null, "1"));
        http.expectThrows(IllegalArgumentException.class, () -> manager.updateConfigurationKey("", "1"));
        http.expectThrows(IllegalArgumentException.class, () -> manager.removeConfigurationKey(""));
        http.assertNoRequests();
    }

    @Test
    public void disabledRedirectsAddRedirectZero() throws Exception {
        ConfigurationSettingsManager manager = http.install(new ConfigurationSettingsManager(MockHttp.API_KEY, true));
        http.respond(200, json("{'result':'OK','data':[]}"));

        manager.getConfigurationData(null);

        http.request().assertQuery("redirect", "0");
    }

    @Test
    public void errorBecomesApiException() {
        final ConfigurationSettingsManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getConfigurationData(null));
    }
}
