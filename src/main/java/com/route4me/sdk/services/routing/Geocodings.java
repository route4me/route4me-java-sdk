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

/** A match of the geocoder for an address. */
@Data
public class Geocodings {

    /** A unique identifier for the geocoding ({@code key}). */
    @SerializedName("key")
    @QueryParameter("key")
    private String key;
    /** Specific description of the geocoding result ({@code name}). */
    @SerializedName("name")
    @QueryParameter("name")
    private String name;
    /** Boundary box ({@code bbox}). */
    @SerializedName("bbox")
    @QueryParameter("bbox")
    private List<Double> bBox;
    /** The latitude of the geocoded address ({@code lat}). */
    @SerializedName("lat")
    private Double latitude;
    /** The longitude of the geocoded address ({@code lng}). */
    @SerializedName("lng")
    private Double longitude;
    /** Confidance level in the address geocoding: high, medium, low ({@code confidence}). */
    @SerializedName("confidence")
    @QueryParameter("confidence")
    private String confidence;
    /** Non-standardized. Is used for tooltip ("Street", "City" etc) ({@code type}). */
    @SerializedName("type")
    @QueryParameter("type")
    private String type;
    /** The postal code of the geocoded address ({@code postalCode}). */
    @SerializedName("postalCode")
    @QueryParameter("postalCode")
    private String postalCode;
    /** If the region is known, it's returned here ({@code countryRegion}). */
    @SerializedName("countryRegion")
    @QueryParameter("countryRegion")
    private String countryRegion;
    /** The coordinates of the match ({@code coordinates}). */
    @SerializedName("coordinates")
    @QueryParameter("coordinates")
    private GeoCoordinates coordinates;
    /** The address curbside coordinates ({@code curbside_coordinates}). */
    @SerializedName("curbside_coordinates")
    @QueryParameter("curbside_coordinates")
    private GeoCoordinates curbsideCoordinates;
    /** The rooftop coordinates of the match, when the geocoder has them ({@code rooftop_coordinates}). */
    @SerializedName("rooftop_coordinates")
    @QueryParameter("rooftop_coordinates")
    private GeoCoordinates rooftopCoordinates;

    /** Creates an empty match. */
    public Geocodings() {
    }

    /**
     * Creates a match.
     *
     * @param latitude the latitude
     * @param longitude the longitude
     * @param type the type, such as {@code street}
     * @param confidence the confidence, such as {@code high}
     * @param address the address text ({@code name})
     */
    public Geocodings(Double latitude, Double longitude, String type, String confidence, String address) {
        this.name = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.confidence = confidence;
    }

    /**
     * Returns the latitude.
     *
     * @return the latitude, or 0 when it is not set
     */
    public Double getLatitude() {
        return latitude == null ? 0.0 : latitude;
    }

    /**
     * Returns the longitude.
     *
     * @return the longitude, or 0 when it is not set
     */
    public Double getLongitude() {
        return longitude == null ? 0.0 : longitude;
    }

    /**
     * Returns the confidence of the match.
     *
     * @return the confidence, or {@code "0"} when it is not set
     */
    public String getConfidence() {
        return confidence == null ? "0" : confidence;
    }

    /**
     * Returns the type of the match.
     *
     * @return the type, or {@code "invalid"} when it is not set
     */
    public String getType() {
        return type == null ? "invalid" : type;
    }

    /**
     * Returns the coordinates of the match.
     *
     * @return the coordinates; empty coordinates (latitude and longitude 0) when they are not set
     */
    public GeoCoordinates getCoordinates() {
        return coordinates == null ? new GeoCoordinates() : coordinates;
    }

}
