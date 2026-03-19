package com.route4me.sdk.integration;

import com.route4me.sdk.services.routing.dynamic.insert.*;
import com.route4me.sdk.exception.APIException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DynamicInsertManagerIntegrationTest extends BaseIntegrationTest {
    private DynamicInsertManager manager;

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new DynamicInsertManager(API_KEY);
    }

    // Dynamic insert requires active routes to search against
    // Manager instantiation validates basic connectivity setup
}
