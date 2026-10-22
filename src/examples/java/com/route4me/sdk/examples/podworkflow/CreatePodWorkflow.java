package com.route4me.sdk.examples.podworkflow;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.podworkflow.PodWorkflow;
import com.route4me.sdk.services.podworkflow.PodWorkflowManager;
import com.route4me.sdk.services.podworkflow.PodWorkflowResponse;

/** Creates a proof-of-delivery workflow with {@link PodWorkflowManager#createPodWorkflow}. */
public class CreatePodWorkflow {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        PodWorkflowManager manager = new PodWorkflowManager(apiKey);
        try {
            PodWorkflow workflow = new PodWorkflow();
            workflow.setTitle("Test POD Workflow");
            workflow.setIsEnabled(true);
            workflow.setIsDefault(false);
            PodWorkflowResponse response = manager.createPodWorkflow(workflow);
            System.out.println("Created workflow: " + response.getData().getTitle());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
