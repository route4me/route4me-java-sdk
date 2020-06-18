/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.routing.breaks;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import java.util.List;
import lombok.Data;

/**
 *
 * @author juan
 */
@Data
public class RouteBreaks {

    @SerializedName("route_id")
    @QueryParameter("route_id")
    public String[] routeID;
    @SerializedName("breaks")
    @QueryParameter("breaks")
    public List<RouteBreak> breaks;
    @SerializedName("replace_existing_breaks")
    @QueryParameter("replace_existing_breaks")
    public boolean replaceExisting;

}
