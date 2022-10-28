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
public class Bundling {
    
    @SerializedName("mode")
    @QueryParameter("mode")
    private Integer mode;
    @SerializedName("mode_params")
    @QueryParameter("mode_params")
    private List<String> modeParams;
    @SerializedName("service_time_rules")
    @QueryParameter("service_time_rules")
    private ServiceTimeRules serviceTimeRules;
    @SerializedName("merge_mode")
    @QueryParameter("merge_mode")
    private Integer mergeMode = 1;
    @SerializedName("time_window_rules")
    @QueryParameter("time_window_rules")
    private TimeWindowRules timeWindowRules;

    
}
