package com.route4me.sdk.examples.podworkflow;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.podworkflow.PodWorkflow;
import com.route4me.sdk.services.podworkflow.PodWorkflowManager;
import com.route4me.sdk.services.podworkflow.PodWorkflowsResponse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Lists the proof-of-delivery workflows sorted by title, page by page, with
 * {@link PodWorkflowManager#getPodWorkflows(Integer, String, String, List)}.
 */
public class GetPodWorkflowsSorted {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        PodWorkflowManager manager = new PodWorkflowManager(apiKey);
        List<List<String>> byTitle = Collections.singletonList(Arrays.asList("title", "asc"));
        try {
            String cursor = null;
            do {
                PodWorkflowsResponse page = manager.getPodWorkflows(20, cursor, null, byTitle);
                if (page.getData() != null) {
                    for (PodWorkflow workflow : page.getData()) {
                        System.out.println(workflow.getTitle() + " (GUID: " + workflow.getWorkflowGuid() + ")");
                    }
                }
                cursor = page.getNextPageCursor();
            } while (cursor != null);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
