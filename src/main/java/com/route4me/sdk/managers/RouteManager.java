package com.route4me.sdk.managers;

import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.Manager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.route4me.sdk.Route4Me;
import com.route4me.sdk.exception.APIConnectionException;
import com.route4me.sdk.exception.InvalidRequestException;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.model.Routes;
import com.route4me.sdk.model.enums.Constants.RoutePathOutput;
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
public class RouteManager extends Manager {

    public RouteManager(String apiKey) {
        super(apiKey);
    }

    public DataObject getRoute(String optimizationProblemID) {
        Response response = null;
        getParams().put("optimization_problem_id", optimizationProblemID);
        String params;
        try {
            params = Manager.transformParams(getParams());
            response = makeURLConnectionRequest(RequestMethod.GET, super.buildBaseURL(), params);
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
    public Response duplicateRoute(String routeID) {
        Response response = null;
        getParams().put("route_id", routeID);
        String params;
        try {
            params = Manager.transformParams(getParams());
            System.out.println(params);
            response = makeURLConnectionRequest(RequestMethod.GET, super.duplicateRoutetURL(), params);
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    public List<Routes> getRoutes(Integer limit, Integer offset) {
        Response response = null;
        List<Routes> routes = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        params.put("api_key", Route4Me.getApiKey());
        params.put("limit", limit.toString());
        params.put("Offset", offset.toString());
        String strParams;
        try {
            strParams = Manager.transformParams(params);
            response = Manager.makeURLConnectionRequest(RequestMethod.GET, routeHostURL(), strParams);
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray;
        jsonArray = (JsonArray) jsonParser.parse(response.getResponseBody());
        Iterator<JsonElement> elemIter = jsonArray.iterator();
        while (elemIter.hasNext()) {
            JsonElement elem = elemIter.next();
            routes.add(getGson().fromJson(elem, Routes.class));
        }
        return routes;
    }
    

    public Routes updateRouteParameters(DataObject parameters, String routeID) {
        Response response = null;
        String params;
        getParams().put("route_id", routeID);
        String data = getGson().toJson(parameters);
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
        Routes responseObject = getGson().fromJson(response.getResponseBody(), Routes.class);
        return responseObject;
    }

    public Routes updateRoute(Routes parameters, String routeID) {
        Response response = null;
        String params;
        getParams().put("route_id", routeID);
        String data = getGson().toJson(parameters);
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
        Routes responseObject = getGson().fromJson(response.getResponseBody(), Routes.class);
        return responseObject;
    }

    public Routes getRouteManifest(String routeID) {
        Response response = null;
        Map<String, String> params = new HashMap<>();
        params.put("api_key", Route4Me.getApiKey());
        params.put("route_id", routeID);
        params.put("directions", "1");
        params.put("route_path_output", RoutePathOutput.POINTS.toString());
        params.put("device_tracking_history", "1");
        String strParams;
        try {
            strParams = Manager.transformParams(params);
            response = Manager.makeURLConnectionRequest(RequestMethod.GET, routeHostURL(), strParams);
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

        public Routes getRouteTracking(String routeID) {
        Response response = null;
        Map<String, String> params = new HashMap<>();
        params.put("api_key", Route4Me.getApiKey());
        params.put("route_id", routeID);
        params.put("device_tracking_history", "true");
        String strParams;
        try {
            strParams = Manager.transformParams(params);
            response = Manager.makeURLConnectionRequest(RequestMethod.GET, routeHostURL(), strParams);
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
   
    public Routes deleteRoute(String routeID) {
        Response response = null;
        Map<String, String> params = new HashMap<>();
        params.put("api_key", Route4Me.getApiKey());
        params.put("route_id", routeID);
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
        Routes responseObject = getGson().fromJson(response.getResponseBody(), Routes.class);
        return responseObject;
    }
    
    public Routes deleteRoutes(List<String> routeID) {
        Response response = null;
        Map<String, String> params = new HashMap<>();
        params.put("api_key", Route4Me.getApiKey());
        String routeIDs = String.join(",", routeID);
        params.put("route_id", routeIDs);
        System.out.println(routeIDs);
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
        Routes responseObject = getGson().fromJson(response.getResponseBody(), Routes.class);
        return responseObject;
    }

    
}
