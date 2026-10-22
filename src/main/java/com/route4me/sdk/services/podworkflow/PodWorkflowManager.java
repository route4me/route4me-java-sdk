package com.route4me.sdk.services.podworkflow;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

public class PodWorkflowManager extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String WORKFLOWS_EP = V5_BASE + "/workflows";

    public PodWorkflowManager(String apiKey) {
        super(apiKey);
    }

    public PodWorkflowManager(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    private static URIBuilder v5Builder(String endpoint, Object... segments) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        Manager.withPathSegments(builder, endpoint, segments);
        return builder;
    }

    public PodWorkflowsResponse getPodWorkflows(Integer perPage, String cursor, String searchQuery)
            throws APIException {
        URIBuilder builder = v5Builder(WORKFLOWS_EP);
        if (perPage != null) {
            builder.setParameter("per_page", String.valueOf(perPage));
        }
        if (cursor != null) {
            builder.setParameter("cursor", cursor);
        }
        if (searchQuery != null) {
            builder.setParameter("search_query", searchQuery);
        }
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, PodWorkflowsResponse.class);
    }

    public PodWorkflowResponse getPodWorkflow(String uuid) throws APIException {
        URIBuilder builder = v5Builder(WORKFLOWS_EP, uuid);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, PodWorkflowResponse.class);
    }

    public PodWorkflowResponse createPodWorkflow(PodWorkflow workflow) throws APIException {
        URIBuilder builder = v5Builder(WORKFLOWS_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(workflow), PodWorkflowResponse.class);
    }

    public PodWorkflowResponse updatePodWorkflow(String uuid, PodWorkflow workflow) throws APIException {
        URIBuilder builder = v5Builder(WORKFLOWS_EP, uuid);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(workflow), PodWorkflowResponse.class);
    }

    public StatusResponse deletePodWorkflow(String uuid) throws APIException {
        URIBuilder builder = v5Builder(WORKFLOWS_EP, uuid);
        return this.makeRequest(RequestMethod.DELETE, builder, "", StatusResponse.class);
    }
}
