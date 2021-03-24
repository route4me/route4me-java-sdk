/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.users;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 *
 * @author Route4Me
 */
@Data
public class Registration {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("session_id")
    private String sessionID;
    @SerializedName("session_guid")
    private String sessionGUID;
    @SerializedName("member_email")
    private String memberEmail;
    @SerializedName("member_id")
    private Integer memberID;
    @SerializedName("api_key")
    private String apiKey;
    @SerializedName("account_type_id")
    private Integer accountTypeID;
    @SerializedName("account_type_alias")
    private Integer accountTypeAlias;
    @SerializedName("max_stops_per_route")
    private Integer maxStopsPerRoute;
    @SerializedName("max_routes")
    private Integer maxRoutes;
    @SerializedName("routes_planned")
    private Integer routesPlanned;

}
