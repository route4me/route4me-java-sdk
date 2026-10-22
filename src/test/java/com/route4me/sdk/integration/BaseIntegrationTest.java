package com.route4me.sdk.integration;

import org.junit.Assume;
import org.junit.Before;

public abstract class BaseIntegrationTest {
    protected static final String API_KEY = System.getenv("R4M_API_KEY");

    @Before
    public void checkApiKey() {
        Assume.assumeNotNull(API_KEY);
        Assume.assumeFalse("11111111111111111111111111111111".equals(API_KEY));
    }
}
