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

/**
 *
 * @author Route4Me
 */
@Data
public class TelematicsConnection {

    @QueryParameter("name")
    @SerializedName("name")
    private String name;
    @QueryParameter("vendor")
    @SerializedName("vendor")
    private String vendor;
    @QueryParameter("host")
    @SerializedName("host")
    private String host;
    @QueryParameter("api_key")
    @SerializedName("api_key")
    private String apiKey;
    @QueryParameter("account_id")
    @SerializedName("account_id")
    private String accountID;
    @QueryParameter("username")
    @SerializedName("username")
    private String username;
    @QueryParameter("password")
    @SerializedName("password")
    private String password;
    @QueryParameter("connection_token")
    @SerializedName("connection_token")
    private String connectionToken;
    @QueryParameter("vendor_id")
    @SerializedName("vendor_id")
    private Integer vendorID;
    @QueryParameter("is_enabled")
    @SerializedName("is_enabled")
    private Integer isEnabled;
    @QueryParameter("vehicle_position_refresh_rate")
    @SerializedName("vehicle_position_refresh_rate")
    private Integer vehiclePositionRefreshRate;
    @SerializedName("max_idle_time")
    private Integer maxIdleTime;
    @SerializedName("synced_vehicles_count")
    private Integer syncedVehiclesCount;
    @SerializedName("total_vehicles_count")
    private Integer totalVehiclesCount;
    @SerializedName("total_addresses_count")
    private Integer totalAddressesCount;
    @SerializedName("last_vehicles_reload")
    private String lastVehiclesReload;
    @SerializedName("last_addresses_reload")
    private String lastAddressesReload;
    @SerializedName("last_position_reload")
    private String lastPositionReload;
    @QueryParameter("metadata")
    @SerializedName("metadata")
    private Map<String, Object> metadata;
    @QueryParameter("validate_remote_credentials")
    String validateRemoteCredentials;

}
