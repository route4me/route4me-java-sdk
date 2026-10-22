/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.routing.breaks;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;

/**
 *
 * @author juan
 */
@Data
public class RouteBreak {

    @SerializedName("type")
    @QueryParameter("type")
    @SuppressWarnings("checkstyle:VisibilityModifier") // public field, kept for compatibility
    public String type;
    @SerializedName("duration")
    @QueryParameter("duration")
    @SuppressWarnings("checkstyle:VisibilityModifier") // public field, kept for compatibility
    public Integer duration;
    @SerializedName("params")
    @QueryParameter("params")
    @SuppressWarnings("checkstyle:VisibilityModifier") // public field, kept for compatibility
    public int[] params;

}
