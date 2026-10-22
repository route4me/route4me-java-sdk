package com.route4me.sdk.examples.podworkflow;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.podworkflow.PodWorkflowManager;
import com.route4me.sdk.services.podworkflow.StatusResponse;

/** Deletes a proof-of-delivery workflow with {@link PodWorkflowManager#deletePodWorkflow}. */
public class DeletePodWorkflow {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        PodWorkflowManager manager = new PodWorkflowManager(apiKey);
        try {
            String uuid = "WORKFLOW_GUID_HERE";
            StatusResponse response = manager.deletePodWorkflow(uuid);
            System.out.println("Deleted: " + response.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
