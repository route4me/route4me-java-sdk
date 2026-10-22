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
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 * Manages the vehicles of the account through the older vehicle API
 * ({@code https://wh.route4me.com/modules/api/vehicles}), which takes the API key as the {@code api_key} query
 * parameter. {@link VehiclesManagerV5} covers the V5 vehicle API.
 *
 * @deprecated the older vehicle API is obsolete: the C# SDK marks its {@code GetVehicles}, {@code GetVehicle},
 *     {@code CreateVehicle}, {@code UpdateVehicle} and {@code DeleteVehicle} as {@code [Obsolete]}. Use
 *     {@link VehiclesManagerV5}. This class will be removed in the next major version.
 */
@Deprecated
public class VehiclesManager extends Manager {
    /** The path of the vehicles, on {@code wh.route4me.com}. */
    public static final String VEHICLES_EP = "/modules/api/vehicles";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as the {@code api_key} query parameter
     */
    public VehiclesManager(String apiKey) {
        super(apiKey);
    }

    private URIBuilder getURI(String endpoint, Object... segments) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        Manager.withPathSegments(builder, endpoint, segments);
        return builder;
    }

    /**
     * Lists the vehicles: {@code GET https://wh.route4me.com/modules/api/vehicles}.
     *
     * @return the vehicles
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @deprecated the older vehicle API is obsolete: the C# SDK marks {@code GetVehicles} as {@code [Obsolete]}.
     *     Use {@link VehiclesManagerV5#getVehicles(VehicleListRequest)}. This method will be removed in the next major
     *     version.
     */
    @Deprecated
    public List<Vehicles> getVehicles() throws APIException {
        URIBuilder builder = this.getURI(VEHICLES_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, new TypeToken<ArrayList<Vehicles>>() {
        }.getType());
    }

    /**
     * Gets one vehicle: {@code GET https://wh.route4me.com/modules/api/vehicles/{vehicleID}}.
     *
     * @param vehicleID the vehicle ID
     * @return the vehicle
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @deprecated the older vehicle API is obsolete: the C# SDK marks {@code GetVehicle} as {@code [Obsolete]}. Use
     *     {@link VehiclesManagerV5#getVehicleById(String)}. This method will be removed in the next major version.
     */
    @Deprecated
    public Vehicles getVehicle(String vehicleID) throws APIException {
        URIBuilder builder = this.getURI(VEHICLES_EP, vehicleID);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, Vehicles.class);
    }

    /**
     * Deletes a vehicle: {@code DELETE https://wh.route4me.com/modules/api/vehicles/{vehicleID}}.
     *
     * @param vehicleID the vehicle ID
     * @return the deleted vehicle
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @deprecated the older vehicle API is obsolete: the C# SDK marks {@code DeleteVehicle} as {@code [Obsolete]}. Use
     *     {@link VehiclesManagerV5#deleteVehicle(String)}. This method will be removed in the next major version.
     */
    @Deprecated
    public Vehicles deleteVehicle(String vehicleID) throws APIException {
        URIBuilder builder = this.getURI(VEHICLES_EP, vehicleID);
        return this.makeRequest(RequestMethod.DELETE, builder, (HttpEntity) null, Vehicles.class);
    }

    /**
     * Creates a vehicle: {@code POST https://wh.route4me.com/modules/api/vehicles}, with the vehicle as the JSON body.
     *
     * @param vehicle the vehicle
     * @return the created vehicle
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @deprecated the older vehicle API is obsolete: the C# SDK marks {@code CreateVehicle} as {@code [Obsolete]}. Use
     *     {@link VehiclesManagerV5#createVehicle(VehicleV5)}. This method will be removed in the next major version.
     */
    @Deprecated
    public Vehicles newVehicle(Vehicles vehicle) throws APIException {
        URIBuilder builder = this.getURI(VEHICLES_EP);
        return this.makeJSONRequest(RequestMethod.POST, builder,  vehicle, Vehicles.class);
    }

    /**
     * Updates a vehicle: {@code PUT https://wh.route4me.com/modules/api/vehicles/{vehicleId}}, with the vehicle as the
     * JSON body.
     *
     * @param vehicle the vehicle, with its vehicle ID
     * @return the updated vehicle
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @deprecated the older vehicle API is obsolete: the C# SDK marks {@code UpdateVehicle} as {@code [Obsolete]}.
     *     Use {@link VehiclesManagerV5#updateVehicle(String, VehicleV5)}. This method will be removed in the next major
     *     version.
     */
    @Deprecated
    public Vehicles updateVehicle(Vehicles vehicle) throws APIException {
        URIBuilder builder = this.getURI(VEHICLES_EP, vehicle.getVehicleId());
        return this.makeJSONRequest(RequestMethod.PUT, builder, vehicle, Vehicles.class);
    }



}
