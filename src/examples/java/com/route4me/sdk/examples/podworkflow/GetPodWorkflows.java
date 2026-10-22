package com.route4me.sdk.examples.podworkflow;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.podworkflow.PodWorkflow;
import com.route4me.sdk.services.podworkflow.PodWorkflowManager;
import com.route4me.sdk.services.podworkflow.PodWorkflowsResponse;

public class GetPodWorkflows {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        PodWorkflowManager manager = new PodWorkflowManager(apiKey);
        try {
            PodWorkflowsResponse response = manager.getPodWorkflows(10, null, null);
            if (response.getData() != null) {
                for (PodWorkflow wf : response.getData()) {
                    System.out.println("Workflow: " + wf.getTitle() + " (GUID: " + wf.getWorkflowGuid() + ")");
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
