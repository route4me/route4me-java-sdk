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

/**
 *
 * @author juan
 */
@Data
public class TerritoryAllocated {

    @SerializedName("address")
    private String address;
    @SerializedName("original")
    private String orginal;
    @SerializedName("lat")
    private Double latitude;
    @SerializedName("lng")
    private Double longitude;
    @SerializedName("confidence")
    private String confidence;
    @SerializedName("type")
    private String type;
    @SerializedName("territories")
    private List<Territory> territories;

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
     * @return the territories
     */
    public List<Territory> getTerritories() {
        return territories == null ? new ArrayList<>() : territories;
    }

}
