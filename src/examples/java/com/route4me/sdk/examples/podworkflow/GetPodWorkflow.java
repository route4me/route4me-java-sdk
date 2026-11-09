package com.route4me.sdk.examples.podworkflow;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.podworkflow.PodWorkflow;
import com.route4me.sdk.services.podworkflow.PodWorkflowManager;

/**
 * Reads one proof-of-delivery workflow with {@link PodWorkflowManager#getPodWorkflow}. Set {@code WORKFLOW_ID} to the
 * GUID of one of your workflows.
 */
public class GetPodWorkflow {
    public static void main(String[] args) {
        String guid = System.getenv("WORKFLOW_ID");
        if (guid == null || guid.isEmpty()) {
            System.err.println("Set WORKFLOW_ID to the GUID of one of your proof-of-delivery workflows; this example"
                    + " reads it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        PodWorkflowManager manager = new PodWorkflowManager(apiKey);
        try {
            PodWorkflow workflow = manager.getPodWorkflow(guid).getData();
            System.out.println(workflow.getTitle() + ", enabled: " + workflow.getIsEnabled());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
