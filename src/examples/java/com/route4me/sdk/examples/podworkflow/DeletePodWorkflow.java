package com.route4me.sdk.examples.podworkflow;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.podworkflow.PodWorkflowManager;
import com.route4me.sdk.services.podworkflow.StatusResponse;

/**
 * Deletes a proof-of-delivery workflow with {@link PodWorkflowManager#deletePodWorkflow}. Set {@code WORKFLOW_ID} to
 * the GUID of a workflow of a test account: the example deletes it.
 */
public class DeletePodWorkflow {
    public static void main(String[] args) {
        String uuid = System.getenv("WORKFLOW_ID");
        if (uuid == null || uuid.isEmpty()) {
            System.err.println("Set WORKFLOW_ID to the GUID of a proof-of-delivery workflow of a test account; this"
                    + " example deletes it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        PodWorkflowManager manager = new PodWorkflowManager(apiKey);
        try {
            StatusResponse response = manager.deletePodWorkflow(uuid);
            System.out.println("Deleted: " + response.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
