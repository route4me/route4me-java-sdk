package com.route4me.sdk.examples.podworkflow;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.podworkflow.PodWorkflow;
import com.route4me.sdk.services.podworkflow.PodWorkflowManager;

/**
 * Renames a proof-of-delivery workflow: it reads it with {@link PodWorkflowManager#getPodWorkflow} and sends it back
 * with a new title with {@link PodWorkflowManager#updatePodWorkflow}.
 */
public class UpdatePodWorkflow {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        PodWorkflowManager manager = new PodWorkflowManager(apiKey);
        try {
            String guid = "WORKFLOW_GUID_HERE";
            PodWorkflow workflow = manager.getPodWorkflow(guid).getData();
            workflow.setTitle(workflow.getTitle() + " (updated)");
            PodWorkflow updated = manager.updatePodWorkflow(guid, workflow).getData();
            System.out.println("New title: " + updated.getTitle());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
