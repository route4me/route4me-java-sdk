package com.route4me.sdk.examples.podworkflow;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.podworkflow.PodWorkflow;
import com.route4me.sdk.services.podworkflow.PodWorkflowManager;

/** Reads one proof-of-delivery workflow with {@link PodWorkflowManager#getPodWorkflow}. */
public class GetPodWorkflow {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        PodWorkflowManager manager = new PodWorkflowManager(apiKey);
        try {
            PodWorkflow workflow = manager.getPodWorkflow("WORKFLOW_GUID_HERE").getData();
            System.out.println(workflow.getTitle() + ", enabled: " + workflow.getIsEnabled());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
