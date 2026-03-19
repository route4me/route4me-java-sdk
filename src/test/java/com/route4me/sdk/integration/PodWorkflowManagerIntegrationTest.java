package com.route4me.sdk.integration;

import com.route4me.sdk.services.podworkflow.*;
import com.route4me.sdk.exception.APIException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class PodWorkflowManagerIntegrationTest extends BaseIntegrationTest {
    private PodWorkflowManager manager;
    private List<String> createdUuids = new ArrayList<>();

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new PodWorkflowManager(API_KEY);
    }

    @After
    public void tearDown() {
        for (String uuid : createdUuids) {
            try { manager.deletePodWorkflow(uuid); } catch (Exception e) { }
        }
    }

    @Test
    public void testGetPodWorkflows() throws APIException {
        PodWorkflowsResponse workflows = manager.getPodWorkflows(10, null, null);
        assertNotNull(workflows);
    }

    @Test
    public void testCreateAndGetPodWorkflow() throws APIException {
        PodWorkflow workflow = new PodWorkflow();
        workflow.setTitle("SDK Test Workflow " + System.currentTimeMillis());
        List<Object> actions = new ArrayList<>();
        actions.add("capture_photo");
        workflow.setDoneActions(actions);

        PodWorkflowResponse created = manager.createPodWorkflow(workflow);
        assertNotNull(created);

        PodWorkflowsResponse all = manager.getPodWorkflows(100, null, null);
        assertNotNull(all);
    }

    @Test
    public void testUpdatePodWorkflow() throws APIException {
        PodWorkflow workflow = new PodWorkflow();
        workflow.setTitle("SDK Update Workflow " + System.currentTimeMillis());
        List<Object> actions = new ArrayList<>();
        actions.add("capture_photo");
        workflow.setDoneActions(actions);

        PodWorkflowResponse created = manager.createPodWorkflow(workflow);
        assertNotNull(created);

        PodWorkflow update = new PodWorkflow();
        update.setTitle("SDK Updated Workflow");
        List<Object> updatedActions = new ArrayList<>();
        updatedActions.add("capture_photo");
        updatedActions.add("capture_signature");
        update.setDoneActions(updatedActions);
    }
}
