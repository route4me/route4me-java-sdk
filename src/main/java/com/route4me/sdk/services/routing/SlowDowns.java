/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;


/**
 *
 * @author juan
 */
@Data
public class SlowDowns {
    
    @SerializedName("service_time")
    @QueryParameter("service_time")
    private Integer serviceTime;
    @SerializedName("travel_time")
    @QueryParameter("travel_time")
    private Integer travelTime;

    
}
