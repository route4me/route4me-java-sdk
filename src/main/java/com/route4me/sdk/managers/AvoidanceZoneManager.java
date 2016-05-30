/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.managers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.route4me.sdk.Route4Me;
import com.route4me.sdk.exception.APIConnectionException;
import com.route4me.sdk.exception.InvalidRequestException;
import com.route4me.sdk.model.DataObject;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.model.Routes;
import com.route4me.sdk.model.Territory;
import com.route4me.sdk.serdes.DataObjectDeserializer;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juan
 */
public class AvoidanceZoneManager extends Manager {

    public AvoidanceZoneManager(String apiKey) {
        super(apiKey);
    }

    public Territory addAvoidanceZone(Territory territory) {
        Response response = null;
        String params;
        String jsonData = getGson().toJson(territory);
        try {
            params = Manager.transformParams(getParams());
            response = Manager.makeURLConnectionRequest(RequestMethod.POST, avoidanceURL(), params, jsonData);
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        Territory responseObject = getGson().fromJson(response.getResponseBody(), Territory.class);
        return responseObject;
    }

    public Response deleteAvoidanceZone(String territory_id) {
        Response response = null;
        Map<String, String> params = new HashMap<>();
        params.put("api_key", Route4Me.getApiKey());
        params.put("territory_id", territory_id);
        String strParams;
        try {
            strParams = Manager.transformParams(params);
            response = Manager.makeURLConnectionRequest(RequestMethod.DELETE, avoidanceURL(), strParams);
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    public List<Territory> getAvoidanceZones() {
        Response response = null;
        String params;
        try {
            params = Manager.transformParams(getParams());
            response = Manager.makeURLConnectionRequest(RequestMethod.GET, avoidanceURL(), params);
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<Territory> territories = new ArrayList<>();
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray;
        jsonArray = (JsonArray) jsonParser.parse(response.getResponseBody());
        Iterator<JsonElement> elemIter = jsonArray.iterator();
        while (elemIter.hasNext()) {
            JsonElement elem = elemIter.next();
            territories.add(getGson().fromJson(elem, Territory.class));
        }
        return territories;    }

    public Territory getAvoidanceZone(String territoryID) {
        Response response = null;
        Map<String, String> params = new HashMap<>();
        params.put("api_key", Route4Me.getApiKey());
        params.put("territory_id", territoryID);
        String strParams;
        try {
            strParams = Manager.transformParams(params);
            response = Manager.makeURLConnectionRequest(RequestMethod.GET, avoidanceURL(), strParams);
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        Territory responseObject = getGson().fromJson(response.getResponseBody(), Territory.class);
        return responseObject;
    }

}
