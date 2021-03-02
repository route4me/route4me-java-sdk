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

@Data
public class Geocodings {

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
    @SerializedName("coordinates")
    @QueryParameter("coordinates")
    private GeoCoordinates coordinates;
    @SerializedName("curbside_coordinates")
    @QueryParameter("curbside_coordinates")
    private GeoCoordinates curbsideCoordinates;
    @SerializedName("rooftop_coordinates")
    @QueryParameter("rooftop_coordinates")
    private GeoCoordinates rooftopCoordinates;

    public Geocodings() {
    }

    public Geocodings(Double latitude, Double longitude, String type, String confidence, String address) {
        this.name = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.confidence = confidence;
    }

    /**
     * @return the latitude
     */
    public Double getLatitude() {
        return latitude == null ? 0.0 : latitude;
    }

    /**
     * @return the longitude
     */
    public Double getLongitude() {
        return longitude == null ? 0.0 : longitude;
    }

    /**
     * @return the confidence
     */
    public String getConfidence() {
        return confidence == null ? "0" : confidence;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type == null ? "invalid" : type;
    }

    /**
     * @return the coordinates
     */
    public GeoCoordinates getCoordinates() {
        return coordinates == null ? new GeoCoordinates() : coordinates;
    }

}
