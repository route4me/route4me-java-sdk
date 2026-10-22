/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.routing.breaks;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;

/** One break rule of a {@link RouteBreaks} request. */
@Data
public class RouteBreak {

    /** What the break follows: a value of {@link RouteBreaksEnum.RouteBreaksType} ({@code type}). */
    @SerializedName("type")
    @QueryParameter("type")
    @SuppressWarnings("checkstyle:VisibilityModifier") // public field, kept for compatibility
    public String type;
    /** The length of the break, in seconds ({@code duration}). */
    @SerializedName("duration")
    @QueryParameter("duration")
    @SuppressWarnings("checkstyle:VisibilityModifier") // public field, kept for compatibility
    public Integer duration;
    /**
     * The amount the break follows (stops, or seconds of travel, service or elapsed time), one per route ({@code
     * params}).
     */
    @SerializedName("params")
    @QueryParameter("params")
    @SuppressWarnings("checkstyle:VisibilityModifier") // public field, kept for compatibility
    public int[] params;

}
