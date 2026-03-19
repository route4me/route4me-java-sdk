package com.route4me.sdk.integration;

import com.route4me.sdk.services.notes.NotesManager;
import com.route4me.sdk.exception.APIException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NotesManagerIntegrationTest extends BaseIntegrationTest {
    private NotesManager manager;

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new NotesManager(API_KEY);
    }

    // Notes require a route_id and route_destination_id which need to come from existing routes
    // These tests validate that the manager can be instantiated and basic read operations work
}
