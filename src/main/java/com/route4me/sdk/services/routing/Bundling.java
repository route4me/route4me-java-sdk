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

/** Address bundling ({@code bundling}): stops that share a value are visited together. */
@Data
public class Bundling {

    /**
     * 0 = id, 1 = address, 2 = coordinates, 3 = address obj field (1 param), 4 = address custom field (1 param) ({@code
     * mode}).
     */
    @SerializedName("mode")
    @QueryParameter("mode")
    private Integer mode;
    /** The fields to bundle by: address field names for mode 3, custom field names for mode 4 ({@code mode_params}). */
    @SerializedName("mode_params")
    @QueryParameter("mode_params")
    private List<String> modeParams;
    /** Service time rules of the address bundling ( ) ({@code service_time_rules}). */
    @SerializedName("service_time_rules")
    @QueryParameter("service_time_rules")
    private ServiceTimeRules serviceTimeRules;
    /** 1 = Keep as separate destinations, 2 = Merge into single destination ({@code merge_mode}). */
    @SerializedName("merge_mode")
    @QueryParameter("merge_mode")
    private Integer mergeMode = 1;
    /** How the time windows of bundled stops are combined ({@code time_window_rules}). */
    @SerializedName("time_window_rules")
    @QueryParameter("time_window_rules")
    private TimeWindowRules timeWindowRules;


}
