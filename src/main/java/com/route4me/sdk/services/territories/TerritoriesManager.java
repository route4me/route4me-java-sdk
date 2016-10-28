package com.route4me.sdk.services.territories;

import com.google.gson.reflect.TypeToken;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.responses.DeleteResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

import java.util.ArrayList;
import java.util.List;

public class TerritoriesManager extends Manager {
    public static final String AVOIDANCE_EP = "/api.v4/avoidance.php";
    public static final String TERRITORY_EP = "/api.v4/territory.php";

    public TerritoriesManager(String apiKey) {
        super(apiKey);
    }

    public Territory addAvoidanceZone(Territory territory) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(AVOIDANCE_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(territory), Territory.class);
    }

    public boolean deleteAvoidanceZone(String territoryId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(AVOIDANCE_EP);
        builder.setParameter("territory_id", territoryId);
        return this.makeRequest(RequestMethod.DELETE, builder, "", DeleteResponse.class).isDeleted();
    }

    public Territory updateAvoidanceZone(Territory territory) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(AVOIDANCE_EP);
        return this.makeJSONRequest(RequestMethod.DELETE, builder, territory, Territory.class);
    }

    public List<Territory> getAvoidanceZones() throws APIException {
        URIBuilder builder = Manager.defaultBuilder(AVOIDANCE_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, new TypeToken<ArrayList<Territory>>() {
        }.getType());
    }

    public Territory getAvoidanceZone(String territoryId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(AVOIDANCE_EP);
        builder.setParameter("territory_id", territoryId);
        return this.makeRequest(RequestMethod.GET, builder, "", Territory.class);
    }

    public Territory addTerritory(Territory territory) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(TERRITORY_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(territory), Territory.class);
    }

    public boolean deleteTerritory(String territoryId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(TERRITORY_EP);
        builder.setParameter("territory_id", territoryId);
        return this.makeRequest(RequestMethod.DELETE, builder, "", DeleteResponse.class).isDeleted();
    }

    public Territory updateTerritory(Territory territory) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(TERRITORY_EP);
        return this.makeJSONRequest(RequestMethod.DELETE, builder, territory, Territory.class);
    }

    public List<Territory> getTerritories() throws APIException {
        URIBuilder builder = Manager.defaultBuilder(TERRITORY_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, new TypeToken<ArrayList<Territory>>() {
        }.getType());
    }

    public Territory getTerritory(String territoryId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(TERRITORY_EP);
        builder.setParameter("territory_id", territoryId);
        return this.makeRequest(RequestMethod.GET, builder, "", Territory.class);
    }

}
