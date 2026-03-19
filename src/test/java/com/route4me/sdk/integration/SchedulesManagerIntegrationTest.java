package com.route4me.sdk.integration;

import com.route4me.sdk.services.schedules.*;
import com.route4me.sdk.exception.APIException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class SchedulesManagerIntegrationTest extends BaseIntegrationTest {
    private SchedulesManager manager;
    private List<String> createdScheduleUids = new ArrayList<>();

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new SchedulesManager(API_KEY);
    }

    @After
    public void tearDown() {
        for (String uid : createdScheduleUids) {
            try { manager.deleteSchedule(uid, false); } catch (Exception e) { }
        }
    }

    private Schedule buildSchedule() {
        Schedule schedule = new Schedule();
        schedule.setName("SDK Test Schedule " + System.currentTimeMillis());
        schedule.setTimezone("America/New_York");
        return schedule;
    }

    @Test
    public void testCreateAndGetSchedule() throws APIException {
        ScheduleList created = manager.createSchedule(buildSchedule());
        assertNotNull(created);

        ScheduleList fetched = manager.getSchedules();
        assertNotNull(fetched);
    }

    @Test
    public void testGetSchedules() throws APIException {
        ScheduleList schedules = manager.getSchedules();
        assertNotNull(schedules);
    }

    @Test
    public void testGetSchedulesWithPagination() throws APIException {
        ScheduleListWithPagination schedules = manager.getSchedulesWithPagination(1, 10);
        assertNotNull(schedules);
    }

    @Test
    public void testGetRouteSchedules() throws APIException {
        RouteScheduleList schedules = manager.getRouteSchedules();
        assertNotNull(schedules);
    }

    @Test
    public void testGetRouteSchedulesWithPagination() throws APIException {
        RouteScheduleListWithPagination schedules = manager.getRouteSchedulesWithPagination(1, 10);
        assertNotNull(schedules);
    }
}
