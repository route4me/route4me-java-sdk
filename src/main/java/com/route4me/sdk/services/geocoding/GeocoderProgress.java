/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.geocoding;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;

/** A {@code geocode_progress} message of the websocket geocoder. */
@Data
public class GeocoderProgress {
    /** The number of addresses of the geocoding job ({@code total}). */
    @SerializedName("total")
    @QueryParameter("total")
    private Integer total;

    /** The number of addresses geocoded so far ({@code done}). */
    @SerializedName("done")
    @QueryParameter("done")
    private Integer done;



}
