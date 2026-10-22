/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;

/** How much the service and travel times are slowed down, in percent ({@code slowdowns}). */
@Data
public class SlowDowns {

    /** Service time slowdowon (percent) ({@code service_time}). */
    @SerializedName("service_time")
    @QueryParameter("service_time")
    private Integer serviceTime;
    /** Travel time slowdowon (percent) ({@code travel_time}). */
    @SerializedName("travel_time")
    @QueryParameter("travel_time")
    private Integer travelTime;

}
