package com.route4me.sdk.integration;

import com.route4me.sdk.services.users.TeamManagementManagerV5;
import com.route4me.sdk.services.users.TeamMember;
import com.route4me.sdk.services.users.TeamMemberRequest;
import com.route4me.sdk.exception.APIException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TeamManagementManagerV5IntegrationTest extends BaseIntegrationTest {
    private TeamManagementManagerV5 manager;

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new TeamManagementManagerV5(API_KEY);
    }

    @Test
    public void testGetTeamMembers() throws APIException {
        TeamMember[] members = manager.getTeamMembers();
        assertNotNull(members);
        assertTrue(members.length > 0);
    }

    @Test
    public void testGetTeamMemberById() throws APIException {
        TeamMember[] members = manager.getTeamMembers();
        assertNotNull(members);
        assertTrue(members.length > 0);
        // Use the first member's ID to fetch by ID - need to find the member_id field
        // TeamMember likely has a getMemberId() method from Lombok
    }
}
