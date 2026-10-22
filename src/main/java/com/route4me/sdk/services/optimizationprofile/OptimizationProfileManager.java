package com.route4me.sdk.services.optimizationprofile;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 * Manages optimization profiles, saved sets of optimization settings, through the V5 API ({@code
 * https://wh.route4me.com/modules/api/v5.0/optimization-profiles}).
 */
public class OptimizationProfileManager extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String PROFILES_EP = V5_BASE + "/optimization-profiles";
    private static final String PROFILES_DATA_LIST_EP = PROFILES_EP + "/data-list";
    private static final String PROFILES_SAVE_ENTITIES_EP = PROFILES_EP + "/save-entities";
    private static final String PROFILES_DELETE_ENTITIES_EP = PROFILES_EP + "/delete-entities";
    private static final String PROFILES_GET_ENTITIES_EP = PROFILES_EP + "/get-entities";
    private static final String PROFILES_GET_ENTITIES_TEMPLATE_EP = PROFILES_EP + "/get-entities-template";
    private static final String PROFILES_LIST_EP = PROFILES_EP + "/list";
    private static final String PROFILES_LIST_CONFIG_EP = PROFILES_EP + "/list/config";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     */
    public OptimizationProfileManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
    public OptimizationProfileManager(String apiKey, boolean disableRedirects) {
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
     * Lists the optimization profiles: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/optimization-profiles/data-list}.
     *
     * @return the profiles
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public OptimizationProfilesResponse getOptimizationProfiles() throws APIException {
        URIBuilder builder = v5Builder(PROFILES_DATA_LIST_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, OptimizationProfilesResponse.class);
    }

    /**
     * Gets one optimization profile: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/optimization-profiles/{profileId}}.
     *
     * @param profileId the profile ID
     * @return the profile
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public OptimizationProfile getOptimizationProfileById(String profileId) throws APIException {
        URIBuilder builder = v5Builder(PROFILES_EP, profileId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, OptimizationProfile.class);
    }

    /**
     * Creates an optimization profile: {@code POST https://wh.route4me.com/modules/api/v5.0/optimization-profiles},
     * with the request as the JSON body.
     *
     * @param request the profile
     * @return the created profile
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public OptimizationProfile createOptimizationProfile(OptimizationProfileCreateRequest request) throws APIException {
        URIBuilder builder = v5Builder(PROFILES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), OptimizationProfile.class);
    }

    /**
     * Updates an optimization profile: {@code PUT
     * https://wh.route4me.com/modules/api/v5.0/optimization-profiles/{profileId}}, with the request as the JSON body.
     *
     * @param profileId the profile ID
     * @param request the new values
     * @return the updated profile
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public OptimizationProfile updateOptimizationProfile(String profileId, OptimizationProfileCreateRequest request)
            throws APIException {
        URIBuilder builder = v5Builder(PROFILES_EP, profileId);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(request), OptimizationProfile.class);
    }

    /**
     * Deletes an optimization profile: {@code DELETE
     * https://wh.route4me.com/modules/api/v5.0/optimization-profiles/{profileId}}.
     *
     * @param profileId the profile ID
     * @return {@code true}; an error status throws instead
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public boolean deleteOptimizationProfile(String profileId) throws APIException {
        URIBuilder builder = v5Builder(PROFILES_EP, profileId);
        this.makeRequest(RequestMethod.DELETE, builder, "", String.class);
        return true;
    }

    /**
     * Saves optimization profile entities: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/optimization-profiles/save-entities}, with the request as the JSON body.
     *
     * @param request the entities
     * @return the saved entities
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public OptimizationProfileSaveEntities saveEntities(OptimizationProfileSaveEntities request) throws APIException {
        URIBuilder builder = v5Builder(PROFILES_SAVE_ENTITIES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request),
                OptimizationProfileSaveEntities.class);
    }

    /**
     * Deletes optimization profile entities: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/optimization-profiles/delete-entities}, with the request as the JSON
     * body.
     *
     * @param request the IDs of the entities
     * @return the answer of the API
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public OptimizationProfileSaveEntities deleteEntities(OptimizationProfileDeleteEntitiesRequest request)
            throws APIException {
        URIBuilder builder = v5Builder(PROFILES_DELETE_ENTITIES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request),
                OptimizationProfileSaveEntities.class);
    }

    /**
     * Gets the entities of an optimization profile: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/optimization-profiles/get-entities?optimization_profile_id=...}.
     *
     * @param profileId the profile ID
     * @return the entities
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public OptimizationProfileSaveEntities getEntities(String profileId) throws APIException {
        URIBuilder builder = v5Builder(PROFILES_GET_ENTITIES_EP);
        builder.setParameter("optimization_profile_id", profileId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, OptimizationProfileSaveEntities.class);
    }

    /**
     * Gets the template of the optimization profile entities: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/optimization-profiles/get-entities-template}.
     *
     * @return the template
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public OptimizationProfileSaveEntities getEntitiesTemplate() throws APIException {
        URIBuilder builder = v5Builder(PROFILES_GET_ENTITIES_TEMPLATE_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, OptimizationProfileSaveEntities.class);
    }
}
