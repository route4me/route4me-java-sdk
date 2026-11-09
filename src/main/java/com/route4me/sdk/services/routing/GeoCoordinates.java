/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A latitude and a longitude. */
@Data
public class GeoCoordinates {

    /** Latitude ({@code lat}). */
    @SerializedName("lat")
    private Double latitude;
    /** Longitude ({@code lng}). */
    @SerializedName("lng")
    private Double longitude;

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

}
