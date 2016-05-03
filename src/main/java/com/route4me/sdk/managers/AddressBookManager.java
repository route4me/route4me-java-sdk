/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.managers;

import com.google.gson.Gson;
import com.route4me.sdk.Route4Me;
import com.route4me.sdk.exception.APIConnectionException;
import com.route4me.sdk.exception.InvalidRequestException;
import com.route4me.sdk.model.Contact;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.serdes.ContactDeserializer;
import com.route4me.sdk.serdes.ContactSerializer;
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
public class AddressBookManager extends Manager {

    private Contact contact = null;

    public AddressBookManager(String apiKey) {
        super(apiKey);
    }

    public Contact createContact(Contact contact) {
        Response response = null;
        setContact(contact);
        String params;
        try {
            params = Manager.transformParams(getParams());
            response = Manager.makeURLConnectionRequest(RequestMethod.POST, addressBookURL(), params, getJsonContact());
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        Contact responseObject = ContactDeserializer.GSON_DESERIALIZER.fromJson(response.getResponseBody(), Contact.class);
        return responseObject;
    }

    public Contact updateContact(Contact contact) {
        Response response = null;
        setContact(contact);
        String params;
        try {
            params = Manager.transformParams(getParams());
            response = Manager.makeURLConnectionRequest(RequestMethod.PUT, addressBookURL(), params, getJsonContact());
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        Contact responseObject = ContactDeserializer.GSON_DESERIALIZER.fromJson(response.getResponseBody(), Contact.class);
        return responseObject;
    }

    public Contact getContact(Number addressID) {
        Response response = null;
        Map<String, String> params = new HashMap<>();
        params.put("api_key", Route4Me.apiKey);
        params.put("address_id", "'" + addressID.toString() + "'");
        String strParams;
        try {
            strParams = Manager.transformParams(params);
            response = Manager.makeURLConnectionRequest(RequestMethod.GET, addressBookURL(), strParams);
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        Contact responseObject = ContactDeserializer.GSON_DESERIALIZER.fromJson(response.getResponseBody(), Contact.class);
        return responseObject;
    }

    public Contact getContacts(Integer limit, Integer offset) {
        Response response = null;
        Map<String, String> params = new HashMap<>();
        params.put("api_key", Route4Me.apiKey);
        params.put("limit", limit.toString());
        params.put("Offset", offset.toString());
        String strParams;
        try {
            strParams = Manager.transformParams(params);
            response = Manager.makeURLConnectionRequest(RequestMethod.GET, addressBookURL(), strParams);
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        Contact responseObject = ContactDeserializer.GSON_DESERIALIZER.fromJson(response.getResponseBody(), Contact.class);
        return responseObject;
    }

    public String getJsonContact() {
        if (getContact() != null) {
            String serializedData = ContactSerializer.GSON_SERIALIZER.toJson(contact);
            return serializedData;
        }
        return null;
    }

    /**
     * @return the contact
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * @param contact the contact to set
     */
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Response deleteContact(List<Number> addressIDs) {

        Response response = null;
        Map<String, String> params = new HashMap<>();
        params.put("api_key", Route4Me.apiKey);
        Gson gson;
        gson = new Gson();
        AddressesID addressIds = new AddressesID(addressIDs);
        String addressIDsSTR;
        addressIDsSTR = gson.toJson(addressIds);
        String strParams;
        try {
            strParams = Manager.transformParams(params);
            response = Manager.makeURLConnectionRequest(RequestMethod.DELETE_BODY, addressBookURL(), strParams, addressIDsSTR);
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    class AddressesID {

        private List<Number> address_ids;

        private AddressesID(List<Number> addressIDs) {
            this.address_ids = addressIDs;
        }

        /**
         * @return the addressIds
         */
        public List<Number> getAddressIds() {
            return address_ids;
        }

        /**
         * @param addressIds the addressIds to set
         */
        public void setAddressIds(List<Number> addressIds) {
            this.address_ids = addressIds;
        }
    }
}
