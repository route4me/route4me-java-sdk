package com.route4me.sdk.integration;

import com.route4me.sdk.services.users.UsersManager;
import com.route4me.sdk.services.users.User;
import com.route4me.sdk.exception.APIException;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class UsersManagerIntegrationTest extends BaseIntegrationTest {
    private UsersManager manager;

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new UsersManager(API_KEY);
    }

    @Test
    public void testGetUsers() throws APIException {
        List<User> users = manager.getUsers(10, 0);
        assertNotNull(users);
        assertFalse(users.isEmpty());
    }
}
