/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of {@link RoutingManager#renameRoute(String, String)}. */
@Data
public class RouteRenamedStatus {

    /** Whether legs for this vendor were recomputed and persisted ({@code success}). */
    @SerializedName("success")
    private Boolean success;
    /** Human-readable error message when `success=false` ({@code error}). */
    @SerializedName("error")
    private String error;

}
