package com.route4me.sdk.services.podworkflow;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 * Manages the proof-of-delivery (POD) workflows of the account, the actions a driver must or may take to complete
 * or fail a stop, through the V5 API ({@code https://wh.route4me.com/modules/api/v5.0/workflows}).
 */
public class PodWorkflowManager extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String WORKFLOWS_EP = V5_BASE + "/workflows";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     */
    public PodWorkflowManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
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

    /**
     * Lists the POD workflows, one page at a time: {@code GET https://wh.route4me.com/modules/api/v5.0/workflows}.
     * Every {@code null} argument is left out of the query.
     *
     * @param perPage the number of workflows per page ({@code per_page})
     * @param cursor the {@code next_page_cursor} of the previous page ({@code cursor})
     * @param searchQuery text to search for in the workflows ({@code search_query})
     * @return the page of workflows, with the cursor of the next one
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
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

    /**
     * Gets one POD workflow: {@code GET https://wh.route4me.com/modules/api/v5.0/workflows/{uuid}}.
     *
     * @param uuid the workflow GUID
     * @return the workflow
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public PodWorkflowResponse getPodWorkflow(String uuid) throws APIException {
        URIBuilder builder = v5Builder(WORKFLOWS_EP, uuid);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, PodWorkflowResponse.class);
    }

    /**
     * Creates a POD workflow: {@code POST https://wh.route4me.com/modules/api/v5.0/workflows}, with the workflow as the
     * JSON body.
     *
     * @param workflow the workflow
     * @return the created workflow
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public PodWorkflowResponse createPodWorkflow(PodWorkflow workflow) throws APIException {
        URIBuilder builder = v5Builder(WORKFLOWS_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(workflow), PodWorkflowResponse.class);
    }

    /**
     * Updates a POD workflow: {@code PUT https://wh.route4me.com/modules/api/v5.0/workflows/{uuid}}, with the workflow
     * as the JSON body.
     *
     * @param uuid the workflow GUID
     * @param workflow the new values of the workflow
     * @return the updated workflow
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public PodWorkflowResponse updatePodWorkflow(String uuid, PodWorkflow workflow) throws APIException {
        URIBuilder builder = v5Builder(WORKFLOWS_EP, uuid);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(workflow), PodWorkflowResponse.class);
    }

    /**
     * Deletes a POD workflow: {@code DELETE https://wh.route4me.com/modules/api/v5.0/workflows/{uuid}}.
     *
     * @param uuid the workflow GUID
     * @return whether it was deleted
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public StatusResponse deletePodWorkflow(String uuid) throws APIException {
        URIBuilder builder = v5Builder(WORKFLOWS_EP, uuid);
        return this.makeRequest(RequestMethod.DELETE, builder, "", StatusResponse.class);
    }
}
