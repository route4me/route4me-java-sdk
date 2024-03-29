/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.routing.advancedconstraints;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import com.route4me.sdk.services.routing.Address;
import java.util.List;
import lombok.Data;

@Data
public class AdvancedConstraints {
    
    @SerializedName("max_cargo_volume")
    @QueryParameter("max_cargo_volume")
    private Double maxCargoVolume;

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
    private List<Integer> route4meMembersID;
    
    @SerializedName("depot_address")
    @QueryParameter("depot_address")
    private Address depotAddress;

    @SerializedName("location_sequence_pattern")
    @QueryParameter("location_sequence_pattern")
    private List<Object> locationSequencePattern;

    @SerializedName("group")
    @QueryParameter("group")
    private String group;
    
    
}
