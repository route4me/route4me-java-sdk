/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 *
 * @author juan
 */
@Data
public class GeoCoordinates {

    @SerializedName("lat")
    private Double latitude;
    @SerializedName("lng")
    private Double longitude;

}
