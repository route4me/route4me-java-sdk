/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

/**
 *
 * @author juan
 */

@Data
class Geocodings {

    @SerializedName("key")
    private String key;
    @SerializedName("name")
    private String name;
    @SerializedName("bbox")
    List<Integer> bBox;
    @SerializedName("lat")
    private Double latitude;
    @SerializedName("lng")
    private Double longitude;
    @SerializedName("confidence")
    private String confidence;
    @SerializedName("type")
    private String type;
    @SerializedName("postalCode")
    private String postalCode;
    @SerializedName("countryRegion")
    private String countryRegion;
    @SerializedName("curbside_coordinates")
    private GeoCoordinates curbsideCoordinates;
    
}
