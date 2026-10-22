package com.route4me.sdk.services.podworkflow;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link PodWorkflowManager}: proof-of-delivery workflows on the V5 API.
 */
public class PodWorkflowManagerTest {

    private static final String HOST = "wh.route4me.com";
    private static final String WORKFLOWS_EP = "/modules/api/v5.0/workflows";
    private static final String WORKFLOW = json("{'workflow_guid':'WF-1','workflow_id':'delivery',"
            + "'title':'Delivery','is_enabled':true,'is_default':false,'done_actions':[],'failed_actions':[]}");

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private PodWorkflowManager manager() {
        return http.install(new PodWorkflowManager(MockHttp.API_KEY));
    }

    /** Builds a sample POD workflow with fake data. */
    private static PodWorkflow workflow() {
        PodWorkflow workflow = new PodWorkflow();
        workflow.setWorkflowId("delivery");
        workflow.setTitle("Delivery");
        workflow.setIsEnabled(true);
        workflow.setDoneActions(Collections.<Object>singletonList(Collections.singletonMap("type", "signature")));
        return workflow;
    }

    @Test
    public void getPodWorkflowsSendsOnlyTheGivenFilters() throws Exception {
        PodWorkflowManager manager = manager();
        http.respondToAll(200, "{\"data\":[" + WORKFLOW + "],\"next_page_cursor\":\"CUR-2\",\"total_items_count\":1}");

        PodWorkflowsResponse page = manager.getPodWorkflows(10, "CUR-1", "Deliv");
        manager.getPodWorkflows(null, null, null);

        http.request(0).assertEndpoint("GET", HOST, WORKFLOWS_EP).assertApiKeyAsBearer()
                .assertQuery("per_page", "10").assertQuery("cursor", "CUR-1").assertQuery("search_query", "Deliv");
        http.request(1).assertNoQuery("per_page").assertNoQuery("cursor").assertNoQuery("search_query");
        assertEquals("WF-1", page.getData().get(0).getWorkflowGuid());
        assertEquals(Integer.valueOf(1), page.getTotalItemsCount());
    }

    @Test
    public void getCreateUpdateAndDeleteUseTheUuid() throws Exception {
        PodWorkflowManager manager = manager();
        http.respond(200, "{\"data\":" + WORKFLOW + "}").respond(200, "{\"data\":" + WORKFLOW + "}")
                .respond(200, "{\"data\":" + WORKFLOW + "}").respond(200, json("{'status':true}"));

        PodWorkflowResponse fetched = manager.getPodWorkflow("WF-1");
        manager.createPodWorkflow(workflow());
        manager.updatePodWorkflow("WF-1", workflow());
        StatusResponse deleted = manager.deletePodWorkflow("WF-1");

        http.request(0).assertEndpoint("GET", HOST, WORKFLOWS_EP + "/WF-1");
        http.request(1).assertEndpoint("POST", HOST, WORKFLOWS_EP)
                .assertJson("workflow_id", "delivery")
                .assertJson("title", "Delivery")
                .assertJson("is_enabled", true)
                .assertJson("done_actions[0].type", "signature");
        http.request(2).assertEndpoint("PUT", HOST, WORKFLOWS_EP + "/WF-1").assertJson("title", "Delivery");
        http.request(3).assertEndpoint("DELETE", HOST, WORKFLOWS_EP + "/WF-1").assertNoBody();
        assertEquals("Delivery", fetched.getData().getTitle());
        assertTrue(deleted.getStatus());
    }

    @Test
    public void getPodWorkflowsSendsTheOrderWithBrackets() throws Exception {
        PodWorkflowManager manager = manager();
        http.respondToAll(200, "{\"data\":[],\"total_items_count\":0}");

        manager.getPodWorkflows(10, null, null,
                Arrays.asList(Arrays.asList("title", "asc"), Arrays.asList("workflow_id", "desc")));
        manager.getPodWorkflows(null, null, null, null);

        http.request(0).assertEndpoint("GET", HOST, WORKFLOWS_EP).assertQuery("per_page", "10")
                .assertQueryValues("order_by[0][]", "title", "asc")
                .assertQueryValues("order_by[1][]", "workflow_id", "desc")
                .assertNoQuery("order_by")
                .assertNoQuery("cursor");
        http.request(1).assertNoQuery("order_by[0][]");
    }

    @Test
    public void createAndUpdateRequireTheSpecFields() {
        final PodWorkflowManager manager = manager();
        final PodWorkflow noId = workflow();
        noId.setWorkflowId("");
        final PodWorkflow noTitle = workflow();
        noTitle.setTitle(null);
        final PodWorkflow noEnabled = workflow();
        noEnabled.setIsEnabled(null);

        http.expectThrows(IllegalArgumentException.class, () -> manager.createPodWorkflow(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.createPodWorkflow(noId));
        http.expectThrows(IllegalArgumentException.class, () -> manager.createPodWorkflow(noTitle));
        http.expectThrows(IllegalArgumentException.class, () -> manager.updatePodWorkflow("WF-1", noEnabled));
        http.assertNoRequests();
    }

    @Test
    public void deletePodWorkflowAcceptsANoContentReply() throws Exception {
        PodWorkflowManager manager = manager();
        http.respondWithoutBody(204);

        StatusResponse deleted = manager.deletePodWorkflow("WF-1");

        http.request().assertEndpoint("DELETE", HOST, WORKFLOWS_EP + "/WF-1").assertNoBody();
        assertNull(deleted);
    }

    @Test
    public void workflowIdsAreRejectedWhenEmptyAndEncodedAsOneSegment() throws Exception {
        final PodWorkflowManager manager = manager();

        http.expectThrows(IllegalArgumentException.class, () -> manager.getPodWorkflow(""));
        http.expectThrows(IllegalArgumentException.class, () -> manager.updatePodWorkflow(null, workflow()));
        http.expectThrows(IllegalArgumentException.class, () -> manager.deletePodWorkflow(""));
        http.assertNoRequests();

        http.respondToAll(200, "{\"data\":" + WORKFLOW + "}");
        manager.updatePodWorkflow("a/b", workflow());
        manager.deletePodWorkflow("a/b");

        assertEquals(WORKFLOWS_EP + "/a%2Fb", http.request(0).getUri().getRawPath());
        assertEquals(WORKFLOWS_EP + "/a%2Fb", http.request(1).getUri().getRawPath());
    }

    @Test
    public void disabledRedirectsAddRedirectZero() throws Exception {
        PodWorkflowManager manager = http.install(new PodWorkflowManager(MockHttp.API_KEY, true));
        http.respond(200, "{\"data\":" + WORKFLOW + "}");

        manager.getPodWorkflow("WF-1");

        http.request().assertQuery("redirect", "0");
    }

    @Test
    public void errorBecomesApiException() {
        final PodWorkflowManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getPodWorkflow("WF-1"));
    }
}
