/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;

@Data
public class Bundling {
    
    @SerializedName("mode")
    @QueryParameter("modes")
    private Integer mode;
    @SerializedName("service_time_rules")
    @QueryParameter("service_time_rules")
    private ServiceTimeRules serviceTimeRules;

    
}
