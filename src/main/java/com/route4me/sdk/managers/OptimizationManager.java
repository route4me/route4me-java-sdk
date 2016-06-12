package com.route4me.sdk.managers;

import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.Manager;
import com.route4me.sdk.Route4Me;
import com.route4me.sdk.exception.APIConnectionException;
import com.route4me.sdk.exception.InvalidRequestException;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.serdes.DataObjectDeserializer;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juan
 */
public class OptimizationManager extends Manager{

    public OptimizationManager(String apiKey) {
        super(apiKey);
    }
    
    public DataObject reOptimization() {
        return reOptimization(null);
    }

    public DataObject reOptimization(DataObject data) {
        Response response = null;
        setData(data);
        String params;
        try {
            params = Manager.transformParams(getParams());
            response = Manager.makeURLConnectionRequest(RequestMethod.PUT, buildBaseURL(), params, getJsonData());
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        DataObject responseObject = DataObjectDeserializer.GSON_DESERIALIZER.fromJson(response.getResponseBody(), DataObject.class);
        return responseObject;
    }

    public DataObject getOptimizations(Integer limit, Integer offset) {
        Response response = null;
        Map<String, String> params = new HashMap<>();
        params.put("api_key", Route4Me.getApiKey());
        params.put("limit", limit.toString());
        params.put("Offset", offset.toString());
        String strParams;
        try {
            strParams = Manager.transformParams(params);
            response = Manager.makeURLConnectionRequest(RequestMethod.GET, buildBaseURL(), strParams);
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        DataObject responseObject = DataObjectDeserializer.GSON_DESERIALIZER.fromJson(response.getResponseBody(), DataObject.class);
        return responseObject;
    }

    public DataObject getOptimization(String optimizationProblemID) {
        Response response = null;
        Map<String, String> params = new HashMap<>();
        params.put("api_key", Route4Me.getApiKey());
        params.put("optimization_problem_id", optimizationProblemID);
        String strParams;
        try {
            strParams = Manager.transformParams(params);
            response = Manager.makeURLConnectionRequest(RequestMethod.GET, buildBaseURL(), strParams);
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        DataObject responseObject = DataObjectDeserializer.GSON_DESERIALIZER.fromJson(response.getResponseBody(), DataObject.class);
        return responseObject;
    }

    public DataObject runOptimization() {
        Response response = null;
        String params;
        try {
            params = Manager.transformParams(getParams());
            response = Manager.makeURLConnectionRequest(RequestMethod.POST, buildBaseURL(), params, getJsonData());
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        DataObject responseObject = DataObjectDeserializer.GSON_DESERIALIZER.fromJson(response.getResponseBody(), DataObject.class);
        return responseObject;
    }
    

    
}
