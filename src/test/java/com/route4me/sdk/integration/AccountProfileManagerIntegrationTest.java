package com.route4me.sdk.integration;

import com.route4me.sdk.services.accountprofile.AccountProfileManager;
import com.route4me.sdk.services.accountprofile.AccountProfile;
import com.route4me.sdk.exception.APIException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountProfileManagerIntegrationTest extends BaseIntegrationTest {
    private AccountProfileManager manager;

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new AccountProfileManager(API_KEY);
    }

    @Test
    public void testGetAccountProfile() throws APIException {
        AccountProfile profile = manager.getAccountProfile();
        assertNotNull(profile);
    }
}
