/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.vehicles;

import com.google.gson.reflect.TypeToken;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 *
 * @author juan
 */
public class VehiclesManager extends Manager {
    public static final String VEHICLES_EP = "/modules/api/vehicles";

    public VehiclesManager(String apiKey) {
        super(apiKey);
    }
    
    private URIBuilder getURI(String endpoint){
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        builder.setPath(endpoint);
        return builder;
    }
    
    public List<Vehicles> getVehicles() throws APIException {
        URIBuilder builder = this.getURI(VEHICLES_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, new TypeToken<ArrayList<Vehicles>>() {
        }.getType());
    }

    public Vehicles getVehicle(String vehicleID) throws APIException {
        URIBuilder builder = this.getURI(VEHICLES_EP + "/" + vehicleID);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, Vehicles.class);
    }
            
    public Vehicles deleteVehicle(String vehicleID) throws APIException {
        URIBuilder builder = this.getURI(VEHICLES_EP + "/" + vehicleID);
        return this.makeRequest(RequestMethod.DELETE, builder, (HttpEntity) null, Vehicles.class);
    }

    public Vehicles newVehicle(Vehicles vehicle) throws APIException {
        URIBuilder builder = this.getURI(VEHICLES_EP);
        return this.makeJSONRequest(RequestMethod.POST, builder,  vehicle, Vehicles.class);
    }

    public Vehicles updateVehicle(Vehicles vehicle) throws APIException {
        URIBuilder builder = this.getURI(VEHICLES_EP + "/" + vehicle.getVehicleId());
        return this.makeJSONRequest(RequestMethod.PUT, builder, vehicle, Vehicles.class);
    }
    
    
            
}
