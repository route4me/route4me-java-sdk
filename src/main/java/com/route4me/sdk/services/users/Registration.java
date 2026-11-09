/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.users;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

/** The answer of {@link UsersManager#registerUser(UserRegistration)}: the new account. */
@Data
public class Registration {

    /** Process status ({@code status}). */
    @SerializedName("status")
    private Boolean status;
    /** Session ID ({@code session_id}). */
    @ToString.Exclude
    @SerializedName("session_id")
    private String sessionID;
    /** Session GUID ({@code session_guid}). */
    @ToString.Exclude
    @SerializedName("session_guid")
    private String sessionGUID;
    /** The email address of the registered user ({@code member_email}). */
    @SerializedName("member_email")
    private String memberEmail;
    /** Member ID ({@code member_id}). */
    @SerializedName("member_id")
    private Integer memberID;
    /** The API key of the new account; a credential, so do not log it ({@code api_key}). */
    @ToString.Exclude
    @SerializedName("api_key")
    private String apiKey;
    /** The account type ID ({@code account_type_id}). */
    @SerializedName("account_type_id")
    private Integer accountTypeID;
    /** Account type alias ({@code account_type_alias}). */
    @SerializedName("account_type_alias")
    private Integer accountTypeAlias;
    /** Maximum allowed number of the stops per route ({@code max_stops_per_route}). */
    @SerializedName("max_stops_per_route")
    private Integer maxStopsPerRoute;
    /** Maximum allowed number of the generated routes ({@code max_routes}). */
    @SerializedName("max_routes")
    private Integer maxRoutes;
    /** Number of the planned routes by the user ({@code routes_planned}). */
    @SerializedName("routes_planned")
    private Integer routesPlanned;

}
