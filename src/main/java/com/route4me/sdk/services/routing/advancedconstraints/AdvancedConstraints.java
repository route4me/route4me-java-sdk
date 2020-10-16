/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.routing.advancedconstraints;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import java.util.List;
import lombok.Data;

@Data
public class AdvancedConstraints {
    
    @SerializedName("max_cargo_volume")
    @QueryParameter("max_cargo_volume")
    private float maxCargoVolume;

    @SerializedName("max_capacity")
    @QueryParameter("max_capacity")
    private Integer maxCapacity;
    
    
    @SerializedName("members_count")
    @QueryParameter("members_count")
    private Integer membersCount;


    @SerializedName("available_time_windows")
    @QueryParameter("available_time_windows")
    private List<List<Integer>> availableTimeWindows;
            
            
    @SerializedName("tags")
    @QueryParameter("tags")
    private List<String> tags;
    
            
    @SerializedName("route4me_members_id")
    @QueryParameter("route4me_members_id")
    private List<Double> route4meMembersID;
    
    
}
