package com.route4me.sdk.services.accountprofile;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link AccountProfileManager}: the account profile on the V5 API.
 */
public class AccountProfileManagerTest {

    @Rule
    public final MockHttp http = new MockHttp();

    @Test
    public void getAccountProfileReadsTheProfile() throws Exception {
        AccountProfileManager manager = http.install(new AccountProfileManager(MockHttp.API_KEY));
        http.respond(200, json("{'email':'owner@example.com','member_id':1001,'root_member_id':1000,"
                + "'preferred_units':'mi'}"));

        AccountProfile profile = manager.getAccountProfile();

        http.request().assertEndpoint("GET", "wh.route4me.com", "/modules/api/v5.0/profile-api")
                .assertApiKeyAsBearer().assertNoBody();
        assertEquals("owner@example.com", profile.getEmail());
        assertEquals(Long.valueOf(1001), profile.getMemberId());
        assertEquals("mi", profile.getPreferredUnits());
    }

    @Test
    public void disabledRedirectsAddRedirectZero() throws Exception {
        AccountProfileManager manager = http.install(new AccountProfileManager(MockHttp.API_KEY, true));
        http.respond(200, "{}");

        manager.getAccountProfile();

        http.request().assertQuery("redirect", "0");
    }

    @Test
    public void errorBecomesApiException() {
        final AccountProfileManager manager = http.install(new AccountProfileManager(MockHttp.API_KEY));
        http.respond(401, json("{'errors':['Unauthenticated']}"));

        http.expectApiException(manager::getAccountProfile);
    }
}
