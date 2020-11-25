/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.geocoding;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.DataObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.client.utils.URIBuilder;

/**
 *
 * @author juan
 */
public class GeocodingManager extends Manager {

    public static final String GEOCODING_EP = "/api/geocoder.php";
    private static final String JSON_GEOCODE = "/actions/upload/json-geocode.php";

    public GeocodingManager(String apiKey) {
        super(apiKey);
    }

    public DataObject jsonUploadGeocoder(List<Address> addresses) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(JSON_GEOCODE);
        AddressesJSONBody body = new AddressesJSONBody(addresses);
        
        return makeJSONRequest(RequestMethod.POST, builder, body, null, DataObject.class);
    }

    public GeocoderWebSockets websocketsGeocoder(List<Address> addresses) {
        GeocoderWebSockets geocoder = new GeocoderWebSockets();
        try {
            DataObject job = jsonUploadGeocoder(addresses);
            geocoder.startGeocoding(job.getOptimizationProblemId(), addresses.size());
        } catch (APIException ex) {
            Logger.getLogger(GeocodingManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return geocoder;

    }
    
    
    @Getter
    @Setter
    private static class AddressesJSONBody {
        @SerializedName("rows")
        private List<Address> addresses;

        private AddressesJSONBody(List<Address> addresses) {
                this.addresses = addresses;
        }
    }
}
