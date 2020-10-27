/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import java.util.List;
import lombok.Data;

/**
 *
 * @author juan
 */
@Data
class Geocodings {

    @SerializedName("key")
    @QueryParameter("key")
    private String key;
    @SerializedName("name")
    @QueryParameter("name")
    private String name;
    @SerializedName("bbox")
    @QueryParameter("bbox")
    private List<Double> bBox;
    @SerializedName("lat")
    private Double latitude;
    @SerializedName("lng")
    private Double longitude;
    @SerializedName("confidence")
    @QueryParameter("confidence")
    private String confidence;
    @SerializedName("type")
    @QueryParameter("type")
    private String type;
    @SerializedName("postalCode")
    @QueryParameter("postalCode")
    private String postalCode;
    @SerializedName("countryRegion")
    @QueryParameter("countryRegion")
    private String countryRegion;
    @SerializedName("curbside_coordinates")
    @QueryParameter("curbside_coordinates")
    private GeoCoordinates curbsideCoordinates;
    
}
