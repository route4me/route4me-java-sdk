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

    /** Whether the route was renamed ({@code success}). */
    @SerializedName("success")
    private Boolean success;
    /** The error message when {@code success} is {@code false} ({@code error}). */
    @SerializedName("error")
    private String error;

}
