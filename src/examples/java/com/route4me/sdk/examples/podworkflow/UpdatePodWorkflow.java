package com.route4me.sdk.examples.podworkflow;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.podworkflow.PodWorkflow;
import com.route4me.sdk.services.podworkflow.PodWorkflowManager;

/**
 * Renames a proof-of-delivery workflow: it reads it with {@link PodWorkflowManager#getPodWorkflow} and sends it back
 * with a new title with {@link PodWorkflowManager#updatePodWorkflow}. Set {@code WORKFLOW_ID} to the GUID of a
 * workflow of a test account: the example renames it.
 */
public class UpdatePodWorkflow {
    public static void main(String[] args) {
        String guid = System.getenv("WORKFLOW_ID");
        if (guid == null || guid.isEmpty()) {
            System.err.println("Set WORKFLOW_ID to the GUID of a proof-of-delivery workflow of a test account; this"
                    + " example renames it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        PodWorkflowManager manager = new PodWorkflowManager(apiKey);
        try {
            PodWorkflow workflow = manager.getPodWorkflow(guid).getData();
            workflow.setTitle(workflow.getTitle() + " (updated)");
            PodWorkflow updated = manager.updatePodWorkflow(guid, workflow).getData();
            System.out.println("New title: " + updated.getTitle());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
