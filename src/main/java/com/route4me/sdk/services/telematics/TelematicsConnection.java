/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.telematics;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import java.util.Map;
import lombok.Data;
import lombok.ToString;

/**
 * A connection between a member and a telematics vendor, with the vendor credentials. The credential fields are
 * left out of {@code toString()}.
 */
@Data
public class TelematicsConnection {

    /** The name of the connection ({@code name}). */
    @QueryParameter("name")
    @SerializedName("name")
    private String name;
    /** The name of the telematics vendor ({@code vendor}). */
    @QueryParameter("vendor")
    @SerializedName("vendor")
    private String vendor;
    /** The host or server of the vendor, for vendors that need one ({@code host}). */
    @QueryParameter("host")
    @SerializedName("host")
    private String host;
    /** The API key at the vendor, for vendors that need one; do not log it ({@code api_key}). */
    @ToString.Exclude
    @QueryParameter("api_key")
    @SerializedName("api_key")
    private String apiKey;
    /** The account ID at the vendor, for vendors that need one ({@code account_id}). */
    @QueryParameter("account_id")
    @SerializedName("account_id")
    private String accountID;
    /** The user name at the vendor, for vendors that need one ({@code username}). */
    @QueryParameter("username")
    @SerializedName("username")
    private String username;
    /** The password at the vendor, for vendors that need one; do not log it ({@code password}). */
    @ToString.Exclude
    @QueryParameter("password")
    @SerializedName("password")
    private String password;
    /** The token of the connection; a credential, so do not log it ({@code connection_token}). */
    @ToString.Exclude
    @QueryParameter("connection_token")
    @SerializedName("connection_token")
    private String connectionToken;
    /** The ID of the telematics vendor ({@code vendor_id}). */
    @QueryParameter("vendor_id")
    @SerializedName("vendor_id")
    private Integer vendorID;
    /** Whether vehicle tracking through this connection is on ({@code is_enabled}). */
    @QueryParameter("is_enabled")
    @SerializedName("is_enabled")
    private Integer isEnabled;
    /** Vehicle tracking interval in seconds ({@code vehicle_position_refresh_rate}). */
    @QueryParameter("vehicle_position_refresh_rate")
    @SerializedName("vehicle_position_refresh_rate")
    private Integer vehiclePositionRefreshRate;
    /** Maximum idle time in seconds ({@code max_idle_time}). */
    @SerializedName("max_idle_time")
    private Integer maxIdleTime;
    /** Synchronized vehicles count ({@code synced_vehicles_count}). */
    @SerializedName("synced_vehicles_count")
    private Integer syncedVehiclesCount;
    /** Total vehicles count ({@code total_vehicles_count}). */
    @SerializedName("total_vehicles_count")
    private Integer totalVehiclesCount;
    /** Total addresses count ({@code total_addresses_count}). */
    @SerializedName("total_addresses_count")
    private Integer totalAddressesCount;
    /** When the vehicles were last reloaded, as a Unix timestamp ({@code last_vehicles_reload}). */
    @SerializedName("last_vehicles_reload")
    private String lastVehiclesReload;
    /** When the addresses were last reloaded, as a Unix timestamp ({@code last_addresses_reload}). */
    @SerializedName("last_addresses_reload")
    private String lastAddressesReload;
    /** When the positions were last reloaded, as a Unix timestamp ({@code last_position_reload}). */
    @SerializedName("last_position_reload")
    private String lastPositionReload;
    /** Metadata, custom key-value storage ({@code metadata}). */
    @QueryParameter("metadata")
    @SerializedName("metadata")
    private Map<String, Object> metadata;
    /** Whether to check the credentials with the vendor before saving ({@code validate_remote_credentials}). */
    @QueryParameter("validate_remote_credentials")
    private String validateRemoteCredentials;

}
