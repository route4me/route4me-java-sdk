/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.territories;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/** A geocoded address, and the territories it falls in. */
@Data
public class TerritoryAllocated {

    /** The address ({@code address}). */
    @SerializedName("address")
    private String address;
    /** The address as it was entered ({@code original}). */
    @SerializedName("original")
    private String orginal;
    /** Latitude ({@code lat}). */
    @SerializedName("lat")
    private Double latitude;
    /** Longitude ({@code lng}). */
    @SerializedName("lng")
    private Double longitude;
    /** The confidence of the geocoding: {@code high}, {@code medium} or {@code low} ({@code confidence}). */
    @SerializedName("confidence")
    private String confidence;
    /** The geocoding type of the address, such as {@code street_address} or {@code locality} ({@code type}). */
    @SerializedName("type")
    private String type;
    /** The territories that contain the address ({@code territories}). */
    @SerializedName("territories")
    private List<Territory> territories;

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
     * Returns the confidence of the geocoding.
     *
     * @return the confidence, or {@code "0"} when it is not set
     */
    public String getConfidence() {
        return confidence == null ? "0" : confidence;
    }

    /**
     * Returns the geocoding type.
     *
     * @return the type, or {@code "invalid"} when it is not set
     */
    public String getType() {
        return type == null ? "invalid" : type;
    }

    /**
     * Returns the territories the address falls in.
     *
     * @return the territories; an empty list when they are not set
     */
    public List<Territory> getTerritories() {
        return territories == null ? new ArrayList<>() : territories;
    }

}
