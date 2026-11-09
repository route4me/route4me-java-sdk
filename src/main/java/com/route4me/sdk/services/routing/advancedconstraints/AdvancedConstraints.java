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

/**
 * An advanced constraint of an optimization ({@code advanced_constraints}): a group of drivers with their time
 * windows, capacity, tags and depot, for {@code Parameters.setAdvancedConstraints}.
 */
@Data
public class AdvancedConstraints {

    /** Maximum cargo volume per route ({@code max_cargo_volume}). */
    @SerializedName("max_cargo_volume")
    @QueryParameter("max_cargo_volume")
    private Double maxCargoVolume;

    /**
     * Vehicle capacity. How much total cargo can be transported per route (units, e.g. cubic meters) ({@code
     * max_capacity}).
     */
    @SerializedName("max_capacity")
    @QueryParameter("max_capacity")
    private Integer maxCapacity;

    /** The number of drivers (routes) of the group ({@code members_count}). */
    @SerializedName("members_count")
    @QueryParameter("members_count")
    private Integer membersCount;

    /**
     * The time windows the drivers of the group work in, each as [start, end] in seconds from midnight ({@code
     * available_time_windows}).
     */
    @SerializedName("available_time_windows")
    @QueryParameter("available_time_windows")
    private List<List<Integer>> availableTimeWindows;

    /** The tags that the stops of the group must have ({@code tags}). */
    @SerializedName("tags")
    @QueryParameter("tags")
    private List<String> tags;

    /** The member IDs of the drivers of the group ({@code route4me_members_id}). */
    @SerializedName("route4me_members_id")
    @QueryParameter("route4me_members_id")
    private List<Integer> route4meMembersID;

    /** The depot of this group. If it has no required skills, it gets the group's `tags` ({@code depot_address}). */
    @SerializedName("depot_address")
    @QueryParameter("depot_address")
    private Address depotAddress;

    /**
     * The order in which the routes of the group visit their locations; an empty string stands for the other stops
     * ({@code location_sequence_pattern}).
     */
    @SerializedName("location_sequence_pattern")
    @QueryParameter("location_sequence_pattern")
    private List<Object> locationSequencePattern;

    /** Group name of the advanced constraints ({@code group}). */
    @SerializedName("group")
    @QueryParameter("group")
    private String group;


}
