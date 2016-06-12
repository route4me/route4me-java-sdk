package com.route4me.sdk.managers;

import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.Manager;
import com.route4me.sdk.Route4Me;
import com.route4me.sdk.exception.APIConnectionException;
import com.route4me.sdk.exception.InvalidRequestException;
import com.route4me.sdk.model.Response;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juan
 */
public class TrackingManager extends Manager {

    public TrackingManager(String apiKey) {
        super(apiKey);
    }

    public Response getLastLocation() {
        Response response = null;
        String params;
        try {
            params = transformParams(getParams());
            response = makeURLConnectionRequest(RequestMethod.GET, routeHostURL(), params);
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
    
    
    
    public Response setGPSPosition() {
        Response response = null;
        String params;
        try {
            params = Manager.transformParams(getParams());
            response = makeURLConnectionRequest(RequestMethod.GET, setGPSURL(), params);
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
    
}
