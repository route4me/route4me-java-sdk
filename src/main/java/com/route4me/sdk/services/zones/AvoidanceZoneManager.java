package com.route4me.sdk.services.zones;

import com.google.gson.Gson;
import com.route4me.sdk.Manager;
import com.google.gson.reflect.TypeToken;
import com.route4me.sdk.responses.DeleteResponse;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.model.enums.APIEndpoints;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

public class AvoidanceZoneManager extends Manager {

    public AvoidanceZoneManager(String apiKey) {
        super(apiKey, new Gson());
    }

    public Territory addAvoidanceZone(Territory territory) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(APIEndpoints.AVOIDANCE);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(territory), Territory.class);
    }

    public boolean deleteAvoidanceZone(String territoryId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(APIEndpoints.AVOIDANCE);
        builder.setParameter("territory_id", territoryId);
        return this.makeRequest(RequestMethod.POST, builder, "", DeleteResponse.class).isDeleted();
    }

    public List<Territory> getAvoidanceZones() throws APIException {
        URIBuilder builder = Manager.defaultBuilder(APIEndpoints.AVOIDANCE);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, new TypeToken<ArrayList<Territory>>() {
        }.getType());
    }

    public Territory getAvoidanceZone(String territoryId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(APIEndpoints.AVOIDANCE);
        builder.setParameter("territory_id", territoryId);
        return this.makeRequest(RequestMethod.GET, builder, "", Territory.class);
    }

}
