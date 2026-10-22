package com.route4me.sdk.services.optimizationprofile;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

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

    public OptimizationProfileManager(String apiKey) {
        super(apiKey);
    }

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

    public OptimizationProfilesResponse getOptimizationProfiles() throws APIException {
        URIBuilder builder = v5Builder(PROFILES_DATA_LIST_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, OptimizationProfilesResponse.class);
    }

    public OptimizationProfile getOptimizationProfileById(String profileId) throws APIException {
        URIBuilder builder = v5Builder(PROFILES_EP, profileId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, OptimizationProfile.class);
    }

    public OptimizationProfile createOptimizationProfile(OptimizationProfileCreateRequest request) throws APIException {
        URIBuilder builder = v5Builder(PROFILES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), OptimizationProfile.class);
    }

    public OptimizationProfile updateOptimizationProfile(String profileId, OptimizationProfileCreateRequest request)
            throws APIException {
        URIBuilder builder = v5Builder(PROFILES_EP, profileId);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(request), OptimizationProfile.class);
    }

    public boolean deleteOptimizationProfile(String profileId) throws APIException {
        URIBuilder builder = v5Builder(PROFILES_EP, profileId);
        this.makeRequest(RequestMethod.DELETE, builder, "", String.class);
        return true;
    }

    public OptimizationProfileSaveEntities saveEntities(OptimizationProfileSaveEntities request) throws APIException {
        URIBuilder builder = v5Builder(PROFILES_SAVE_ENTITIES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request),
                OptimizationProfileSaveEntities.class);
    }

    public OptimizationProfileSaveEntities deleteEntities(OptimizationProfileDeleteEntitiesRequest request)
            throws APIException {
        URIBuilder builder = v5Builder(PROFILES_DELETE_ENTITIES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request),
                OptimizationProfileSaveEntities.class);
    }

    public OptimizationProfileSaveEntities getEntities(String profileId) throws APIException {
        URIBuilder builder = v5Builder(PROFILES_GET_ENTITIES_EP);
        builder.setParameter("optimization_profile_id", profileId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, OptimizationProfileSaveEntities.class);
    }

    public OptimizationProfileSaveEntities getEntitiesTemplate() throws APIException {
        URIBuilder builder = v5Builder(PROFILES_GET_ENTITIES_TEMPLATE_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, OptimizationProfileSaveEntities.class);
    }
}
