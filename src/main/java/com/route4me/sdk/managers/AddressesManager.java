/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.managers;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.exception.APIConnectionException;
import com.route4me.sdk.exception.InvalidRequestException;
import com.route4me.sdk.model.Address;
import com.route4me.sdk.model.DataObject;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.model.Routes;
import com.route4me.sdk.serdes.DataObjectDeserializer;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juan
 */
public class AddressesManager extends Manager {
    
    public AddressesManager(String apiKey) {
        super(apiKey);
    }

    public DataObject addAddressesToOptimizaion(String optimizationProblemID, List<Address> addresses) {
        Response response = null;
        String params;
        getParams().put("optimization_problem_id", optimizationProblemID);
        getParams().put("reoptimize", "1");
        DataObject dataObj = new DataObject();
        dataObj.setAddresses(addresses);
        String data = getGson().toJson(dataObj);
        try {
            params = Manager.transformParams(getParams());
            response = Manager.makeURLConnectionRequest(RequestMethod.PUT, buildBaseURL(), params, data);
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

    public DataObject addAddressesToRoute(String routeID, List<Address> addresses) {
        Response response = null;
        String params;
        getParams().put("route_id", routeID);
        DataObject dataObj = new DataObject();
        dataObj.setAddresses(addresses);
        String data = getGson().toJson(dataObj);
        try {
            params = Manager.transformParams(getParams());
            response = Manager.makeURLConnectionRequest(RequestMethod.PUT, routeHostURL(), params, data);
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
    
    public Address getAddress(String routeID, Number routeDestinationID) {
        Response response = null;
        String params;
        getParams().put("route_id", routeID);
        getParams().put("route_destination_id", routeDestinationID.toString());
        try {
            params = Manager.transformParams(getParams());
            response = Manager.makeURLConnectionRequest(RequestMethod.GET, addressURL(), params);
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        Address responseObject = getGson().fromJson(response.getResponseBody(), Address.class);
        return responseObject;
    }
    
    public Routes moveAddresses(DataObject parameters, String routeID){
        Map<String, String> params = new HashMap<>();
        params.put("route_id", routeID);
        return updateAddress(parameters, params);
    }
    
    private Routes updateAddress(Object parameters, Map<String, String> params) {
        Response response = null;
        params.put("api_key", Route4Me.getApiKey());
        String data = getGson().toJson(parameters);
        String strParams;
        try {
            strParams = Manager.transformParams(params);
            response = Manager.makeURLConnectionRequest(RequestMethod.PUT, routeHostURL(), strParams, data);
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Routes responseObject = getGson().fromJson(response.getResponseBody(), Routes.class);
        return responseObject;
    }    


    public Response deleteAddress(String routeID, Number routeDestinationID) {
        Response response = null;
        Map<String, String> params = new HashMap<>();
        params.put("api_key", Route4Me.getApiKey());
        params.put("route_id", routeID);
        params.put("route_destination_id", routeDestinationID.toString());
        String strParams;
        try {
            strParams = Manager.transformParams(params);
            response = Manager.makeURLConnectionRequest(RequestMethod.DELETE, routeHostURL(), strParams);
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }    

    public Routes updateAddressAttribute(String routeID, Number routeDestinationID, Address dataObj) {
        Map<String, String> params = new HashMap<>();
        params.put("route_id", routeID);
        params.put("route_destination_id", routeDestinationID.toString());
        return updateAddress(dataObj, params);    }


    
}
