/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.geocoding;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;

/**
 *
 * @author juan
 */
@Data
public class GeocoderProgress {
    @SerializedName("total")
    @QueryParameter("total")
    private Integer total;

    @SerializedName("done")
    @QueryParameter("done")
    private Integer done;


    
}
