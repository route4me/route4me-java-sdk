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

/** The body of the {@code insertBreakAfter...} methods of {@link RouteBreaksManager}. */
@Data
public class RouteBreaks {

    /** The routes to add the breaks to ({@code route_id}). */
    @SerializedName("route_id")
    @QueryParameter("route_id")
    @SuppressWarnings("checkstyle:VisibilityModifier") // public field, kept for compatibility
    public String[] routeID;
    /** The break rules ({@code breaks}). */
    @SerializedName("breaks")
    @QueryParameter("breaks")
    @SuppressWarnings("checkstyle:VisibilityModifier") // public field, kept for compatibility
    public List<RouteBreak> breaks;
    /** Whether to replace the existing breaks of the routes ({@code replace_existing_breaks}). */
    @SerializedName("replace_existing_breaks")
    @QueryParameter("replace_existing_breaks")
    @SuppressWarnings("checkstyle:VisibilityModifier") // public field, kept for compatibility
    public boolean replaceExisting;

}
