/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.managers;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.exception.APIConnectionException;
import com.route4me.sdk.exception.InvalidRequestException;
import com.route4me.sdk.model.Contact;
import com.route4me.sdk.model.Notes;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.model.enums.Constants;
import com.route4me.sdk.serdes.ContactDeserializer;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author juan
 */
public class NotesManager extends Manager {

    public NotesManager(String apiKey) {
        super(apiKey);
    }

    public Response getAddressNotes(String routeID, String routeDestinationID) {
        Response response = null;
        Map<String, String> params = new HashMap<>();
        params.put("api_key", Route4Me.getApiKey());
        params.put("route_id", routeID);
        params.put("route_destination_id", routeDestinationID);
        params.put("notes", "1");
        String strParams;
        try {
            strParams = Manager.transformParams(params);
            response = Manager.makeURLConnectionRequest(RequestMethod.GET, addressURL(), strParams);
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    public Notes addAddressNotes(String routeID, String routeDestinationID,
                                    String note, String activityType, Constants.DeviceType deviceType,
                                    Number latitude, Number longitude) {
        Response response = null;
        List <NameValuePair> data = new ArrayList <NameValuePair>();
        data.add(new BasicNameValuePair("strNoteContents", note));
        data.add(new BasicNameValuePair("strUpdateType", activityType));
        Map<String, String> params = new HashMap<>();
        params.put("api_key", Route4Me.getApiKey());
        params.put("route_id", routeID);
        params.put("address_id", routeDestinationID);
        params.put("device_type", deviceType.toString());
        params.put("dev_lat", latitude.toString());
        params.put("dev_lng", longitude.toString());
        String strParams;
        try {
            strParams = Manager.transformParams(params);
            response = Manager.makeURLConnectionRequest(RequestMethod.POST_FORM, addRouteNotesHostURL(), strParams, data);
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        Notes responseObject = getGson().fromJson(response.getResponseBody(), Notes.class);
        return responseObject;
    }

}
